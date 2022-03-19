package com.flab.planb.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flab.planb.controller.member.AddressController;
import com.flab.planb.exception.ExceptionAdvice;
import com.flab.planb.response.message.MessageLookup;
import com.flab.planb.response.ResponseEntityBuilder;
import com.flab.planb.dto.member.Address;
import com.flab.planb.dto.member.request.AddressRequest;
import com.flab.planb.service.member.AddressService;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import org.hamcrest.core.IsEqual;
import org.junit.jupiter.api.Assertions;
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
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.util.UriComponentsBuilder;

@PropertySource("file:src/main/resources/logback-dev.xml")
public class AddressControllerUnitTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
    private final MessageLookup messageLookup = Mockito.spy(new MessageLookup(messageSource));
    private final ResponseEntityBuilder responseEntityBuilder = Mockito.spy(new ResponseEntityBuilder(messageLookup));
    @Mock
    private AddressService addressService;
    @InjectMocks
    private AddressController addressController;
    @InjectMocks
    private ExceptionAdvice exceptionAdvice;
    private MockMvc mockMvc;
    private Address address;

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
        mockMvc = MockMvcBuilders.standaloneSetup(addressController).setControllerAdvice(exceptionAdvice)
                                 .addFilter(new CharacterEncodingFilter(StandardCharsets.UTF_8.toString(), true))
                                 .build();

        address = Address.builder().memberId(1L).address("서울특별시 종로구 종로 1").zipCode("03154").build();
    }

    @Test
    @DisplayName("존재하지 않는 회원 ID 실패")
    void when_id_from_members_is_not_existed_expected_bad_request() throws Exception {
        // given
        Mockito.when(addressService.existById(ArgumentMatchers.anyLong())).thenReturn(0);
        // when
        ResultActions actions = getResultActionsRequest(getPostBuilder());
        // then
        expectBadRequest(actions, "VALID_FAIL_001");
    }

    @Test
    @DisplayName("배달주소 등록 성공")
    void when_new_address_expected_ok() throws Exception {
        // given
        Mockito.when(addressService.existById(ArgumentMatchers.anyLong())).thenReturn(1);
        // when
        ResultActions actions = getResultActionsRequest(getPostBuilder());
        // then
        expectOk(actions, "등록하였습니다.");
    }

    @Test
    @DisplayName("배달주소 전체 조회")
    void when_find_all_address_expected_ok() throws Exception {
        // given
        Mockito.when(addressService.existById(ArgumentMatchers.anyLong())).thenReturn(1);
        Mockito.when(addressService.findByMemberId(ArgumentMatchers.anyLong())).thenReturn(ArgumentMatchers.anyList());
        // when
        ResultActions actions = getResultActionsRequest(getGetBuilder("/" + address.getMemberId()));
        // then
        expectOk(actions, "조회에 성공하였습니다.");
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.data.result").exists());
    }

    @Test
    @DisplayName("존재하지 않는 주소 실패")
    void when_adress_is_not_existed_expected_bad_request() throws Exception {
        // given
        Mockito.when(addressService.existByMemberIdAndId(ArgumentMatchers.anyLong(), ArgumentMatchers.anyLong()))
               .thenReturn(0);
        // when
        ResultActions actions = getResultActionsRequest(getPostBuilder());
        // then
        expectBadRequest(actions, "VALID_FAIL_001");
    }


    @Test
    @DisplayName("배달주소 단건 조회")
    void when_find_one_address_expected_ok() throws Exception {
        // given
        Mockito.when(addressService.existById(ArgumentMatchers.anyLong())).thenReturn(1);
        Mockito.when(addressService.existByMemberIdAndId(ArgumentMatchers.anyLong(), ArgumentMatchers.anyLong()))
               .thenReturn(1);
        Mockito.when(addressService.findByMemberIdAndId(ArgumentMatchers.anyLong(), ArgumentMatchers.anyLong()))
               .thenReturn(address);
        // when
        ResultActions actions = getResultActionsRequest(getGetBuilder("/" + address.getMemberId()
                                                                          + "/" + address.getId()));
        // then
        expectOk(actions, "조회에 성공하였습니다.");
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.data.result").exists());
    }

    @Test
    @DisplayName("배달주소 단건 삭제")
    void when_delete_one_address_expected_ok() throws Exception {
        // given
        Mockito.when(addressService.existById(ArgumentMatchers.anyLong())).thenReturn(1);
        Mockito.when(addressService.existByMemberIdAndId(ArgumentMatchers.anyLong(), ArgumentMatchers.anyLong()))
               .thenReturn(1);
        // when
        ResultActions actions = getResultActionsRequest(getDeleteBuilder("/" + address.getMemberId()
                                                                             + "/" + address.getId()));
        // then
        expectOk(actions, "삭제하였습니다.");
    }

    @Test
    @DisplayName("배달주소 단건 수정")
    void when_patch_one_address_expected_ok() throws Exception {
        // given
        Mockito.when(addressService.existById(ArgumentMatchers.anyLong())).thenReturn(1);
        Mockito.when(addressService.existByMemberIdAndId(ArgumentMatchers.anyLong(), ArgumentMatchers.anyLong()))
               .thenReturn(1);
        // when
        ResultActions actions = getResultActionsRequest(getPatchBuilder("/" + address.getMemberId()
                                                                            + "/" + address.getId()));
        // then
        expectOk(actions, "수정하였습니다.");
    }

    @Test
    @DisplayName("우편번호 또는 주소 하나만 공백으로 배달주소 단건 수정 실패")
    void when_blank_patch_zipCode_or_address_expected_throws() {
        try {
            AddressRequest.builder().zipCode("").address("서울시 강남구 삼성동").build();
        } catch (IllegalArgumentException e) {
            Assertions.assertEquals("AddressRequestBuilder is not valid", e.getMessage());
        }

        try {
            AddressRequest.builder().zipCode("01234").address("").build();
        } catch (IllegalArgumentException e) {
            Assertions.assertEquals("AddressRequestBuilder is not valid", e.getMessage());
        }
    }

    @Test
    @DisplayName("우편번호 패턴 틀려서 배달주소 단건 수정 실패")
    void when_patch_zipCode_pattern_not_valid_expected_throws() {
        try {
            AddressRequest.builder().zipCode("111111").address("서울시 강남구 삼성동").build();
        } catch (IllegalArgumentException e) {
            Assertions.assertEquals("AddressRequestBuilder is not valid", e.getMessage());
        }
    }

    private ResultActions getResultActionsRequest(MockHttpServletRequestBuilder requestBuilder) throws Exception {
        return mockMvc.perform(requestBuilder.contentType(MediaType.APPLICATION_JSON_UTF8)
                                             .content(objectMapper.writeValueAsString(address)))
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