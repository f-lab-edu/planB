package com.flab.planb.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flab.planb.config.DBConfig;
import com.flab.planb.config.RootConfig;
import com.flab.planb.config.SecurityConfig;
import com.flab.planb.config.ServletConfig;
import com.flab.planb.dto.member.MemberDTO;
import com.flab.planb.service.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebAppConfiguration
@ExtendWith({MockitoExtension.class})
@ContextConfiguration(classes = {ServletConfig.class, RootConfig.class, DBConfig.class, SecurityConfig.class})
public class MemberControllerUnitTest {
    
    private static final Logger LOG = LoggerFactory.getLogger(MemberControllerUnitTest.class);
    private static final MediaType JSON_UTF_8 = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), StandardCharsets.UTF_8);
    @Mock
    private MemberService memberService;
    @InjectMocks
    private MemberController memberController;
    private MockMvc mockMvc;
    private MemberDTO member;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(memberController).addFilter(new CharacterEncodingFilter("UTF-8", true)).build();
        member = MemberDTO.builder().userId("test").nickname("테스트").passwd("test1234").tel("01012345678").build();
    }
    
    @Test
    @DisplayName("Valid 체크")
    void signUpTest() throws Exception {
        member.setUserId("");
        member.setNickname(" ");
        member.setPasswd(null);
        member.setTel(null);
        URI uri = UriComponentsBuilder.fromUriString("/members").build().encode().toUri();
        final ResultActions actions = mockMvc.perform(post(uri).contentType(JSON_UTF_8).content(new ObjectMapper().writeValueAsString(member)))
            .andExpect(status().isBadRequest())
            .andDo(print());
    }
}