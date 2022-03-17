package com.flab.planb;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.net.URI;
import org.hamcrest.core.IsEqual;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.util.UriComponentsBuilder;

public class TestUtils {

    private static final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    public static ResultActions requestPost(MockMvc mockMvc, String uri, Object object) throws Exception {
        return getResultActionsRequest(mockMvc, MockMvcRequestBuilders.post(getUri(uri)), object);
    }

    public static ResultActions requestGet(MockMvc mockMvc, String uri, Object object) throws Exception {
        return getResultActionsRequest(mockMvc, MockMvcRequestBuilders.get(getUri(uri)), object);
    }

    public static ResultActions requestDelete(MockMvc mockMvc, String uri, Object object) throws Exception {
        return getResultActionsRequest(mockMvc, MockMvcRequestBuilders.delete(getUri(uri)), object);
    }

    public static ResultActions requestPatch(MockMvc mockMvc, String uri, Object object) throws Exception {
        return getResultActionsRequest(mockMvc, MockMvcRequestBuilders.patch(getUri(uri)), object);
    }


    public static void expectBadRequest(ResultActions actions, String errorCode) throws Exception {
        actions.andExpect(MockMvcResultMatchers.status().isBadRequest())
               .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
               .andExpect(MockMvcResultMatchers.jsonPath("$.data.errorCode").value(IsEqual.equalTo(errorCode)));
    }

    public static void expectOk(ResultActions actions, String statusMessage) throws Exception {
        actions.andExpect(MockMvcResultMatchers.status().isOk())
               .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
               .andExpect(MockMvcResultMatchers.jsonPath("$.statusMessage").value(IsEqual.equalTo(statusMessage)));
    }

    private static ResultActions getResultActionsRequest(MockMvc mockMvc, MockHttpServletRequestBuilder requestBuilder,
                                                         Object object) throws Exception {
        return mockMvc.perform(requestBuilder.contentType(MediaType.APPLICATION_JSON_UTF8)
                                             .content(objectMapper.writeValueAsString(object)))
                      .andDo(MockMvcResultHandlers.print());
    }

    private static URI getUri(String uri) {
        return UriComponentsBuilder.fromUriString(uri).build().encode().toUri();
    }


}
