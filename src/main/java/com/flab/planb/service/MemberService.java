package com.flab.planb.service;

import com.flab.planb.dto.member.MemberDTO;
import com.flab.planb.service.mapper.MemberMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MemberService {

  private final MemberMapper memberMapper;
  private final PasswordEncoder passwordEncoder;

  public MemberService(MemberMapper memberMapper, PasswordEncoder passwordEncoder) {
    this.memberMapper = memberMapper;
    this.passwordEncoder = passwordEncoder;
  }

  public void insertMemberInfo(MemberDTO memberDTO) {
    memberDTO.setPasswd(passwordEncoder.encode(memberDTO.getPasswd()));
    memberMapper.insertMemberInfo(memberDTO);
  }

  public int selectMemberIdCount(String memberId) {
    return memberMapper.selectMemberIdCount(memberId);
  }

  public int selectNickNameCount(String nickName) {
    return memberMapper.selectNickNameCount(nickName);
  }

}
