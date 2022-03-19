package com.flab.planb.mapper.member;

import com.flab.planb.dto.member.Login;
import com.flab.planb.dto.member.Member;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MemberMapper {

    void saveMemberInfo(Member member);

    int countByMemberId(String memberId);

    int countByNickName(String nickName);

    Login findByMemberId(String memberId);

}
