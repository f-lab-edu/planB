package com.flab.planb.service.member;

import com.flab.planb.dto.member.Login;
import com.flab.planb.dto.member.Member;
import com.flab.planb.mapper.member.MemberMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class MemberService {

    private final MemberMapper memberMapper;

    public void saveMemberInfo(Member member) {
        memberMapper.saveMemberInfo(member);
    }

    public int countByMemberId(String memberId) {
        return memberMapper.countByMemberId(memberId);
    }

    public int countByNickName(String nickName) {
        return memberMapper.countByNickName(nickName);
    }

    public Login findByMemberId(String memberId) {
        return memberMapper.findByMemberId(memberId);
    }

}
