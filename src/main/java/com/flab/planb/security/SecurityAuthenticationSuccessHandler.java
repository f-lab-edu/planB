package com.flab.planb.security;

import com.flab.planb.common.ResponseWriter;
import com.flab.planb.dto.member.LoginDTO;
import com.flab.planb.message.ResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import java.io.IOException;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
public class SecurityAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final ResponseWriter responseWriter;

    @Override
    public void onAuthenticationSuccess(
        HttpServletRequest request,
        HttpServletResponse response,
        Authentication authentication
    ) throws IOException {
        responseWriter.setHeader(response, HttpStatus.OK);
        response.getWriter()
                .write(responseWriter.messageToString(ResponseMessage.builder()
                                                                     .data(Map.of("nickname",
                                                                                  ((LoginDTO) authentication.getPrincipal()).getNickname()))
                                                                     .build()));
    }

}