package com.flab.planb.service.mapper;

import com.flab.planb.dto.member.MemberDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MemberMapper {

  void insertMemberInfo(MemberDTO memberDTO);

  int selectMemberIdCount(String memberId);

  int selectNickNameCount(String nickName);

}
