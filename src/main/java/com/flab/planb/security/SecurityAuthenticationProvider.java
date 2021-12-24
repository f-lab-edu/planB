package com.flab.planb.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;

@RequiredArgsConstructor
@Slf4j
public class SecurityAuthenticationProvider implements AuthenticationProvider {

    private final SecurityUserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication)
        throws AuthenticationException {
        SecurityUser securityUser = (SecurityUser) userDetailsService
            .loadUserByUsername(authentication.getName());

        if (!passwordEncoder.matches(
            (String) authentication.getCredentials(),
            securityUser.getPassword()
        )) {
            throw new BadCredentialsException("password not matches");
        }

        return new UsernamePasswordAuthenticationToken(
            securityUser.getLoginDTO(),
            null,
            securityUser.getAuthorities()
        );
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
