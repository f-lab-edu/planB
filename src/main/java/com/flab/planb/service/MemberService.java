package com.flab.planb.service;

import com.flab.planb.dto.member.Member;
import com.flab.planb.service.mapper.MemberMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class MemberService {

    private final MemberMapper memberMapper;
    private final PasswordEncoder passwordEncoder;

    public void saveMemberInfo(Member member) {
        member.setPasswd(passwordEncoder.encode(member.getPasswd()));
        memberMapper.saveMemberInfo(member);
    }

    public int countByMemberId(String memberId) {
        return memberMapper.countByMemberId(memberId);
    }

    public int countByNickName(String nickName) {
        return memberMapper.countByNickName(nickName);
    }

}
