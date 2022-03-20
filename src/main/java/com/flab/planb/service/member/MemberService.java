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

    public boolean isNotUsedMemberId(String sqlParameter) {
        return countByMemberId(sqlParameter) == 0;
    }

    public boolean isNotUsedNicname(String sqlParameter) {
        return countByNickName(sqlParameter) == 0;
    }

    public Login findByMemberId(String memberId) {
        return memberMapper.findByMemberId(memberId);
    }

    private int countByMemberId(String memberId) {
        return memberMapper.countByMemberId(memberId);
    }

    private int countByNickName(String nickName) {
        return memberMapper.countByNickName(nickName);
    }

}
