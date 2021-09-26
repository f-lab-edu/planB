package com.flab.planb.controller;

import com.flab.planb.dto.member.MemberDTO;
import com.flab.planb.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class MemberController {

    private final Logger log = LoggerFactory.getLogger(getClass());
    private final MemberService memberService;

    @PostMapping("/signUp")
    public String signUp(@RequestBody MemberDTO memberDTO) {
        log.debug(memberDTO.toString());
        memberService.signUp(memberDTO);
        return "";
    }
}
