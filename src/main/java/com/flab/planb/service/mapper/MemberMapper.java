package com.flab.planb.service.mapper;

import com.flab.planb.dto.member.LoginDTO;
import com.flab.planb.dto.member.Member;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MemberMapper {

    void saveMemberInfo(Member member);

    int countByMemberId(String memberId);

    int countByNickName(String nickName);

    LoginDTO findByMemberId(String memberId);

}
