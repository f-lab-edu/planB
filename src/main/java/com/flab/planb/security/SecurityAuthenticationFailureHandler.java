package com.flab.planb.security;

import com.flab.planb.response.message.MessageLookup;
import com.flab.planb.response.ResponseWriter;
import com.flab.planb.response.message.MessageType;
import com.flab.planb.response.message.ResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import java.io.IOException;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
public class SecurityAuthenticationFailureHandler implements AuthenticationFailureHandler {

    private final ResponseWriter responseWriter;
    private final MessageLookup messageLookup;

    @Override
    public void onAuthenticationFailure(
        HttpServletRequest request,
        HttpServletResponse response,
        AuthenticationException exception
    ) throws IOException {
        responseWriter.setHeader(response, HttpStatus.BAD_REQUEST);
        response.getWriter()
                .write(responseWriter.messageToString(
                    new ResponseMessage(messageLookup.getMessage(MessageType.LOGIN_FAIL.getLookupKey()),
                                        Map.of("errorCode", MessageType.LOGIN_FAIL.getCode()))));
    }
}
