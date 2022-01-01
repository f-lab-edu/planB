package com.flab.planb.security;

import com.flab.planb.common.MessageLookup;
import com.flab.planb.common.ResponseWriter;
import com.flab.planb.message.MessageCode;
import com.flab.planb.message.ResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
@Component
public class SecurityAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ResponseWriter responseWriter;
    private final MessageLookup messageLookup;

    @Override
    public void commence(
        HttpServletRequest request,
        HttpServletResponse response,
        AuthenticationException authException
    ) throws IOException {
        responseWriter.setHeader(response, HttpStatus.UNAUTHORIZED);
        response.getWriter()
                .write(responseWriter.messageToString(ResponseMessage.builder()
                                                                     .statusMessage(messageLookup.getMessage(
                                                                         MessageCode.AUTH_FAIL.getMessageKey()))
                                                                     .data(Map.of("errorCode",
                                                                                  MessageCode.AUTH_FAIL.getMessageCode()))
                                                                     .build()));
    }
}
