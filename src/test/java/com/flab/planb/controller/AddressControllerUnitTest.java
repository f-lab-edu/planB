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

    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
    private final MessageLookup messageLookup = Mockito.spy(new MessageLookup(messageSource));
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
        mockMvc = MockMvcBuilders.standaloneSetup(addressController).setControllerAdvice(exceptionAdvice)
                                 .addFilter(new CharacterEncodingFilter(StandardCharsets.UTF_8.toString(), true))
                                 .build();

        addressDTO = AddressDTO.builder().memberId(1L).address("서울특별시 종로구 종로 1").zipCode("03154").build();
    }

    @Test
    @DisplayName("존재하지 않는 회원 ID 실패")
    void when_id_from_members_is_not_existed_expected_bad_request() throws Exception {
        // given
        Mockito.when(addressService.existById(ArgumentMatchers.anyLong())).thenReturn(0);
        // when
        ResultActions actions = getResultActionsOfPostRequest();
        // then
        expectBadRequest(actions, "VALID_FAIL_001");
    }

    @Test
    @DisplayName("배달주소 등록 성공")
    void when_new_address_expected_ok() throws Exception {
        // given
        Mockito.when(addressService.existById(ArgumentMatchers.anyLong())).thenReturn(1);
        // when
        ResultActions actions = getResultActionsOfPostRequest();
        // then
        expectOk(actions, "등록하였습니다.");
    }

    private ResultActions getResultActionsOfPostRequest() throws Exception {
        return mockMvc.perform(
                          MockMvcRequestBuilders.post(getUri("/address")).contentType(MediaType.APPLICATION_JSON_UTF8)
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