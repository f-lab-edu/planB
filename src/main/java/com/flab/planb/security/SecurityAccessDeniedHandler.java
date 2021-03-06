package com.flab.planb.security;

import com.flab.planb.response.message.MessageLookup;
import com.flab.planb.response.ResponseWriter;
import com.flab.planb.response.message.MessageSet;
import com.flab.planb.response.message.ResponseMessage;
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
        responseWriter.setHeader(response, HttpStatus.FORBIDDEN);
        response.getWriter()
                .write(responseWriter.messageToString(
                    new ResponseMessage(messageLookup.getMessage(MessageSet.DENIED_FAIL.getLookupKey()),
                                        Map.of("errorCode", MessageSet.DENIED_FAIL.getCode()))));
    }
}
