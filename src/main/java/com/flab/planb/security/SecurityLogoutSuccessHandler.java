package com.flab.planb.security;

import com.flab.planb.response.ResponseWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
public class SecurityLogoutSuccessHandler implements LogoutSuccessHandler {

    private final ResponseWriter responseWriter;

    @Override
    public void onLogoutSuccess(
        HttpServletRequest request,
        HttpServletResponse response,
        Authentication authentication
    ) throws IOException {
        responseWriter.setHeader(response, HttpStatus.OK);
    }
}
