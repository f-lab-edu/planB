package com.flab.planb.service;

import com.flab.planb.config.DBConfig;
import com.flab.planb.config.RootConfig;
import com.flab.planb.config.SecurityConfig;
import com.flab.planb.dto.member.MemberDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@WebAppConfiguration
@ExtendWith({SpringExtension.class})
@Transactional
@ContextConfiguration(classes = {RootConfig.class, DBConfig.class, SecurityConfig.class})
class MemberServiceUnitTest {

    private static final Logger LOG = LoggerFactory.getLogger(MemberServiceUnitTest.class);

    @Autowired
    private MemberService memberService;
    private MemberDTO member;

    @BeforeEach
    void setUp() {
        member = MemberDTO.builder().userId("test").nickname("테스트").passwd("test1234").tel("01012345678").build();
    }

    @Test
    @DisplayName("사용자 추가 서비스 테스트")
    void insertMemberInfoTest() {
        memberService.insertMemberInfo(member);
        assertEquals(1, memberService.selectUserIdCount("test"));
    }

    @Test
    @DisplayName("사용 중인 ID 확인 서비스 테스트")
    void selectUserIdCountTest() {
        assertEquals(0, memberService.selectUserIdCount("test2"));

        memberService.insertMemberInfo(member);
        assertEquals(1, memberService.selectUserIdCount("test"));
    }

    @Test
    @DisplayName("사용 중인 닉네임 확인 서비스 테스트")
    void selectNickNameCountTest() {
        assertEquals(0, memberService.selectNickNameCount("테스터2"));

        memberService.insertMemberInfo(member);
        assertEquals(1, memberService.selectNickNameCount("테스터"));
    }
}