package com.flab.planb.controller;

import ch.qos.logback.core.joran.spi.JoranException;
import com.flab.planb.TestUtils;
import com.flab.planb.controller.member.MemberController;
import com.flab.planb.dto.member.Member;
import com.flab.planb.exception.ExceptionAdvice;
import com.flab.planb.response.ResponseEntityBuilder;
import com.flab.planb.response.message.MessageLookup;
import com.flab.planb.service.member.MemberService;
import java.io.FileNotFoundException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.util.UriComponentsBuilder;

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
        member = Member.builder().memberId("memberTest").nickname("???????????????").passwd("test1234").tel("01012345678")
                       .build();
    }

    @Test
    @DisplayName("?????? ?????? MemberId ?????? - ?????? ????????? ??????")
    void test_existence_memberId() throws Exception {
        // given
        Mockito.when(memberService.isNotUsedMemberId(ArgumentMatchers.anyString())).thenReturn(false);
        // when
        ResultActions actions = TestUtils.requestGet(mockMvc, URI + "/member-id?check=" + member.getMemberId(), null);
        // then
        TestUtils.expectBadRequest(actions, existenceValidCode);
    }

    @Test
    @DisplayName("?????? ?????? MemberId ?????? - ??????????????? ??????")
    void test_not_existence_memberId() throws Exception {
        // given
        Mockito.when(memberService.isNotUsedMemberId(ArgumentMatchers.anyString())).thenReturn(true);
        // when
        ResultActions actions = TestUtils.requestGet(mockMvc, URI + "/member-id?check=" + member.getMemberId(), null);
        // then
        TestUtils.expectOk(actions, "????????? ?????????????????????.");
    }

    @Test
    @DisplayName("?????? ?????? Nickname ?????? - ?????? ????????? ??????")
    void test_existence_nickname() throws Exception {
        // given
        Mockito.when(memberService.isNotUsedNicname(ArgumentMatchers.anyString())).thenReturn(false);
        // when
        ResultActions actions = TestUtils.requestGet(mockMvc, URI + "/nickname?check=" + member.getNickname(), null);
        // then
        TestUtils.expectBadRequest(actions, existenceValidCode);
    }

    @Test
    @DisplayName("?????? ?????? Nickname ?????? - ??????????????? ??????")
    void test_not_existence_nickname() throws Exception {
        // given
        Mockito.when(memberService.isNotUsedNicname(ArgumentMatchers.anyString())).thenReturn(true);
        // when
        ResultActions actions = TestUtils.requestGet(mockMvc, URI + "/nickname?check=" + member.getNickname(), null);
        // then
        TestUtils.expectOk(actions, "????????? ?????????????????????.");
    }

    @Test
    @DisplayName("???????????? ??????")
    void test_signup_failed() throws Exception {
        // given
        Mockito.when(memberService.isNotUsedMemberId(ArgumentMatchers.anyString())).thenReturn(false);
        Mockito.when(memberService.isNotUsedNicname(ArgumentMatchers.anyString())).thenReturn(false);
        // when
        ResultActions actions = TestUtils.requestPost(mockMvc, URI, member);
        // then
        TestUtils.expectBadRequest(actions, "INSERT_FAIL_001");
    }

    @Test
    @DisplayName("???????????? ??????")
    void test_signup_succeed() throws Exception {
        // given
        Mockito.when(memberService.isNotUsedMemberId(ArgumentMatchers.anyString())).thenReturn(true);
        Mockito.when(memberService.isNotUsedNicname(ArgumentMatchers.anyString())).thenReturn(true);
        // when
        ResultActions actions = TestUtils.requestPost(mockMvc, URI, member);
        // then
        TestUtils.expectOk(actions, "?????????????????????.");
    }

}