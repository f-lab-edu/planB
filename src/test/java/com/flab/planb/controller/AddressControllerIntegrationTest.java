package com.flab.planb.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flab.planb.config.DBConfig;
import com.flab.planb.config.RootConfig;
import com.flab.planb.config.SecurityConfig;
import com.flab.planb.config.ServletConfig;
import com.flab.planb.dto.member.AddressDTO;
import java.net.URI;
import java.nio.charset.StandardCharsets;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.util.UriComponentsBuilder;

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
        "file:src/main/resources/logback-dev.xml"
    }
)
public class AddressControllerIntegrationTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String VALID_FAIL_001 = "VALID_FAIL_001";
    @Autowired
    private WebApplicationContext webContext;
    private MockMvc mockMvc;
    private AddressDTO addressDTO;

    private URI getUri(String uri) {
        return UriComponentsBuilder.fromUriString(uri).build().encode().toUri();
    }

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webContext)
                                 .addFilter(new CharacterEncodingFilter(StandardCharsets.UTF_8.toString(), true))
                                 .build();
        addressDTO = AddressDTO.builder()
                               .memberId(1L)
                               .address("서울특별시 종로구 종로 1")
                               .zipCode("03154")
                               .build();
    }

    @Test
    @DisplayName("memberId Positive 실패")
    void when_memberId_not_positive_expected_bad_request() throws Exception {
        // given
        addressDTO.setMemberId(0L);
        // when
        final ResultActions actions = getResultActionsOfPostRequest();
        // then
        expectBadRequest(actions, VALID_FAIL_001);
    }

    @Test
    @DisplayName("zipCode NotBlank 실패")
    void when_zipCode_is_blank_expected_bad_request() throws Exception {
        // given
        addressDTO.setZipCode("");
        // when
        final ResultActions actions = getResultActionsOfPostRequest();
        // then
        expectBadRequest(actions, VALID_FAIL_001);
    }

    @Test
    @DisplayName("zipCode 숫자가 아닌 값이 있어서 실패")
    void when_zipCode_contains_non_numeric_value_expected_bad_request() throws Exception {
        // given
        addressDTO.setZipCode("01b11");
        // when
        final ResultActions actions = getResultActionsOfPostRequest();
        // then
        expectBadRequest(actions, VALID_FAIL_001);
    }

    @Test
    @DisplayName("zipCode 5자리 초과 실패")
    void when_zipCode_more_tan_five_digits_expected_bad_request() throws Exception {
        // given
        addressDTO.setZipCode("012345");
        // when
        final ResultActions actions = getResultActionsOfPostRequest();
        // then
        expectBadRequest(actions, VALID_FAIL_001);
    }

    @Test
    @DisplayName("address Positive 실패")
    void when_address_is_blank_expected_bad_request() throws Exception {
        // given
        addressDTO.setAddress("");
        // when
        final ResultActions actions = getResultActionsOfPostRequest();
        // then
        expectBadRequest(actions, VALID_FAIL_001);
    }

    @Test
    @DisplayName("존재하지 않는 회원 ID 실패")
    void when_id_from_members_is_not_existed_expected_bad_request() throws Exception {
        // given
        addressDTO.setMemberId(Long.MAX_VALUE);
        // when
        final ResultActions actions = getResultActionsOfPostRequest();
        // then
        expectBadRequest(actions, VALID_FAIL_001);
    }

    @Test
    @DisplayName("배달주소 등록 성공")
    void when_new_address_expected_ok() throws Exception {
        // given
        // when
        final ResultActions actions = getResultActionsOfPostRequest();
        // then
        expectOk(actions, "등록하였습니다.");
    }

    private ResultActions getResultActionsOfPostRequest() throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.post(getUri("/address"))
                                                     .contentType(MediaType.APPLICATION_JSON_UTF8)
                                                     .content(objectMapper.writeValueAsString(addressDTO)))
                      .andDo(MockMvcResultHandlers.print());
    }

    private void expectBadRequest(ResultActions actions, String errorCode) throws Exception {
        actions.andExpect(MockMvcResultMatchers.status().isBadRequest())
               .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
               .andExpect(MockMvcResultMatchers.jsonPath("$.data.errorCode").value(IsEqual.equalTo(errorCode)));
    }

    private void expectOk(ResultActions actions, String statusMessage) throws Exception {
        actions.andExpect(MockMvcResultMatchers.status().isOk())
               .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
               .andExpect(MockMvcResultMatchers.jsonPath("$.statusMessage").value(IsEqual.equalTo(statusMessage)));
    }

}