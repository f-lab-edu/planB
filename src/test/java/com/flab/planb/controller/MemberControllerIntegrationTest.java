package com.flab.planb.controller;

import com.flab.planb.TestUtils;
import com.flab.planb.config.DBConfig;
import com.flab.planb.config.PushBatchConfig;
import com.flab.planb.config.RootConfig;
import com.flab.planb.config.SecurityConfig;
import com.flab.planb.config.ServletConfig;
import com.flab.planb.dto.member.Member;
import com.flab.planb.service.member.MemberService;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

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
public class MemberControllerIntegrationTest {

    private final String URI = "/members";
    private final String existenceValidCode = "VALID_FAIL_002";
    private final String argumentNotValidCode = "VALID_FAIL_001";
    @Autowired
    private WebApplicationContext webContext;
    @Autowired
    private MemberService memberService;
    private MockMvc mockMvc;
    private Member member;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webContext)
                                 .addFilter(new CharacterEncodingFilter(StandardCharsets.UTF_8.toString(), true))
                                 .build();
        member = Member.builder()
                       .memberId("memberTest").nickname("멤버테스트")
                       .passwd("test1234").tel("01012345678")
                       .build();
        memberService.saveMemberInfo(member);
    }

    @Test
    @DisplayName("사용 중인 MemberId 확인 - 사용 중으로 실패")
    void when_memberId_is_existed_expected_bad_request() throws Exception {
        // given
        member = Member.builder()
                       .memberId("memberTest").nickname("멤버테스트")
                       .passwd("test1234").tel("01012345678")
                       .build();
        // when
        ResultActions actions = TestUtils.requestGet(mockMvc,
                                                     URI + "/member-id?check=" + member.getMemberId(),
                                                     null);
        // then
        TestUtils.expectBadRequest(actions, existenceValidCode);
    }

    @Test
    @DisplayName("사용 중인 MemberId 확인 - 미사용으로 성공")
    void when_memberId_is_not_existed_expected_ok() throws Exception {
        // given
        member = Member.builder()
                       .memberId("memberTest2").nickname("멤버테스트")
                       .passwd("test1234").tel("01012345678")
                       .build();
        // when
        ResultActions actions = TestUtils.requestGet(mockMvc,
                                                     URI + "/member-id?check=" + member.getMemberId(),
                                                     null);
        // then
        TestUtils.expectOk(actions, "인증에 성공하였습니다.");
    }

    @Test
    @DisplayName("사용 중인 Nickname 확인 - 사용 중으로 실패")
    void when_nickname_is_existed_expected_bad_request() throws Exception {
        // given
        member = Member.builder()
                       .memberId("memberTest2").nickname("멤버테스트")
                       .passwd("test1234").tel("01012345678")
                       .build();
        // when
        ResultActions actions = TestUtils.requestGet(mockMvc,
                                                     URI + "/nickname?check=" + member.getNickname(),
                                                     null);
        // then
        TestUtils.expectBadRequest(actions, existenceValidCode);
    }

    @Test
    @DisplayName("사용 중인 Nickname 확인 - 미사용으로 성공")
    void when_nickname_is_not_existed_expected_ok() throws Exception {
        // given
        member = Member.builder()
                       .memberId("memberTest2").nickname("멤버테스트2")
                       .passwd("test1234").tel("01012345678")
                       .build();
        // when
        ResultActions actions = TestUtils.requestGet(mockMvc,
                                                     URI + "/nickname?check=" + member.getNickname(),
                                                     null);
        // then
        TestUtils.expectOk(actions, "인증에 성공하였습니다.");
    }

    @Test
    @DisplayName("memberId NotBlank 실패")
    void when_memberid_is_blank_expected_bad_request() throws Exception {
        // given
        member = Member.builder()
                       .memberId("").nickname("멤버테스트2")
                       .passwd("test1234").tel("01012345678")
                       .build();
        // when
        ResultActions actions = TestUtils.requestPost(mockMvc, URI, member);
        // then
        TestUtils.expectBadRequest(actions, argumentNotValidCode);
    }

    @Test
    @DisplayName("passwd NotBlank 실패")
    void when_passwwd_is_blank_expected_bad_request() throws Exception {
        // given
        member = Member.builder()
                       .memberId("memberTest2").nickname("멤버테스트2")
                       .passwd("").tel("01012345678")
                       .build();
        // when
        ResultActions actions = TestUtils.requestPost(mockMvc, URI, member);
        // then
        TestUtils.expectBadRequest(actions, argumentNotValidCode);
    }

    @Test
    @DisplayName("nickname NotBlank 실패")
    void when_nickname_is_blank_expected_bad_request() throws Exception {
        // given
        member = Member.builder()
                       .memberId("memberTest2").nickname("")
                       .passwd("test1234").tel("01012345678")
                       .build();
        // when
        ResultActions actions = TestUtils.requestPost(mockMvc, URI, member);
        // then
        TestUtils.expectBadRequest(actions, argumentNotValidCode);
    }

    @Test
    @DisplayName("tel NotBlank 실패")
    void when_tel_is_blank_expected_bad_request() throws Exception {
        // given
        member = Member.builder()
                       .memberId("memberTest2").nickname("멤버테스트2")
                       .passwd("test1234").tel("")
                       .build();
        // when
        ResultActions actions = TestUtils.requestPost(mockMvc, URI, member);
        // then
        TestUtils.expectBadRequest(actions, argumentNotValidCode);
    }

    @Test
    @DisplayName("회원가입 실패")
    void when_duplicated_signup_expected_bad_request() throws Exception {
        // given
        member = Member.builder()
                       .memberId("memberTest").nickname("멤버테스트")
                       .passwd("test1234").tel("01012345678")
                       .build();
        // when
        ResultActions actions = TestUtils.requestPost(mockMvc, URI, member);
        // then
        TestUtils.expectBadRequest(actions, "INSERT_FAIL_001");
    }

    @Test
    @DisplayName("회원가입 성공")
    void when_new_signup_expected_ok() throws Exception {
        // given
        member = Member.builder()
                       .memberId("memberTest2").nickname("멤버테스트2")
                       .passwd("test1234").tel("01012345678")
                       .build();
        // when
        ResultActions actions = TestUtils.requestPost(mockMvc, URI, member);
        // then
        TestUtils.expectOk(actions, "등록하였습니다.");
    }

}