package com.flab.planb.aop;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.CodeSignature;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Component
@Aspect
@Slf4j
public class Logging {

    private final ObjectMapper mapper;

    @Before("execution(* com.flab.planb.controller.*Controller.*(..))")
    public void requestLog(JoinPoint joinPoint) {
        try {
            log.debug("[LOG] target-method : {}-{} parameters : {}",
                      joinPoint.getTarget().getClass().getSimpleName(),
                      joinPoint.getSignature().getName(),
                      mapper.writeValueAsString(getParameters(joinPoint)));
        } catch (JsonProcessingException e) {
            log.error("[LOG] target-method : {}-{} logging error caused by {}",
                      joinPoint.getTarget().getClass().getSimpleName(),
                      joinPoint.getSignature().getName(),
                      e.getMessage());
        }
    }

    private Map<String, Object> getParameters(JoinPoint joinPoint) {
        Map<String, Object> parameters = new HashMap<>();
        Object[] args = joinPoint.getArgs();
        String[] parameterNames = ((CodeSignature) joinPoint.getSignature()).getParameterNames();
        for (int i = 0; i < parameterNames.length; i++) {
            parameters.put(parameterNames[i], args[i]);
        }
        return parameters;
    }

}
