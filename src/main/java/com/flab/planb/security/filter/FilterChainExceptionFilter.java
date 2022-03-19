package com.flab.planb.security.filter;

import javax.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
@Component
@Slf4j
public class FilterChainExceptionFilter extends OncePerRequestFilter {

    @Resource(name = "exceptionResolver")
    private final HandlerExceptionResolver exceptionResolver;

    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain
    ) {
        try {
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            log.error("Spring Security Filter Chain Exception : {}", e.getMessage());
            exceptionResolver.resolveException(request, response, null, e);
        }
    }
}
