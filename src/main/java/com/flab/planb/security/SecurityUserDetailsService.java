package com.flab.planb.security;

import com.flab.planb.dto.member.LoginDTO;
import com.flab.planb.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class SecurityUserDetailsService implements UserDetailsService {

    private final MemberService memberService;

    @Override
    public UserDetails loadUserByUsername(String memberId) throws UsernameNotFoundException {
        log.debug("loadUserByUsername : {}", memberId);

        LoginDTO loginDTO = memberService.findByMemberId(memberId);
        if (loginDTO == null) {
            throw new UsernameNotFoundException("member id not found");
        }

        return new SecurityUser(loginDTO, List.of(new SimpleGrantedAuthority("ROLE_USER")));
    }

}
