package com.flab.planb.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flab.planb.config.DBConfig;
import com.flab.planb.config.RootConfig;
import com.flab.planb.config.SecurityConfig;
import com.flab.planb.config.ServletConfig;
import com.flab.planb.dto.member.MemberDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
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

import java.net.URI;
import java.nio.charset.StandardCharsets;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebAppConfiguration
@ExtendWith({SpringExtension.class})
@ContextConfiguration(classes = {ServletConfig.class, RootConfig.class, DBConfig.class, SecurityConfig.class})
@Transactional
public class MemberControllerIntegrationTest {
    
    private static final Logger LOG = LoggerFactory.getLogger(MemberControllerIntegrationTest.class);
    private static final MediaType JSON_UTF_8 = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), StandardCharsets.UTF_8);
    @Autowired
    private WebApplicationContext webContext;
    private MockMvc mockMvc;
    private MemberDTO member;
    
    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webContext).addFilter(new CharacterEncodingFilter("UTF-8", true)).build();
        member = MemberDTO.builder().passwd("test1234").tel("01012345678").build();
    }
    
    @Test
    @DisplayName("사용 중인 ID 확인 테스트")
    void checkUserIdTest() throws Exception {
        member.setUserId("test");
        
        URI uri = UriComponentsBuilder.fromUriString("/members/user-id/test/existence").build().encode().toUri();
        final ResultActions actions = mockMvc.perform(get(uri).contentType(JSON_UTF_8))
            .andDo(print());
        
        actions
            .andExpect(status().isOk())
            .andExpect(content().contentType(JSON_UTF_8))
            .andExpect(jsonPath("$.result.none").value(equalTo(false)));
    }
    
    @Test
    @DisplayName("사용 중인 닉네임 확인 테스트")
    void checkNickNameTest() throws Exception {
        member.setNickname("테스트");
        
        URI uri = UriComponentsBuilder.fromUriString("/members/nickname/테스트/existence").build().encode().toUri();
        final ResultActions actions = mockMvc.perform(get(uri).contentType(JSON_UTF_8))
            .andDo(print());
        
        actions
            .andExpect(status().isOk())
            .andExpect(content().contentType(JSON_UTF_8))
            .andExpect(jsonPath("$.result.none").value(equalTo(false)));
    }
    
    @Test
    @DisplayName("회원가입 테스트")
    void signUpTest() throws Exception {
        member.setUserId("test2");
        member.setNickname("테스트2");
        
        URI uri = UriComponentsBuilder.fromUriString("/members").build().encode().toUri();
        final ResultActions actions = mockMvc.perform(post(uri).contentType(JSON_UTF_8).content(new ObjectMapper().writeValueAsString(member)))
            .andDo(print());
        
        actions
            .andExpect(status().isOk())
            .andExpect(content().contentType(JSON_UTF_8))
            .andExpect(jsonPath("$.result.succeed").value(equalTo(true)));
    }
}