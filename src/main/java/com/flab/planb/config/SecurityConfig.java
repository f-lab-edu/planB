package com.flab.planb.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flab.planb.response.message.MessageLookup;
import com.flab.planb.response.ResponseWriter;
import com.flab.planb.security.filter.FilterChainExceptionFilter;
import com.flab.planb.security.SecurityAccessDeniedHandler;
import com.flab.planb.security.SecurityAuthenticationEntryPoint;
import com.flab.planb.security.SecurityAuthenticationFailureHandler;
import com.flab.planb.security.SecurityAuthenticationFilter;
import com.flab.planb.security.SecurityAuthenticationProvider;
import com.flab.planb.security.SecurityAuthenticationSuccessHandler;
import com.flab.planb.security.SecurityLogoutSuccessHandler;
import com.flab.planb.security.SecurityUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final SecurityUserDetailsService memberDetailsService;
    private final SecurityAuthenticationEntryPoint securityAuthenticationEntryPoint;
    private final SecurityAccessDeniedHandler securityAccessDeniedHandler;
    private final FilterChainExceptionFilter filterChainExceptionFilter;
    private final ObjectMapper objectMapper;
    private final MessageLookup messageLookup;
    private final ResponseWriter responseWriter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.exceptionHandling()
            .authenticationEntryPoint(securityAuthenticationEntryPoint)
            .accessDeniedHandler(securityAccessDeniedHandler)
            .and()
            .csrf().disable()
            .authorizeRequests()
            .antMatchers("/members", "/members/member-id", "/members/nickname")
            .permitAll().anyRequest().authenticated()
            .and()
            .formLogin().disable()
            .logout(logout -> logout.logoutUrl("/members/logout")
                                    .invalidateHttpSession(true)
                                    .clearAuthentication(true)
                                    .addLogoutHandler(new SecurityContextLogoutHandler())
                                    .logoutSuccessHandler(
                                        new SecurityLogoutSuccessHandler(responseWriter)
                                    ))
            .addFilterBefore(securityAuthenticationFilter(),
                             UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(filterChainExceptionFilter, SecurityAuthenticationFilter.class)
            .sessionManagement(session -> session.maximumSessions(1)
                                                 .maxSessionsPreventsLogin(false));
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(authenticationProvider());
    }

    @Bean
    public SecurityAuthenticationFilter securityAuthenticationFilter() throws Exception {
        SecurityAuthenticationFilter authenticationFilter = new SecurityAuthenticationFilter(
            new AntPathRequestMatcher("/members/login", HttpMethod.POST.name()),
            objectMapper
        );
        authenticationFilter.setAuthenticationManager(authenticationManagerBean());
        authenticationFilter.setAuthenticationSuccessHandler(
            new SecurityAuthenticationSuccessHandler(responseWriter)
        );
        authenticationFilter.setAuthenticationFailureHandler(
            new SecurityAuthenticationFailureHandler(responseWriter, messageLookup)
        );
        authenticationFilter.afterPropertiesSet();
        return authenticationFilter;
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        return new SecurityAuthenticationProvider(memberDetailsService, passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
