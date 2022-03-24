package com.flab.planb.config.resolver;

import com.flab.planb.exception.ExceptionType;
import com.flab.planb.response.message.MessageLookup;
import com.flab.planb.response.ResponseWriter;
import com.flab.planb.response.message.ResponseMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import java.io.IOException;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
@Component
@Slf4j
public class ExceptionResolver implements HandlerExceptionResolver {

    private final ResponseWriter responseWriter;
    private final MessageLookup messageLookup;

    @Override
    public ModelAndView resolveException(
        HttpServletRequest request, HttpServletResponse response,
        Object handler, Exception exception) {

        try {
            ExceptionType exceptionType = ExceptionType.getExceptionType(exception.getClass().getSimpleName());
            responseWriter.setHeader(response, exceptionType.getHttpStatus());
            response.getWriter()
                    .write(responseWriter.messageToString(
                        new ResponseMessage(
                            messageLookup.getMessage(exceptionType.getLookupKey()),
                            Map.of("errorCode", exceptionType.getCode()))));
        } catch (IOException e) {
            log.error("{}", e.getMessage());
        }
        return new ModelAndView();
    }
}