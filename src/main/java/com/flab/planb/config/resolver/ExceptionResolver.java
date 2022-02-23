package com.flab.planb.config.resolver;

import com.flab.planb.common.MessageLookup;
import com.flab.planb.common.ResponseWriter;
import com.flab.planb.message.MessageSenderStrategyGroup;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import java.io.IOException;
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
        HttpServletRequest request,
        HttpServletResponse response,
        Object handler,
        Exception exception
    ) {
        try {
            MessageSenderStrategyGroup.findMessageStrategy(exception.getClass().getSimpleName())
                                      .sendMessage(response, responseWriter, messageLookup);
        } catch (IOException e) {
            log.error("{}", e.getMessage());
        }
        return null;
    }
}