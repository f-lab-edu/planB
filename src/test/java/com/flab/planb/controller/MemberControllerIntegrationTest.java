package com.flab.planb.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flab.planb.config.DBConfig;
import com.flab.planb.config.PushBatchConfig;
import com.flab.planb.config.RootConfig;
import com.flab.planb.config.SecurityConfig;
import com.flab.planb.config.ServletConfig;
import com.flab.planb.dto.member.Member;
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
        SecurityConfig.class,
        PushBatchConfig.class
    }
)
@PropertySource(
    {
        "file:src/main/resources/properties/*.properties",
        "file:src/main/resources/messages/*.properties",
        "file:src/main/resources/logback-dev.xml"
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
    private Member member;

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
        member = Member.builder()
                       .memberId("memberTest").nickname("멤버테스트")
                       .passwd("test1234").tel("01012345678")
                       .build();
        memberService.saveMemberInfo(member);
    }

    @Test
    @DisplayName("사용 중인 MemberId 확인 - 사용 중으로 실패")
    void test_existence_memberId() throws Exception {
        // given
        member = Member.builder()
                       .memberId("memberTest").nickname("멤버테스트")
                       .passwd("test1234").tel("01012345678")
                       .build();
        // when
        final ResultActions actions = mockMvc.perform(
            MockMvcRequestBuilders.get(
                getUri("/members/member-id?check=" + member.getMemberId())
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
    void test_checkNickName_noneFalse() throws Exception {
        // given
        member = Member.builder()
                       .memberId("memberTest2").nickname("멤버테스트")
                       .passwd("test1234").tel("01012345678")
                       .build();
        // when
        final ResultActions actions = mockMvc.perform(
            MockMvcRequestBuilders.get(
                getUri("/members/member-id?check=" + member.getMemberId())
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
    void test_existence_nickname() throws Exception {
        // given
        member = Member.builder()
                       .memberId("memberTest2").nickname("멤버테스트")
                       .passwd("test1234").tel("01012345678")
                       .build();
        // when
        final ResultActions actions = mockMvc.perform(
            MockMvcRequestBuilders.get(
                getUri("/members/nickname?check=" + member.getNickname())
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
    void test_not_existence_nickname() throws Exception {
        // given
        member = Member.builder()
                       .memberId("memberTest2").nickname("멤버테스트2")
                       .passwd("test1234").tel("01012345678")
                       .build();
        // when
        final ResultActions actions = mockMvc.perform(
            MockMvcRequestBuilders.get(
                getUri("/members/nickname?check=" + member.getNickname())
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
    void test_memberID_notBlank() throws Exception {
        // given
        member = Member.builder()
                       .memberId("").nickname("멤버테스트2")
                       .passwd("test1234").tel("01012345678")
                       .build();
        // when
        final ResultActions actions = mockMvc.perform(
            MockMvcRequestBuilders.post(getUri("/members"))
                                  .contentType(JSON_UTF_8)
                                  .content(new ObjectMapper().writeValueAsString(member))
        ).andDo(MockMvcResultHandlers.print());
        // then
        actions.andExpect(MockMvcResultMatchers.status().isBadRequest())
               .andExpect(MockMvcResultMatchers.content().contentType(JSON_UTF_8))
               .andExpect(MockMvcResultMatchers.jsonPath(resultErrorCodeKey)
                                               .value(IsEqual.equalTo(argumentNotValidCode)));
    }

    @Test
    @DisplayName("passwd NotBlank 실패")
    void test_passwd_notBlank() throws Exception {
        // given
        member = Member.builder()
                       .memberId("memberTest2").nickname("멤버테스트2")
                       .passwd("").tel("01012345678")
                       .build();
        // when
        final ResultActions actions = mockMvc.perform(
            MockMvcRequestBuilders.post(getUri("/members"))
                                  .contentType(JSON_UTF_8)
                                  .content(new ObjectMapper().writeValueAsString(member))
        ).andDo(MockMvcResultHandlers.print());
        // then
        actions.andExpect(MockMvcResultMatchers.status().isBadRequest())
               .andExpect(MockMvcResultMatchers.content().contentType(JSON_UTF_8))
               .andExpect(MockMvcResultMatchers.jsonPath(resultErrorCodeKey)
                                               .value(IsEqual.equalTo(argumentNotValidCode)));
    }

    @Test
    @DisplayName("nickname NotBlank 실패")
    void test_nickname_notBlank() throws Exception {
        // given
        member = Member.builder()
                       .memberId("memberTest2").nickname("")
                       .passwd("test1234").tel("01012345678")
                       .build();
        // when
        final ResultActions actions = mockMvc.perform(
            MockMvcRequestBuilders.post(getUri("/members"))
                                  .contentType(JSON_UTF_8)
                                  .content(new ObjectMapper().writeValueAsString(member))
        ).andDo(MockMvcResultHandlers.print());
        // then
        actions.andExpect(MockMvcResultMatchers.status().isBadRequest())
               .andExpect(MockMvcResultMatchers.content().contentType(JSON_UTF_8))
               .andExpect(MockMvcResultMatchers.jsonPath(resultErrorCodeKey)
                                               .value(IsEqual.equalTo(argumentNotValidCode)));
    }

    @Test
    @DisplayName("tel NotBlank 실패")
    void test_tel_notBlank() throws Exception {
        // given
        member = Member.builder()
                       .memberId("memberTest2").nickname("멤버테스트2")
                       .passwd("test1234").tel("")
                       .build();
        // when
        final ResultActions actions = mockMvc.perform(
            MockMvcRequestBuilders.post(getUri("/members"))
                                  .contentType(JSON_UTF_8)
                                  .content(new ObjectMapper().writeValueAsString(member))
        ).andDo(MockMvcResultHandlers.print());
        // then
        actions.andExpect(MockMvcResultMatchers.status().isBadRequest())
               .andExpect(MockMvcResultMatchers.content().contentType(JSON_UTF_8))
               .andExpect(MockMvcResultMatchers.jsonPath(resultErrorCodeKey)
                                               .value(IsEqual.equalTo(argumentNotValidCode)));
    }

    @Test
    @DisplayName("회원가입 실패")
    void test_signup_failed() throws Exception {
        // given
        member = Member.builder()
                       .memberId("memberTest").nickname("멤버테스트")
                       .passwd("test1234").tel("01012345678")
                       .build();
        // when
        final ResultActions actions = mockMvc.perform(
            MockMvcRequestBuilders.post(getUri("/members"))
                                  .contentType(JSON_UTF_8)
                                  .content(new ObjectMapper().writeValueAsString(member))
        ).andDo(MockMvcResultHandlers.print());
        // then
        actions.andExpect(MockMvcResultMatchers.status().isBadRequest())
               .andExpect(MockMvcResultMatchers.content().contentType(JSON_UTF_8))
               .andExpect(MockMvcResultMatchers.jsonPath(resultErrorCodeKey)
                                               .value(IsEqual.equalTo("INSERT_FAIL_001")));
    }

    @Test
    @DisplayName("회원가입 성공")
    void test_signup_succeed() throws Exception {
        // given
        member = Member.builder()
                       .memberId("memberTest2").nickname("멤버테스트2")
                       .passwd("test1234").tel("01012345678")
                       .build();
        // when
        final ResultActions actions = mockMvc.perform(
            MockMvcRequestBuilders.post(getUri("/members"))
                                  .contentType(JSON_UTF_8)
                                  .content(new ObjectMapper().writeValueAsString(member))
        ).andDo(MockMvcResultHandlers.print());
        // then
        actions.andExpect(MockMvcResultMatchers.status().isOk())
               .andExpect(MockMvcResultMatchers.content().contentType(JSON_UTF_8))
               .andExpect(MockMvcResultMatchers.jsonPath(resultMessageKey)
                                               .value(IsEqual.equalTo("등록하였습니다.")));
    }

}