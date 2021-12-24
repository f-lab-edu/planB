package com.flab.planb.security;

import com.flab.planb.common.MessageLookup;
import com.flab.planb.common.ResponseWriter;
import com.flab.planb.message.MessageCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import java.io.IOException;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
@Component
public class SecurityAccessDeniedHandler implements AccessDeniedHandler {

    private final ResponseWriter responseWriter;
    private final MessageLookup messageLookup;

    @Override
    public void handle(
        HttpServletRequest request,
        HttpServletResponse response,
        AccessDeniedException accessDeniedException
    ) throws IOException {
        responseWriter.writer(
            response,
            HttpStatus.FORBIDDEN,
            messageLookup.getMessage(MessageCode.DENIED_FAIL.getMessageKey()),
            Map.of("errorCode", MessageCode.DENIED_FAIL.getMessageCode())
        );
    }
}
