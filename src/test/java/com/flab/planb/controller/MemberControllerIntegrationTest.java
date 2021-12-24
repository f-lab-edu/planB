package com.flab.planb.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flab.planb.config.DBConfig;
import com.flab.planb.config.RootConfig;
import com.flab.planb.config.SecurityConfig;
import com.flab.planb.config.ServletConfig;
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
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
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
public class MemberControllerIntegrationTest {

    private static final MediaType JSON_UTF_8 = new MediaType(
        MediaType.APPLICATION_JSON.getType(),
        MediaType.APPLICATION_JSON.getSubtype(),
        StandardCharsets.UTF_8
    );
    private final String resultErrorCodeKey = "$.data.errorCode";
    private final String resultMessageKey = "$.statusMessage";
    private final String existenceValidCode = "VALID_FAIL_002";
    private final String argumentNotValidCode = "VALID_FAIL_001";
    @Autowired
    private WebApplicationContext webContext;
    @Autowired
    private MemberService memberService;
    private MockMvc mockMvc;
    private MemberDTO memberDTO;

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
                                 .build();
        memberDTO = MemberDTO.builder()
                             .memberId("memberTest").nickname("멤버테스트")
                             .passwd("test1234").tel("01012345678")
                             .build();
        memberService.saveMemberInfo(memberDTO);
    }

    @Test
    @DisplayName("사용 중인 MemberId 확인 - 사용 중으로 실패")
    void when_memberId_is_existed_expected_bad_request() throws Exception {
        // given
        memberDTO.setMemberId("memberTest");
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
        memberDTO.setMemberId("memberTest2");
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
        memberDTO.setNickname("멤버테스트");
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
        memberDTO.setNickname("멤버테스트2");
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
        memberDTO = MemberDTO.builder()
                             .memberId("memberTest").nickname("멤버테스트")
                             .passwd("test1234").tel("01012345678")
                             .build();
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
        memberDTO = MemberDTO.builder()
                             .memberId("memberTest2").nickname("멤버테스트2")
                             .passwd("test1234").tel("01012345678")
                             .build();
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