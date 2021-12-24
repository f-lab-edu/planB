package com.flab.planb.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flab.planb.config.DBConfig;
import com.flab.planb.config.RootConfig;
import com.flab.planb.config.SecurityConfig;
import com.flab.planb.config.ServletConfig;
import com.flab.planb.dto.member.LoginDTO;
import com.flab.planb.dto.member.MemberDTO;
import com.flab.planb.service.MemberService;
import org.hamcrest.core.IsEqual;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.util.UriComponentsBuilder;
import java.net.URI;
import java.nio.charset.StandardCharsets;

@WebAppConfiguration
@Transactional
@ExtendWith({SpringExtension.class})
@ContextConfiguration(
    classes = {
        ServletConfig.class,
        RootConfig.class,
        DBConfig.class,
        SecurityConfig.class
    }
)
@PropertySource(
    {
        "file:src/main/resources/properties/*.properties",
        "file:src/main/resources/messages/*.properties",
        "file:src/main/resources/log4j2.xml"
    }
)
public class LoginLogoutIntegrationTest {

    private static final MediaType JSON_UTF_8 = new MediaType(
        MediaType.APPLICATION_JSON.getType(),
        MediaType.APPLICATION_JSON.getSubtype(),
        StandardCharsets.UTF_8
    );
    private final String resultErrorCodeKey = "$.data.errorCode";
    private final String illegalFailCode = "ILLEGAL_ARGUMENT_FAIL";
    private final String deniedFailCode = "DENIED_FAIL";
    @Autowired
    private WebApplicationContext webContext;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private MemberService memberService;
    private MockMvc mockMvc;
    private MemberDTO memberDTO;
    private LoginDTO loginDTO;

    private URI getUri(String uri) {
        return UriComponentsBuilder.fromUriString(uri).build().encode().toUri();
    }

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webContext)
                                 .addFilter(
                                     new CharacterEncodingFilter(
                                         StandardCharsets.UTF_8.toString(),
                                         true)
                                 )
                                 .apply(SecurityMockMvcConfigurers.springSecurity())
                                 .build();
        loginDTO = LoginDTO.builder()
                           .memberId("memberTest")
                           .passwd("test1234")
                           .build();
        memberDTO = MemberDTO.builder()
                             .memberId(loginDTO.getMemberId())
                             .passwd(passwordEncoder.encode(loginDTO.getPasswd()))
                             .nickname("멤버테스트")
                             .tel("01012345678")
                             .build();
        memberService.saveMemberInfo(memberDTO);
    }

    @Test
    @DisplayName("memberId NotEmpty 실패")
    void when_memberid_is_empty_expected_bad_request() throws Exception {
        // given
        loginDTO.setMemberId("");
        // when
        final ResultActions actions = mockMvc.perform(
            MockMvcRequestBuilders.post(getUri("/members/login"))
                                  .contentType(JSON_UTF_8)
                                  .content(new ObjectMapper().writeValueAsString(loginDTO))
        ).andDo(MockMvcResultHandlers.print());
        // then
        actions.andExpect(MockMvcResultMatchers.status().isBadRequest())
               .andExpect(MockMvcResultMatchers.content().contentType(JSON_UTF_8))
               .andExpect(MockMvcResultMatchers.jsonPath(resultErrorCodeKey)
                                               .value(IsEqual.equalTo(illegalFailCode)));
    }

    @Test
    @DisplayName("passwd NotEmpty 실패")
    void when_passwd_is_empty_expected_bad_request() throws Exception {
        // given
        loginDTO.setPasswd("");
        // when
        final ResultActions actions = mockMvc.perform(
            MockMvcRequestBuilders.post(getUri("/members/login"))
                                  .contentType(JSON_UTF_8)
                                  .content(new ObjectMapper().writeValueAsString(loginDTO))
        ).andDo(MockMvcResultHandlers.print());
        // then
        actions.andExpect(MockMvcResultMatchers.status().isBadRequest())
               .andExpect(MockMvcResultMatchers.content().contentType(JSON_UTF_8))
               .andExpect(MockMvcResultMatchers.jsonPath(resultErrorCodeKey)
                                               .value(IsEqual.equalTo(illegalFailCode)));
    }

    @Test
    @DisplayName("존재하지 않은 memberId이므로 로그인 실패")
    void when_memberId_not_existed_expected_bad_request() throws Exception {
        // given
        loginDTO.setMemberId(memberDTO.getMemberId() + "test");
        // when
        final ResultActions actions = mockMvc.perform(
            MockMvcRequestBuilders.post(getUri("/members/login"))
                                  .contentType(JSON_UTF_8)
                                  .content(new ObjectMapper().writeValueAsString(loginDTO))
        ).andDo(MockMvcResultHandlers.print());
        // then
        actions.andExpect(MockMvcResultMatchers.status().isBadRequest())
               .andExpect(MockMvcResultMatchers.content().contentType(JSON_UTF_8))
               .andExpect(MockMvcResultMatchers.jsonPath(resultErrorCodeKey)
                                               .value(IsEqual.equalTo(deniedFailCode)));
    }

    @Test
    @DisplayName("passwd 틀려서 로그인 실패")
    void when_wrong_passwd_expected_bad_request() throws Exception {
        // given
        loginDTO.setPasswd(memberDTO.getPasswd() + "123444");
        // when
        final ResultActions actions = mockMvc.perform(
            MockMvcRequestBuilders.post(getUri("/members/login"))
                                  .contentType(JSON_UTF_8)
                                  .content(new ObjectMapper().writeValueAsString(loginDTO))
        ).andDo(MockMvcResultHandlers.print());
        // then
        actions.andExpect(MockMvcResultMatchers.status().isBadRequest())
               .andExpect(MockMvcResultMatchers.content().contentType(JSON_UTF_8))
               .andExpect(MockMvcResultMatchers.jsonPath(resultErrorCodeKey)
                                               .value(IsEqual.equalTo(deniedFailCode)));
    }

    @Test
    @DisplayName("로그인 성공")
    void when_correct_member_login_expected_okt() throws Exception {
        String resultDataCodeKey = "$.data.nickname";
        // given
        // when
        final ResultActions actions = mockMvc.perform(
            MockMvcRequestBuilders.post(getUri("/members/login"))
                                  .contentType(JSON_UTF_8)
                                  .content(new ObjectMapper().writeValueAsString(loginDTO))
        ).andDo(MockMvcResultHandlers.print());
        // then
        actions.andExpect(MockMvcResultMatchers.status().isOk())
               .andExpect(MockMvcResultMatchers.content().contentType(JSON_UTF_8))
               .andExpect(MockMvcResultMatchers.jsonPath(resultDataCodeKey)
                                               .value(IsEqual.equalTo(memberDTO.getNickname())));
    }

    @Test
    @WithMockUser
    @DisplayName("로그아웃 성공")
    void when_logout_expected_okt() throws Exception {
        // given
        // when
        final ResultActions actions = mockMvc.perform(
            MockMvcRequestBuilders.post("/members/logout")
                                  .contentType(JSON_UTF_8)
        ).andDo(MockMvcResultHandlers.print());
        // then
        actions.andExpect(MockMvcResultMatchers.status().isOk());
    }

}