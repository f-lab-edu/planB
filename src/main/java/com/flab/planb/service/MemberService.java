package com.flab.planb.service;

import com.flab.planb.dto.member.MemberDTO;
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

    public void saveMemberInfo(MemberDTO memberDTO) {
        memberDTO.setPasswd(passwordEncoder.encode(memberDTO.getPasswd()));
        memberMapper.saveMemberInfo(memberDTO);
    }

    public int countByMemberId(String memberId) {
        return memberMapper.countByMemberId(memberId);
    }

    public int countByNickName(String nickName) {
        return memberMapper.countByNickName(nickName);
    }

}
