package com.flab.planb.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flab.planb.common.ExceptionAdvice;
import com.flab.planb.common.MessageLookup;
import com.flab.planb.dto.member.AddressDTO;
import com.flab.planb.service.AddressService;
import java.net.URI;
import java.nio.charset.StandardCharsets;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.util.UriComponentsBuilder;

@PropertySource("file:src/main/resources/logback-dev.xml")
public class AddressControllerUnitTest {

    private static final MediaType JSON_UTF_8 = new MediaType(
        MediaType.APPLICATION_JSON.getType(),
        MediaType.APPLICATION_JSON.getSubtype(),
        StandardCharsets.UTF_8
    );
    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
    private final MessageLookup messageLookup = Mockito.spy(new MessageLookup(messageSource));
    private final String resultErrorCodeKey = "$.data.errorCode";
    private final String resultMessageKey = "$.statusMessage";
    private final String argumentNotValidCode = "VALID_FAIL_001";
    @Mock
    private AddressService addressService;
    @InjectMocks
    private AddressController addressController;
    @InjectMocks
    private ExceptionAdvice exceptionAdvice;
    private MockMvc mockMvc;
    private AddressDTO addressDTO;

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
        mockMvc = MockMvcBuilders.standaloneSetup(addressController)
                                 .setControllerAdvice(exceptionAdvice)
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
        final ResultActions actions = performAddress();
        // then
        expectArgNotValid(actions);
    }

    @Test
    @DisplayName("zipCode NotBlank 실패")
    void when_zipCode_is_blank_expected_bad_request() throws Exception {
        // given
        addressDTO.setZipCode("");
        // when
        final ResultActions actions = performAddress();
        // then
        expectArgNotValid(actions);
    }

    @Test
    @DisplayName("address Positive 실패")
    void when_address_is_blank_expected_bad_request() throws Exception {
        // given
        addressDTO.setAddress("");
        // when
        final ResultActions actions = performAddress();
        // then
        expectArgNotValid(actions);
    }

    @Test
    @DisplayName("존재하지 않는 회원 ID 실패")
    void when_id_from_members_is_not_existed_expected_bad_request() throws Exception {
        // given
        Mockito.when(addressService.countByIdFromMembers(ArgumentMatchers.anyLong())).thenReturn(0);
        // when
        final ResultActions actions = performAddress();
        // then
        expectArgNotValid(actions);
    }

    @Test
    @DisplayName("배달주소 등록 성공")
    void when_new_address_expected_ok() throws Exception {
        // given
        Mockito.when(addressService.countByIdFromMembers(ArgumentMatchers.anyLong())).thenReturn(1);
        // when
        final ResultActions actions = performAddress();
        // then
        actions.andExpect(MockMvcResultMatchers.status().isOk())
               .andExpect(MockMvcResultMatchers.content().contentType(JSON_UTF_8))
               .andExpect(MockMvcResultMatchers.jsonPath(resultMessageKey)
                                               .value(IsEqual.equalTo("등록하였습니다.")));
    }

    private ResultActions performAddress() throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.post(getUri("/address"))
                                                     .contentType(JSON_UTF_8)
                                                     .content(objectMapper.writeValueAsString(addressDTO)))
                      .andDo(MockMvcResultHandlers.print());
    }

    private void expectArgNotValid(ResultActions actions) throws Exception {
        actions.andExpect(MockMvcResultMatchers.status().isBadRequest())
               .andExpect(MockMvcResultMatchers.content().contentType(JSON_UTF_8))
               .andExpect(MockMvcResultMatchers.jsonPath(resultErrorCodeKey)
                                               .value(IsEqual.equalTo(argumentNotValidCode)));
    }

}