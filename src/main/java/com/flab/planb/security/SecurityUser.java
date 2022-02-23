package com.flab.planb.security;

import com.flab.planb.dto.member.LoginDTO;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import java.util.Collection;

public class SecurityUser extends User {

    private final LoginDTO loginDTO;

    public SecurityUser(LoginDTO loginDTO, Collection<? extends GrantedAuthority> authorities) {
        super(loginDTO.getMemberId(), loginDTO.getPasswd(), authorities);
        this.loginDTO = loginDTO;
    }

    public LoginDTO getLoginDTO() {
        return loginDTO;
    }

}
