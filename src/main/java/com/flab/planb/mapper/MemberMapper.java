package com.flab.planb.mapper;

import com.flab.planb.dto.member.MemberDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MemberMapper {

    void signUp(MemberDTO memberDTO);
}
