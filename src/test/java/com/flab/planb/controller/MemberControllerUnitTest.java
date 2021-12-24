package com.flab.planb.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flab.planb.common.MessageLookup;
import com.flab.planb.common.ExceptionAdvice;
import com.flab.planb.dto.member.MemberDTO;
import com.flab.planb.service.MemberService;
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
import org.springframework.security.crypto.password.PasswordEncoder;
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

@PropertySource("file:src/main/resources/log4j2.xml")
public class MemberControllerUnitTest {

    private static final MediaType JSON_UTF_8 = new MediaType(
        MediaType.APPLICATION_JSON.getType(),
        MediaType.APPLICATION_JSON.getSubtype(),
        StandardCharsets.UTF_8
    );
    private static final ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
    private final MessageLookup messageLookup = Mockito.spy(new MessageLookup(messageSource));
    private final String resultErrorCodeKey = "$.data.errorCode";
    private final String resultMessageKey = "$.statusMessage";
    private final String existenceValidCode = "VALID_FAIL_002";
    private final String argumentNotValidCode = "VALID_FAIL_001";
    @Mock
    private MemberService memberService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private MemberController memberController;
    @InjectMocks
    private ExceptionAdvice exceptionAdvice;
    private MockMvc mockMvc;
    private MemberDTO memberDTO;

    private URI getUri(String uri) {
        return UriComponentsBuilder.fromUriString(uri).build().encode().toUri();
    }

    @BeforeAll
    static void staticSetUp() {
        messageSource.setBasename("file:src/main/resources/messages/message");
        messageSource.setDefaultEncoding(MessageLookup.ENCODIG);
        messageSource.setCacheSeconds(60);
        messageSource.setUseCodeAsDefaultMessage(true);
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(memberController)
                                 .setControllerAdvice(exceptionAdvice)
                                 .addFilter(
                                     new CharacterEncodingFilter(
                                         StandardCharsets.UTF_8.toString(),
                                         true
                                     )
                                 ).build();
        memberDTO = MemberDTO.builder()
                             .memberId("memberTest").nickname("멤버테스트")
                             .passwd("test1234").tel("01012345678")
                             .build();
    }

    @Test
    @DisplayName("사용 중인 MemberId 확인 - 사용 중으로 실패")
    void when_memberId_is_existed_expected_bad_request() throws Exception {
        // given
        Mockito.when(memberService.countByMemberId(ArgumentMatchers.anyString())).thenReturn(1);
        // when
        final ResultActions actions = mockMvc.perform(
            MockMvcRequestBuilders.get(
                getUri("/members/member-id?check=" + memberDTO.getMemberId())
            ).contentType(JSON_UTF_8)
        ).andDo(MockMvcResultHandlers.print());
        // then
        actions.andExpect(MockMvcResultMatchers.status().isBadRequest())
               .andExpect(MockMvcResultMatchers.content().contentType(JSON_UTF_8))
               .andExpect(MockMvcResultMatchers.jsonPath(resultErrorCodeKey)
                                               .value(IsEqual.equalTo(existenceValidCode)));
    }

    @Test
    @DisplayName("사용 중인 MemberId 확인 - 미사용으로 성공")
    void when_memberId_is_not_existed_expected_ok() throws Exception {
        // given
        Mockito.when(memberService.countByMemberId(ArgumentMatchers.anyString())).thenReturn(0);
        // when
        final ResultActions actions = mockMvc.perform(
            MockMvcRequestBuilders.get(
                getUri("/members/member-id?check=" + memberDTO.getMemberId())
            ).contentType(JSON_UTF_8)
        ).andDo(MockMvcResultHandlers.print());
        // then
        actions.andExpect(MockMvcResultMatchers.status().isOk())
               .andExpect(MockMvcResultMatchers.content().contentType(JSON_UTF_8))
               .andExpect(MockMvcResultMatchers.jsonPath(resultMessageKey)
                                               .value(IsEqual.equalTo("인증에 성공하였습니다.")));
    }

    @Test
    @DisplayName("사용 중인 Nickname 확인 - 사용 중으로 실패")
    void when_nickname_is_existed_expected_bad_request() throws Exception {
        // given
        Mockito.when(memberService.countByNickName(ArgumentMatchers.anyString())).thenReturn(1);
        // when
        final ResultActions actions = mockMvc.perform(
            MockMvcRequestBuilders.get(
                getUri("/members/nickname?check=" + memberDTO.getNickname())
            ).contentType(JSON_UTF_8)
        ).andDo(MockMvcResultHandlers.print());
        // then
        actions.andExpect(MockMvcResultMatchers.status().isBadRequest())
               .andExpect(MockMvcResultMatchers.content().contentType(JSON_UTF_8))
               .andExpect(MockMvcResultMatchers.jsonPath(resultErrorCodeKey)
                                               .value(IsEqual.equalTo(existenceValidCode)));
    }

    @Test
    @DisplayName("사용 중인 Nickname 확인 - 미사용으로 성공")
    void when_nickname_is_not_existed_expected_ok() throws Exception {
        // given
        Mockito.when(memberService.countByNickName(ArgumentMatchers.anyString())).thenReturn(0);
        // when
        final ResultActions actions = mockMvc.perform(
            MockMvcRequestBuilders.get(
                getUri("/members/nickname?check=" + memberDTO.getNickname())
            ).contentType(JSON_UTF_8)
        ).andDo(MockMvcResultHandlers.print());
        // then
        actions.andExpect(MockMvcResultMatchers.status().isOk())
               .andExpect(MockMvcResultMatchers.content().contentType(JSON_UTF_8))
               .andExpect(MockMvcResultMatchers.jsonPath(resultMessageKey)
                                               .value(IsEqual.equalTo("인증에 성공하였습니다.")));
    }

    @Test
    @DisplayName("memberId NotBlank 실패")
    void when_memberid_is_blank_expected_bad_request() throws Exception {
        // given
        memberDTO.setMemberId("");
        // when
        final ResultActions actions = mockMvc.perform(
            MockMvcRequestBuilders.post(getUri("/members"))
                                  .contentType(JSON_UTF_8)
                                  .content(new ObjectMapper().writeValueAsString(memberDTO))
        ).andDo(MockMvcResultHandlers.print());
        // then
        actions.andExpect(MockMvcResultMatchers.status().isBadRequest())
               .andExpect(MockMvcResultMatchers.content().contentType(JSON_UTF_8))
               .andExpect(MockMvcResultMatchers.jsonPath(resultErrorCodeKey)
                                               .value(IsEqual.equalTo(argumentNotValidCode)));
    }

    @Test
    @DisplayName("passwd NotBlank 실패")
    void when_passwwd_is_blank_expected_bad_request() throws Exception {
        // given
        memberDTO.setPasswd("");
        // when
        final ResultActions actions = mockMvc.perform(
            MockMvcRequestBuilders.post(getUri("/members"))
                                  .contentType(JSON_UTF_8)
                                  .content(new ObjectMapper().writeValueAsString(memberDTO))
        ).andDo(MockMvcResultHandlers.print());
        // then
        actions.andExpect(MockMvcResultMatchers.status().isBadRequest())
               .andExpect(MockMvcResultMatchers.content().contentType(JSON_UTF_8))
               .andExpect(MockMvcResultMatchers.jsonPath(resultErrorCodeKey)
                                               .value(IsEqual.equalTo(argumentNotValidCode)));
    }

    @Test
    @DisplayName("nickname NotBlank 실패")
    void when_nickname_is_blank_expected_bad_request() throws Exception {
        // given
        memberDTO.setNickname("");
        // when
        final ResultActions actions = mockMvc.perform(
            MockMvcRequestBuilders.post(getUri("/members"))
                                  .contentType(JSON_UTF_8)
                                  .content(new ObjectMapper().writeValueAsString(memberDTO))
        ).andDo(MockMvcResultHandlers.print());
        // then
        actions.andExpect(MockMvcResultMatchers.status().isBadRequest())
               .andExpect(MockMvcResultMatchers.content().contentType(JSON_UTF_8))
               .andExpect(MockMvcResultMatchers.jsonPath(resultErrorCodeKey)
                                               .value(IsEqual.equalTo(argumentNotValidCode)));
    }

    @Test
    @DisplayName("tel NotBlank 실패")
    void when_tel_is_blank_expected_bad_request() throws Exception {
        // given
        memberDTO.setTel("");
        // when
        final ResultActions actions = mockMvc.perform(
            MockMvcRequestBuilders.post(getUri("/members"))
                                  .contentType(JSON_UTF_8)
                                  .content(new ObjectMapper().writeValueAsString(memberDTO))
        ).andDo(MockMvcResultHandlers.print());
        // then
        actions.andExpect(MockMvcResultMatchers.status().isBadRequest())
               .andExpect(MockMvcResultMatchers.content().contentType(JSON_UTF_8))
               .andExpect(MockMvcResultMatchers.jsonPath(resultErrorCodeKey)
                                               .value(IsEqual.equalTo(argumentNotValidCode)));
    }

    @Test
    @DisplayName("회원가입 실패")
    void when_duplicated_signup_expected_bad_request() throws Exception {
        // given
        Mockito.when(memberService.countByMemberId(ArgumentMatchers.anyString())).thenReturn(1);
        Mockito.when(memberService.countByNickName(ArgumentMatchers.anyString())).thenReturn(1);
        // when
        final ResultActions actions = mockMvc.perform(
            MockMvcRequestBuilders.post(getUri("/members"))
                                  .contentType(JSON_UTF_8)
                                  .content(new ObjectMapper().writeValueAsString(memberDTO))
        ).andDo(MockMvcResultHandlers.print());
        // then
        actions.andExpect(MockMvcResultMatchers.status().isBadRequest())
               .andExpect(MockMvcResultMatchers.content().contentType(JSON_UTF_8))
               .andExpect(MockMvcResultMatchers.jsonPath(resultErrorCodeKey)
                                               .value(IsEqual.equalTo("INSERT_FAIL_001")));
    }

    @Test
    @DisplayName("회원가입 성공")
    void when_new_signup_expected_ok() throws Exception {
        // given
        Mockito.when(memberService.countByMemberId(ArgumentMatchers.anyString())).thenReturn(0);
        Mockito.when(memberService.countByNickName(ArgumentMatchers.anyString())).thenReturn(0);
        // when
        final ResultActions actions = mockMvc.perform(
            MockMvcRequestBuilders.post(getUri("/members"))
                                  .contentType(JSON_UTF_8)
                                  .content(new ObjectMapper().writeValueAsString(memberDTO))
        ).andDo(MockMvcResultHandlers.print());
        // then
        actions.andExpect(MockMvcResultMatchers.status().isOk())
               .andExpect(MockMvcResultMatchers.content().contentType(JSON_UTF_8))
               .andExpect(MockMvcResultMatchers.jsonPath(resultMessageKey)
                                               .value(IsEqual.equalTo("등록하였습니다.")));
    }

}