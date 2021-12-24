package com.flab.planb.security;

import com.flab.planb.common.MessageLookup;
import com.flab.planb.common.ResponseWriter;
import com.flab.planb.message.MessageCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import java.io.IOException;
import java.util.Map;
import javax.servlet.ServletException;
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
        responseWriter.writer(
            response,
            HttpStatus.FORBIDDEN,
            messageLookup.getMessage(MessageCode.DENIED_FAIL.getMessageKey()),
            Map.of("errorCode", MessageCode.DENIED_FAIL.getMessageCode())
        );
    }
}
