package com.flab.planb.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flab.planb.dto.member.Login;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class SecurityAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private final ObjectMapper objectMapper;

    public SecurityAuthenticationFilter(
        RequestMatcher requiresAuthenticationRequestMatcher,
        ObjectMapper objectMapper
    ) {
        super(requiresAuthenticationRequestMatcher);
        this.objectMapper = objectMapper;
    }

    @Override
    public Authentication attemptAuthentication(
        HttpServletRequest request,
        HttpServletResponse response
    ) throws AuthenticationException, IOException {
        Login login = objectMapper.readValue(request.getReader(), Login.class);
        log.debug("memberId {}, passwd {}", login.getMemberId(), login.getPasswd());
        if (StringUtils.isEmpty(login.getMemberId())
            || StringUtils.isEmpty(login.getPasswd())) {
            throw new IllegalArgumentException("memberId or Password is empty");
        }

        return getAuthenticationManager().authenticate(
            new UsernamePasswordAuthenticationToken(
                login.getMemberId(),
                login.getPasswd()
            )
        );
    }
}
