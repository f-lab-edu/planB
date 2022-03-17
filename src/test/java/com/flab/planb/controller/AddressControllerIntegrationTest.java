package com.flab.planb.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flab.planb.config.DBConfig;
import com.flab.planb.config.PushBatchConfig;
import com.flab.planb.config.RootConfig;
import com.flab.planb.config.SecurityConfig;
import com.flab.planb.config.ServletConfig;
import com.flab.planb.dto.member.AddressDTO;
import com.flab.planb.dto.member.request.AddressRequest;
import com.flab.planb.service.AddressService;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.hamcrest.core.IsEqual;
import org.junit.jupiter.api.Assertions;
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
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
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
public class AddressControllerIntegrationTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String VALID_FAIL_001 = "VALID_FAIL_001";
    @Autowired
    private WebApplicationContext webContext;
    @Autowired
    private AddressService addressService;
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
        addressDTO = AddressDTO.builder().memberId(1L).address("서울특별시 종로구 종로 1").zipCode("03154").build();
        addressService.saveAddress(addressDTO);
    }

    @Test
    @DisplayName("memberId Positive 실패")
    void when_memberId_not_positive_expected_bad_request() throws Exception {
        // given
        addressDTO = AddressDTO.builder().memberId(0L).address("서울특별시 종로구 종로 1").zipCode("03154").build();
        // when
        final ResultActions actions = getResultActionsRequest(getPostBuilder());
        // then
        expectBadRequest(actions, VALID_FAIL_001);
    }

    @Test
    @DisplayName("zipCode NotBlank 실패")
    void when_zipCode_is_blank_expected_bad_request() throws Exception {
        // given
        addressDTO = AddressDTO.builder().memberId(1L).address("").zipCode("03154").build();
        // when
        final ResultActions actions = getResultActionsRequest(getPostBuilder());
        // then
        expectBadRequest(actions, VALID_FAIL_001);
    }

    @Test
    @DisplayName("zipCode 숫자가 아닌 값이 있어서 실패")
    void when_zipCode_contains_non_numeric_value_expected_bad_request() throws Exception {
        // given
        addressDTO = AddressDTO.builder().memberId(1L).address("서울특별시 종로구 종로 1").zipCode("01b11").build();
        // when
        final ResultActions actions = getResultActionsRequest(getPostBuilder());
        // then
        expectBadRequest(actions, VALID_FAIL_001);
    }

    @Test
    @DisplayName("zipCode 5자리 초과 실패")
    void when_zipCode_more_tan_five_digits_expected_bad_request() throws Exception {
        // given
        addressDTO = AddressDTO.builder().memberId(1L).address("서울특별시 종로구 종로 1").zipCode("012345").build();
        // when
        final ResultActions actions = getResultActionsRequest(getPostBuilder());
        // then
        expectBadRequest(actions, VALID_FAIL_001);
    }

    @Test
    @DisplayName("address NotBlank 실패")
    void when_address_is_blank_expected_bad_request() throws Exception {
        // given
        addressDTO = AddressDTO.builder().memberId(1L).address("").zipCode("03154").build();
        // when
        final ResultActions actions = getResultActionsRequest(getPostBuilder());
        // then
        expectBadRequest(actions, VALID_FAIL_001);
    }

    @Test
    @DisplayName("존재하지 않는 회원 ID 실패")
    void when_id_from_members_is_not_existed_expected_bad_request() throws Exception {
        // given
        addressDTO = AddressDTO.builder().memberId(Long.MAX_VALUE).address("서울특별시 종로구 종로 1").zipCode("03154").build();
        // when
        final ResultActions actions = getResultActionsRequest(getPostBuilder());
        // then
        expectBadRequest(actions, VALID_FAIL_001);
    }

    @Test
    @DisplayName("배달주소 등록 성공")
    void when_new_address_expected_ok() throws Exception {
        // given
        // when
        final ResultActions actions = getResultActionsRequest(getPostBuilder());
        // then
        expectOk(actions, "등록하였습니다.");
    }

    @Test
    @DisplayName("배달주소 조회")
    void when_find_address_expected_ok() throws Exception {
        // given
        // when
        final ResultActions actions = getResultActionsRequest(getGetBuilder("/" + addressDTO.getMemberId()));
        // then
        expectOk(actions, "조회에 성공하였습니다.");
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.data.result").isNotEmpty());
    }


    @Test
    @DisplayName("배달주소 단건 조회")
    void when_find_one_address_expected_ok() throws Exception {
        // given
        long id = addressService.findByMemberId(addressDTO.getMemberId()).get(0).getId();
        // when
        ResultActions actions = getResultActionsRequest(getGetBuilder("/" + addressDTO.getMemberId() + "/" + id));
        // then
        expectOk(actions, "조회에 성공하였습니다.");
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.data.result").exists());
    }

    @Test
    @DisplayName("배달주소 단건 삭제")
    void when_delete_one_address_expected_ok() throws Exception {
        // given
        List<AddressDTO> address = addressService.findByMemberId(addressDTO.getMemberId());
        // when
        ResultActions actions = getResultActionsRequest(getDeleteBuilder("/" + addressDTO.getMemberId()
                                                                             + "/" + address.get(address.size() - 1)
                                                                                            .getId()));
        // then
        expectOk(actions, "삭제하였습니다.");
    }

    @Test
    @DisplayName("배달주소 단건 수정")
    void when_patch_one_address_expected_ok() throws Exception {
        // given
        List<AddressDTO> address = addressService.findByMemberId(addressDTO.getMemberId());
        AddressRequest aliasPatchRequest = AddressRequest.builder().alias("배달주소").build();
        // when
        ResultActions actions = getResultActionsRequest(
            getPatchBuilder("/" + addressDTO.getMemberId() + "/" + address.get(address.size() - 1).getId()),
            aliasPatchRequest);
        // then
        expectPatchSucceed(actions, aliasPatchRequest, "alias");

        // given
        AddressRequest addressPatchRequest = AddressRequest.builder().zipCode("01234").address("서울시 강남구 삼성동").build();
        // when
        actions = getResultActionsRequest(
            getPatchBuilder("/" + addressDTO.getMemberId() + "/" + address.get(address.size() - 1).getId()),
            addressPatchRequest);
        // then
        expectPatchSucceed(actions, addressPatchRequest, "address");
    }

    private void expectPatchSucceed(ResultActions actions, AddressRequest request, String check)
        throws Exception {
        expectOk(actions, "수정하였습니다.");
        expectPatchedData(request, check);
    }

    private void expectPatchedData(AddressRequest request, String check) {
        List<AddressDTO> findAddress = addressService.findByMemberId(addressDTO.getMemberId());
        switch (check) {
            case "alias" -> {
                Assertions.assertEquals(request.getAlias(), findAddress.get(findAddress.size() - 1).getAlias());
            }
            case "address" -> {
                Assertions.assertEquals(request.getAddress(), findAddress.get(findAddress.size() - 1).getAddress());
                Assertions.assertEquals(request.getZipCode(), findAddress.get(findAddress.size() - 1).getZipCode());
            }
        }
    }

    private ResultActions getResultActionsRequest(MockHttpServletRequestBuilder requestBuilder) throws Exception {
        return mockMvc.perform(requestBuilder.contentType(MediaType.APPLICATION_JSON_UTF8)
                                             .content(objectMapper.writeValueAsString(addressDTO)))
                      .andDo(MockMvcResultHandlers.print());
    }

    private ResultActions getResultActionsRequest(MockHttpServletRequestBuilder requestBuilder, Object object)
        throws Exception {
        return mockMvc.perform(requestBuilder.contentType(MediaType.APPLICATION_JSON_UTF8)
                                             .content(objectMapper.writeValueAsString(object)))
                      .andDo(MockMvcResultHandlers.print());
    }

    private MockHttpServletRequestBuilder getPostBuilder() {
        return MockMvcRequestBuilders.post(getUri("/address"));
    }

    private MockHttpServletRequestBuilder getGetBuilder(String uri) {
        return MockMvcRequestBuilders.get(getUri("/address" + uri));
    }

    private MockHttpServletRequestBuilder getDeleteBuilder(String uri) {
        return MockMvcRequestBuilders.delete(getUri("/address" + uri));
    }

    private MockHttpServletRequestBuilder getPatchBuilder(String uri) {
        return MockMvcRequestBuilders.patch(getUri("/address" + uri));
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