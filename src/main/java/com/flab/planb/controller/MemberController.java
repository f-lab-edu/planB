package com.flab.planb.controller;

import com.flab.planb.common.MessageLookup;
import com.flab.planb.dto.member.MemberDTO;
import com.flab.planb.message.MessageSet;
import com.flab.planb.message.ResponseMessage;
import com.flab.planb.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;
    private final MessageLookup messageLookup;
    private final PasswordEncoder passwordEncoder;
    private final String validKey = MessageSet.VALID_OVERLAP.getLookupKey();
    private final String validCode = MessageSet.VALID_OVERLAP.getCode();

    @GetMapping(value = "/member-id")
    public ResponseEntity<?> isExistMemberId(
        @RequestParam("check") @NotBlank String memberId) {
        return isCountByMemberIdZero(memberId) ? setSucceed(
            MessageSet.VALID_SUCCEED.getLookupKey()) : setFail(validKey, "text.id", validCode);
    }

    @GetMapping(value = "/nickname")
    public ResponseEntity<?> isExistNickname(
        @RequestParam("check") @NotBlank String nickname) {
        return isCountByNickNameZero(nickname) ? setSucceed(
            MessageSet.VALID_SUCCEED.getLookupKey()) : setFail(validKey, "text.nickname", validCode);
    }

    @PostMapping("")
    public ResponseEntity<?> signUp(@RequestBody @Valid MemberDTO memberDTO) {
        log.debug(memberDTO.toString());

        if (!isCountByMemberIdZero(memberDTO.getMemberId()) || !isCountByNickNameZero(memberDTO.getNickname())) {
            return setFail(MessageSet.INSERT_FAIL_DATA.getLookupKey(), null, MessageSet.INSERT_FAIL_DATA.getCode());
        }

        memberDTO.setPasswd(passwordEncoder.encode(memberDTO.getPasswd()));
        memberService.saveMemberInfo(memberDTO);

        return setSucceed(MessageSet.INSERT_SUCCEED.getLookupKey());
    }

    private boolean isCountByMemberIdZero(String sqlParameter) {
        return memberService.countByMemberId(sqlParameter) == 0;
    }

    private boolean isCountByNickNameZero(String sqlParameter) {
        return memberService.countByNickName(sqlParameter) == 0;
    }

    private ResponseEntity<?> setFail(String messageKey, String messageArg, String messageCode) {
        return ResponseEntity.badRequest().body(
            ResponseMessage.builder()
                           .statusMessage(messageLookup.getMessage(messageKey, messageLookup.getMessage(messageArg)))
                           .data(Map.of("errorCode", messageCode)).build()
        );
    }

    private ResponseEntity<?> setSucceed(String messageKey) {
        return ResponseEntity.ok(
            ResponseMessage.builder()
                           .statusMessage(messageLookup.getMessage(messageKey))
                           .build()
        );
    }

}
