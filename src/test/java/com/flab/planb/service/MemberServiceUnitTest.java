package com.flab.planb.service;

import com.flab.planb.dto.member.Login;
import com.flab.planb.dto.member.Member;
import com.flab.planb.mapper.member.MemberMapper;
import com.flab.planb.service.member.MemberService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.PropertySource;

@ExtendWith({MockitoExtension.class})
@PropertySource("file:src/main/resources/logback-dev.xml")
class MemberServiceUnitTest {

    private static final Logger LOG = LoggerFactory.getLogger(MemberServiceUnitTest.class);
    @Mock
    private MemberMapper memberMapper;
    @InjectMocks
    private MemberService memberService;

    @BeforeEach
    void setUp() {}

    @Test
    @DisplayName("MemberId 개수 확인")
    void test_countByMemberId() throws Exception {
        int checkCouunt = 1;
        // given
        Mockito.when(memberMapper.countByMemberId(ArgumentMatchers.anyString()))
               .thenReturn(checkCouunt);
        // when
        int memberIdCount = memberService.countByMemberId(ArgumentMatchers.anyString());
        // then
        Assertions.assertEquals(checkCouunt, memberIdCount);
    }

    @Test
    @DisplayName("Nickname 개수 확인")
    void test_countByNickName() throws Exception {
        int checkCouunt = 1;
        // given
        Mockito.when(memberMapper.countByNickName(ArgumentMatchers.anyString()))
               .thenReturn(checkCouunt);
        // when
        int nickNameCount = memberService.countByNickName(ArgumentMatchers.anyString());
        // then
        Assertions.assertEquals(checkCouunt, nickNameCount);
    }

    @Test
    @DisplayName("회원가입")
    void test_saveMemberInfo() throws Exception {
        int checkCouunt = 1;
        // given
        Member member = Member.builder()
                              .memberId("memberTest").nickname("멤버테스트")
                              .passwd("test1234").tel("01012345678")
                              .build();
        // when
        memberService.saveMemberInfo(member);
        // then
        Mockito.verify(memberMapper).saveMemberInfo(member);
    }

    @Test
    @DisplayName("MemberId로 회원정보 조회")
    void test_findByMemberId() throws Exception {
        Login returnDTO = new Login("memberTest", "test1234");
        // given
        Mockito.when(memberMapper.findByMemberId(ArgumentMatchers.anyString()))
               .thenReturn(returnDTO);
        // when
        Login login = memberService.findByMemberId(ArgumentMatchers.anyString());
        // then
        Assertions.assertEquals(returnDTO, login);
    }

}