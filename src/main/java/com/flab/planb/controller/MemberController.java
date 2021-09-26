package com.flab.planb.controller;

import com.flab.planb.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class MemberController {

    private final Logger log = LoggerFactory.getLogger(getClass());
    private final MemberService memberService;

    @GetMapping("/signup")
    public String signup() {
        return "";
    }
}
