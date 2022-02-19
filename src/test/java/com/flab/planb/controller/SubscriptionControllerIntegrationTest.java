package com.flab.planb.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.flab.planb.config.DBConfig;
import com.flab.planb.config.RootConfig;
import com.flab.planb.config.SecurityConfig;
import com.flab.planb.config.ServletConfig;
import com.flab.planb.dto.subscription.request.SubscriptionRequest;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
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
public class SubscriptionControllerIntegrationTest {

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    private final String VALID_FAIL_001 = "VALID_FAIL_001";
    @Autowired
    private WebApplicationContext webContext;
    private MockMvc mockMvc;
    private SubscriptionRequest request;

    private URI getUri(String uri) {
        return UriComponentsBuilder.fromUriString(uri).build().encode().toUri();
    }

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webContext)
                                 .addFilter(new CharacterEncodingFilter(StandardCharsets.UTF_8.toString(), true))
                                 .build();
    }

    @Test
    @DisplayName("shopId Positive 실패")
    void when_shopId_not_positive_expected_bad_request() throws Exception {
        // given
        request = new SubscriptionRequest(0L, 1L, 8L, 5, LocalTime.parse("23:00"),
                                          Map.of(1L, List.of(1L, 2L), 2L, Collections.emptyList()));
        // when
        ResultActions actions = getResultActionsOfPostRequest();
        // then
        expectBadRequest(actions, VALID_FAIL_001);
    }

    @Test
    @DisplayName("addressId Positive 실패")
    void when_addressId_not_positive_expected_bad_request() throws Exception {
        // given
        request = new SubscriptionRequest(111L, 1L, 0L, 5, LocalTime.parse("23:00"),
                                          Map.of(1L, List.of(1L, 2L), 2L, Collections.emptyList()));
        // when
        ResultActions actions = getResultActionsOfPostRequest();
        // then
        expectBadRequest(actions, VALID_FAIL_001);
    }

    @Test
    @DisplayName("subscriptionDay 0에서 6사이에 없어서 실패")
    void when_subscriptionDay_none_in_between_zero_to_six_expected_bad_request() throws Exception {
        // given
        request = new SubscriptionRequest(111L, 1L, 8L, -1, LocalTime.parse("23:00"),
                                          Map.of(1L, List.of(1L, 2L), 2L, Collections.emptyList()));
        // when
        ResultActions actions = getResultActionsOfPostRequest();
        // then
        expectBadRequest(actions, VALID_FAIL_001);

        // given
        request = new SubscriptionRequest(111L, 1L, 8L, 7, LocalTime.parse("23:00"),
                                          Map.of(1L, List.of(1L, 2L), 2L, Collections.emptyList()));
        // when
        actions = getResultActionsOfPostRequest();
        // then
        expectBadRequest(actions, VALID_FAIL_001);
    }

    @Test
    @DisplayName("subscriptionTime 시간 단위 실패")
    void when_subscriptionTime_time_failure_expected_bad_request() throws Exception {
        // given
        request = new SubscriptionRequest(111L, 1L, 8L, 5, LocalTime.parse("00:25"),
                                          Map.of(1L, List.of(1L, 2L), 2L, Collections.emptyList()));
        // when
        ResultActions actions = getResultActionsOfPostRequest();
        // then
        expectBadRequest(actions, VALID_FAIL_001);

        // given
        request = new SubscriptionRequest(111L, 1L, 8L, 5, LocalTime.parse("23:59"),
                                          Map.of(1L, List.of(1L, 2L), 2L, Collections.emptyList()));
        // when
        actions = getResultActionsOfPostRequest();
        // then
        expectBadRequest(actions, VALID_FAIL_001);
    }

    @Test
    @DisplayName("subscriptionMenus 없어서 실패")
    void when_subscriptionMenus_not_exist_expected_bad_request() throws Exception {
        // given
        request = new SubscriptionRequest(111L, Long.MAX_VALUE, 8L, 5, LocalTime.parse("23:00"),
                                          Collections.emptyMap());
        // when
        ResultActions actions = getResultActionsOfPostRequest();
        // then
        expectBadRequest(actions, VALID_FAIL_001);
    }

    @Test
    @DisplayName("존재하지 않는 가게 실패")
    void when_not_exist_shop_expected_bad_request() throws Exception {
        // given
        request = new SubscriptionRequest(111L, Long.MAX_VALUE, 8L, 5, LocalTime.parse("23:00"),
                                          Map.of(1L, List.of(1L, 2L), 2L, Collections.emptyList()));
        // when
        ResultActions actions = getResultActionsOfPostRequest();
        // then
        expectBadRequest(actions, VALID_FAIL_001);
    }

    @Test
    @DisplayName("휴일 구독으로 실패")
    void when_subscription_dayoff_expected_bad_request() throws Exception {
        // given
        request = new SubscriptionRequest(111L, 1L, 8L, 5, LocalTime.parse("23:00"),
                                          Map.of(1L, List.of(1L, 2L), 2L, Collections.emptyList()));
        // when
        ResultActions actions = getResultActionsOfPostRequest();
        // then
        expectBadRequest(actions, VALID_FAIL_001);
    }

    @Test
    @DisplayName("운영하지 않는 시간 구독 실패")
    void when_subscription_non_operationg_hours_expected_bad_request() throws Exception {
        // given
        request = new SubscriptionRequest(111L, 1L, 8L, 2, LocalTime.parse("23:00"),
                                          Map.of(1L, List.of(1L, 2L), 2L, Collections.emptyList()));
        // when
        ResultActions actions = getResultActionsOfPostRequest();
        // then
        expectBadRequest(actions, VALID_FAIL_001);
    }

    @Test
    @DisplayName("중복 구독 실패")
    void when_duplicate_subscription_expected_bad_request() throws Exception {
        // given
        request = new SubscriptionRequest(111L, 1L, 8L, 1, LocalTime.parse("12:00"),
                                          Map.of(1L, List.of(1L, 2L), 2L, Collections.emptyList()));
        // when
        ResultActions actions = getResultActionsOfPostRequest();
        // then
        expectBadRequest(actions, VALID_FAIL_001);
    }

    @Test
    @DisplayName("구독 등록 성공")
    void when_correct_subscription_expected_ok() throws Exception {
        // given
        request = new SubscriptionRequest(111L, 1L, 8L, 2, LocalTime.parse("12:00"),
                                          Map.of(1L, List.of(1L, 2L), 2L, Collections.emptyList()));
        // when
        ResultActions actions = getResultActionsOfPostRequest();
        // then
        expectOk(actions, "등록하였습니다.");
    }

    private ResultActions getResultActionsOfPostRequest() throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.post(getUri("/subscription"))
                                                     .contentType(MediaType.APPLICATION_JSON_UTF8)
                                                     .content(objectMapper.writeValueAsString(request)))
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