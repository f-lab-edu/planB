package com.flab.planb.security;

import com.flab.planb.response.message.MessageLookup;
import com.flab.planb.response.ResponseWriter;
import com.flab.planb.response.message.MessageType;
import com.flab.planb.response.message.ResponseMessage;
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
                .write(responseWriter.messageToString(
                    new ResponseMessage(messageLookup.getMessage(MessageType.AUTH_FAIL.getLookupKey()),
                                        Map.of("errorCode", MessageType.AUTH_FAIL.getCode()))));
    }
}
