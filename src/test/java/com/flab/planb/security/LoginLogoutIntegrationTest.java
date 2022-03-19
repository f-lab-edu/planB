package com.flab.planb.security;

import com.flab.planb.TestUtils;
import com.flab.planb.config.DBConfig;
import com.flab.planb.config.PushBatchConfig;
import com.flab.planb.config.RootConfig;
import com.flab.planb.config.SecurityConfig;
import com.flab.planb.config.ServletConfig;
import com.flab.planb.dto.member.Login;
import com.flab.planb.dto.member.Member;
import com.flab.planb.service.member.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.util.UriComponentsBuilder;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

@WebAppConfiguration
@Transactional
@ExtendWith({SpringExtension.class})
@ContextConfiguration(
    classes = {
        ServletConfig.class,
        RootConfig.class,
        DBConfig.class,
        SecurityConfig.class,
        PushBatchConfig.class
    }
)
@PropertySource(
    {
        "file:src/main/resources/properties/*.properties",
        "file:src/main/resources/messages/*.properties",
        "file:src/main/resources/logback-dev.xml",
        "file:src/main/resources/mapper/*.xml"
    }
)
public class LoginLogoutIntegrationTest {

    private final String URI = "/members/login";
    private final String illegalFailCode = "ILLEGAL_ARGUMENT_FAIL";
    private final String loginFailCode = "LOGIN_FAIL_001";
    @Autowired
    private WebApplicationContext webContext;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private MemberService memberService;
    private MockMvc mockMvc;
    private Member member;
    private Login login;

    private URI getUri(String uri) {
        return UriComponentsBuilder.fromUriString(uri).build().encode().toUri();
    }

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webContext)
                                 .addFilter(new CharacterEncodingFilter(StandardCharsets.UTF_8.toString(), true))
                                 .apply(SecurityMockMvcConfigurers.springSecurity())
                                 .build();
        login = new Login("memberTest", "test1234");
        member = Member.builder().memberId(login.getMemberId()).passwd(passwordEncoder.encode(login.getPasswd()))
                       .nickname("멤버테스트").tel("01012345678").build();
        memberService.saveMemberInfo(member);
    }

    @Test
    @DisplayName("memberId NotEmpty 실패")
    void when_memberid_is_empty_expected_bad_request() throws Exception {
        // given
        login = new Login("", "test1234");
        // when
        ResultActions actions = TestUtils.requestPost(mockMvc, URI, login);
        // then
        TestUtils.expectBadRequest(actions, illegalFailCode);
    }

    @Test
    @DisplayName("passwd NotEmpty 실패")
    void when_passwd_is_empty_expected_bad_request() throws Exception {
        // given
        login = new Login("memberTest", "");
        // when
        ResultActions actions = TestUtils.requestPost(mockMvc, URI, login);
        // then
        TestUtils.expectBadRequest(actions, illegalFailCode);
    }

    @Test
    @DisplayName("존재하지 않은 memberId이므로 로그인 실패")
    void when_memberId_not_existed_expected_bad_request() throws Exception {
        // given
        login = new Login("memberTesttest", "test1234");
        // when
        ResultActions actions = TestUtils.requestPost(mockMvc, URI, login);
        // then
        TestUtils.expectBadRequest(actions, loginFailCode);
    }

    @Test
    @DisplayName("passwd 틀려서 로그인 실패")
    void when_wrong_passwd_expected_bad_request() throws Exception {
        // given
        login = new Login("memberTest", "test1234123444");
        // when
        ResultActions actions = TestUtils.requestPost(mockMvc, URI, login);
        // then
        TestUtils.expectBadRequest(actions, loginFailCode);
    }

    @Test
    @DisplayName("로그인 성공")
    void when_correct_member_login_expected_okt() throws Exception {
        String resultDataCodeKey = "$.data.nickname";
        // given
        // when
        ResultActions actions = TestUtils.requestPost(mockMvc, URI, login);
        // then
        TestUtils.expectEqual(actions, resultDataCodeKey, member.getNickname());

        actions.andExpect(MockMvcResultMatchers.request().sessionAttribute(
            "SPRING_SECURITY_CONTEXT",
            Objects.requireNonNull(actions.andReturn().getRequest().getSession())
                   .getAttribute("SPRING_SECURITY_CONTEXT")));
    }

    @Test
    @WithMockUser
    @DisplayName("로그아웃 성공")
    void when_logout_expected_okt() throws Exception {
        // given
        TestUtils.requestPost(mockMvc, URI, login);
        // when
        ResultActions actions = TestUtils.requestPost(mockMvc, "/members/logout", null);
        // then
        actions.andExpect(MockMvcResultMatchers.status().isOk())
               .andExpect(MockMvcResultMatchers.request().sessionAttributeDoesNotExist("SPRING_SECURITY_CONTEXT"));
    }

}