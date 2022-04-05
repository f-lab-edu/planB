package com.flab.planb.controller.member;

import com.flab.planb.response.ResponseEntityBuilder;
import com.flab.planb.dto.member.Member;
import com.flab.planb.response.message.MessageType;
import com.flab.planb.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;
    private final ResponseEntityBuilder responseEntityBuilder;

    @GetMapping(value = "/member-id")
    public ResponseEntity<?> isExistMemberId(@RequestParam("check") @NotBlank String memberId) {
        return memberService.isNotUsedMemberId(memberId)
               ? responseEntityBuilder.get(HttpStatus.OK, MessageType.VALID_SUCCEED)
               : responseEntityBuilder.get(HttpStatus.BAD_REQUEST, MessageType.VALID_OVERLAP,
                                           new String[]{"text.id"});
    }

    @GetMapping(value = "/nickname")
    public ResponseEntity<?> isExistNickname(@RequestParam("check") @NotBlank String nickname) {
        return memberService.isNotUsedNicname(nickname)
               ? responseEntityBuilder.get(HttpStatus.OK, MessageType.VALID_SUCCEED)
               : responseEntityBuilder.get(HttpStatus.BAD_REQUEST, MessageType.VALID_OVERLAP,
                                           new String[]{"text.nickname"});
    }

    @PostMapping("")
    public ResponseEntity<?> signUp(@RequestBody @Valid Member member) {
        if (!memberService.isNotUsedMemberId(member.getMemberId())
            || !memberService.isNotUsedNicname(member.getNickname())) {
            return responseEntityBuilder.get(HttpStatus.BAD_REQUEST, MessageType.INSERT_FAIL_DATA);
        }

        memberService.saveMemberInfo(member);

        return responseEntityBuilder.get(HttpStatus.OK, MessageType.INSERT_SUCCEED);
    }

}
