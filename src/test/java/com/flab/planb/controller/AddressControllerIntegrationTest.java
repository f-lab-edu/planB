package com.flab.planb.controller;

import com.flab.planb.TestUtils;
import com.flab.planb.config.DBConfig;
import com.flab.planb.config.PushBatchConfig;
import com.flab.planb.config.RootConfig;
import com.flab.planb.config.SecurityConfig;
import com.flab.planb.config.ServletConfig;
import com.flab.planb.dto.member.Address;
import com.flab.planb.dto.member.request.AddressRequest;
import com.flab.planb.service.member.AddressService;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.junit.jupiter.api.Assertions;
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

    private final String URI = "/address";
    private final String VALID_FAIL_001 = "VALID_FAIL_001";
    @Autowired
    private WebApplicationContext webContext;
    @Autowired
    private AddressService addressService;
    private MockMvc mockMvc;
    private Address address;

    private URI getUri(String uri) {
        return UriComponentsBuilder.fromUriString(uri).build().encode().toUri();
    }

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webContext)
                                 .addFilter(new CharacterEncodingFilter(StandardCharsets.UTF_8.toString(), true))
                                 .build();
        address = Address.builder().memberId(1L).address("서울특별시 종로구 종로 1").zipCode("03154").build();
        addressService.saveAddress(address);
    }

    @Test
    @DisplayName("memberId Positive 실패")
    void when_memberId_not_positive_expected_bad_request() throws Exception {
        // given
        address = Address.builder().memberId(0L).address("서울특별시 종로구 종로 1").zipCode("03154").build();
        // when
        ResultActions actions = TestUtils.requestPost(mockMvc, URI, address);
        // then
        TestUtils.expectBadRequest(actions, VALID_FAIL_001);
    }

    @Test
    @DisplayName("zipCode NotBlank 실패")
    void when_zipCode_is_blank_expected_bad_request() throws Exception {
        // given
        address = Address.builder().memberId(1L).address("").zipCode("03154").build();
        // when
        ResultActions actions = TestUtils.requestPost(mockMvc, URI, address);
        // then
        TestUtils.expectBadRequest(actions, VALID_FAIL_001);
    }

    @Test
    @DisplayName("zipCode 숫자가 아닌 값이 있어서 실패")
    void when_zipCode_contains_non_numeric_value_expected_bad_request() throws Exception {
        // given
        address = Address.builder().memberId(1L).address("서울특별시 종로구 종로 1").zipCode("01b11").build();
        // when
        ResultActions actions = TestUtils.requestPost(mockMvc, URI, address);
        // then
        TestUtils.expectBadRequest(actions, VALID_FAIL_001);
    }

    @Test
    @DisplayName("zipCode 5자리 초과 실패")
    void when_zipCode_more_tan_five_digits_expected_bad_request() throws Exception {
        // given
        address = Address.builder().memberId(1L).address("서울특별시 종로구 종로 1").zipCode("012345").build();
        // when
        ResultActions actions = TestUtils.requestPost(mockMvc, URI, address);
        // then
        TestUtils.expectBadRequest(actions, VALID_FAIL_001);
    }

    @Test
    @DisplayName("address NotBlank 실패")
    void when_address_is_blank_expected_bad_request() throws Exception {
        // given
        address = Address.builder().memberId(1L).address("").zipCode("03154").build();
        // when
        ResultActions actions = TestUtils.requestPost(mockMvc, URI, address);
        // then
        TestUtils.expectBadRequest(actions, VALID_FAIL_001);
    }

    @Test
    @DisplayName("존재하지 않는 회원 ID 실패")
    void when_id_from_members_is_not_existed_expected_bad_request() throws Exception {
        // given
        address = Address.builder().memberId(Long.MAX_VALUE).address("서울특별시 종로구 종로 1").zipCode("03154").build();
        // when
        ResultActions actions = TestUtils.requestPost(mockMvc, URI, address);
        // then
        TestUtils.expectBadRequest(actions, VALID_FAIL_001);
    }

    @Test
    @DisplayName("배달주소 등록 성공")
    void when_new_address_expected_ok() throws Exception {
        // given
        // when
        ResultActions actions = TestUtils.requestPost(mockMvc, URI, address);
        // then
        TestUtils.expectOk(actions, "등록하였습니다.");
    }

    @Test
    @DisplayName("배달주소 조회")
    void when_find_address_expected_ok() throws Exception {
        // given
        // when
        ResultActions actions = TestUtils.requestGet(mockMvc, URI + "/" + address.getMemberId(), null);
        // then
        TestUtils.expectOk(actions, "조회에 성공하였습니다.");
        TestUtils.expectNotEmpty(actions);
    }

    @Test
    @DisplayName("배달주소 단건 조회")
    void when_find_one_address_expected_ok() throws Exception {
        // given
        long id = addressService.findByMemberId(address.getMemberId()).get(0).getId();
        // when
        ResultActions actions = TestUtils.requestGet(mockMvc, URI + "/" + address.getMemberId() + "/" + id, null);
        // then
        TestUtils.expectOk(actions, "조회에 성공하였습니다.");
        TestUtils.expectNotEmpty(actions);
    }

    @Test
    @DisplayName("배달주소 단건 삭제")
    void when_delete_one_address_expected_ok() throws Exception {
        // given
        List<Address> address = addressService.findByMemberId(this.address.getMemberId());
        // when
        ResultActions actions = TestUtils.requestDelete(mockMvc,
                                                        URI + "/" + this.address.getMemberId()
                                                            + "/" + address.get(address.size() - 1).getId(),
                                                        null);
        // then
        TestUtils.expectOk(actions, "삭제하였습니다.");
    }

    @Test
    @DisplayName("배달주소 단건 수정")
    void when_patch_one_address_expected_ok() throws Exception {
        // given
        List<Address> address = addressService.findByMemberId(this.address.getMemberId());
        AddressRequest aliasPatchRequest = AddressRequest.builder().alias("배달주소").build();
        // when
        ResultActions actions = TestUtils.requestPatch(mockMvc,
                                                       URI + "/" + this.address.getMemberId()
                                                           + "/" + address.get(address.size() - 1).getId(),
                                                       aliasPatchRequest);
        // then
        expectPatchSucceed(actions, aliasPatchRequest, "alias");

        // given
        AddressRequest addressPatchRequest = AddressRequest.builder().zipCode("01234").address("서울시 강남구 삼성동").build();
        // when
        actions = TestUtils.requestPatch(mockMvc,
                                         URI + "/" + this.address.getMemberId()
                                             + "/" + address.get(address.size() - 1).getId(),
                                         addressPatchRequest);
        // then
        expectPatchSucceed(actions, addressPatchRequest, "address");
    }

    private void expectPatchSucceed(ResultActions actions, AddressRequest request, String check)
        throws Exception {
        TestUtils.expectOk(actions, "수정하였습니다.");
        expectPatchedData(request, check);
    }

    private void expectPatchedData(AddressRequest request, String check) {
        List<Address> findAddress = addressService.findByMemberId(address.getMemberId());
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

}