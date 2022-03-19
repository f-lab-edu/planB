package com.flab.planb.security;

import com.flab.planb.dto.member.Login;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import java.util.Collection;

public class SecurityUser extends User {

    private final Login login;

    public SecurityUser(Login login, Collection<? extends GrantedAuthority> authorities) {
        super(login.getMemberId(), login.getPasswd(), authorities);
        this.login = login;
    }

    public Login getLoginDTO() {
        return login;
    }

}
