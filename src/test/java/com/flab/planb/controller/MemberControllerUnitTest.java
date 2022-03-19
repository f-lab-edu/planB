package com.flab.planb.controller;

import ch.qos.logback.core.joran.spi.JoranException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flab.planb.TestUtils;
import com.flab.planb.controller.member.MemberController;
import com.flab.planb.response.message.MessageLookup;
import com.flab.planb.exception.ExceptionAdvice;
import com.flab.planb.response.ResponseEntityBuilder;
import com.flab.planb.dto.member.Member;
import com.flab.planb.service.member.MemberService;
import java.io.FileNotFoundException;
import org.hamcrest.core.IsEqual;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.util.UriComponentsBuilder;
import java.net.URI;
import java.nio.charset.StandardCharsets;

@PropertySource("file:src/main/resources/logback-dev.xml")
public class MemberControllerUnitTest {

    private final String URI = "/members";
    private static final ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
    private final MessageLookup messageLookup = Mockito.spy(new MessageLookup(messageSource));
    private final ResponseEntityBuilder responseEntityBuilder = Mockito.spy(new ResponseEntityBuilder(messageLookup));
    private final String existenceValidCode = "VALID_FAIL_002";
    private final String argumentNotValidCode = "VALID_FAIL_001";
    @Mock
    private MemberService memberService;
    @InjectMocks
    private MemberController memberController;
    @InjectMocks
    private ExceptionAdvice exceptionAdvice;
    private MockMvc mockMvc;
    private Member member;

    private URI getUri(String uri) {
        return UriComponentsBuilder.fromUriString(uri).build().encode().toUri();
    }

    @BeforeAll
    static void staticSetUp() throws JoranException, FileNotFoundException {
        messageSource.setBasename("file:src/main/resources/messages/message");
        messageSource.setDefaultEncoding(MessageLookup.ENCODIG);
        messageSource.setCacheSeconds(60);
        messageSource.setUseCodeAsDefaultMessage(true);
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(memberController).setControllerAdvice(exceptionAdvice)
                                 .addFilter(new CharacterEncodingFilter(StandardCharsets.UTF_8.toString(), true))
                                 .build();
        member = Member.builder().memberId("memberTest").nickname("멤버테스트").passwd("test1234").tel("01012345678")
                       .build();
    }

    @Test
    @DisplayName("사용 중인 MemberId 확인 - 사용 중으로 실패")
    void test_existence_memberId() throws Exception {
        // given
        Mockito.when(memberService.countByMemberId(ArgumentMatchers.anyString())).thenReturn(1);
        // when
        ResultActions actions = TestUtils.requestGet(mockMvc, URI + "/member-id?check=" + member.getMemberId(), null);
        // then
        TestUtils.expectBadRequest(actions, existenceValidCode);
    }

    @Test
    @DisplayName("사용 중인 MemberId 확인 - 미사용으로 성공")
    void test_not_existence_memberId() throws Exception {
        // given
        Mockito.when(memberService.countByMemberId(ArgumentMatchers.anyString())).thenReturn(0);
        // when
        ResultActions actions = TestUtils.requestGet(mockMvc, URI + "/member-id?check=" + member.getMemberId(), null);
        // then
        TestUtils.expectOk(actions, "인증에 성공하였습니다.");
    }

    @Test
    @DisplayName("사용 중인 Nickname 확인 - 사용 중으로 실패")
    void test_existence_nickname() throws Exception {
        // given
        Mockito.when(memberService.countByNickName(ArgumentMatchers.anyString())).thenReturn(1);
        // when
        ResultActions actions = TestUtils.requestGet(mockMvc, URI + "/nickname?check=" + member.getNickname(), null);
        // then
        TestUtils.expectBadRequest(actions, existenceValidCode);
    }

    @Test
    @DisplayName("사용 중인 Nickname 확인 - 미사용으로 성공")
    void test_not_existence_nickname() throws Exception {
        // given
        Mockito.when(memberService.countByNickName(ArgumentMatchers.anyString())).thenReturn(0);
        // when
        ResultActions actions = TestUtils.requestGet(mockMvc, URI + "/nickname?check=" + member.getNickname(), null);
        // then
        TestUtils.expectOk(actions, "인증에 성공하였습니다.");
    }

    @Test
    @DisplayName("회원가입 실패")
    void test_signup_failed() throws Exception {
        // given
        Mockito.when(memberService.countByMemberId(ArgumentMatchers.anyString())).thenReturn(1);
        Mockito.when(memberService.countByNickName(ArgumentMatchers.anyString())).thenReturn(1);
        // when
        ResultActions actions = TestUtils.requestPost(mockMvc, URI, member);
        // then
        TestUtils.expectBadRequest(actions, "INSERT_FAIL_001");
    }

    @Test
    @DisplayName("회원가입 성공")
    void test_signup_succeed() throws Exception {
        // given
        Mockito.when(memberService.countByMemberId(ArgumentMatchers.anyString())).thenReturn(0);
        Mockito.when(memberService.countByNickName(ArgumentMatchers.anyString())).thenReturn(0);
        // when
        ResultActions actions = TestUtils.requestPost(mockMvc, URI, member);
        // then
        TestUtils.expectOk(actions, "등록하였습니다.");
    }

}