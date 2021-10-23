package com.flab.planb.service.mapper;

import com.flab.planb.dto.member.MemberDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MemberMapper {

    void saveMemberInfo(MemberDTO memberDTO);

    int countByMemberId(String memberId);

    int countByNickName(String nickName);

}
