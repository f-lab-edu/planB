package com.flab.planb.controller;

import com.flab.planb.common.Common;
import com.flab.planb.dto.member.MemberDTO;
import com.flab.planb.message.MessageCode;
import com.flab.planb.message.ResponseMessage;
import com.flab.planb.message.SucceedMessage;
import com.flab.planb.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.HashMap;
import java.util.Objects;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@RestController
@Slf4j
@RequestMapping("/members")
public class MemberController {

  private final MemberService memberService;

  public MemberController(MemberService memberService) {
    this.memberService = memberService;
  }

  @GetMapping("/member-id/{memberId}/existence")
  public ResponseEntity<ResponseMessage> isExistMemberId(
      @PathVariable("memberId") @NotBlank String memberId) {
    String resultParamName = "none";
    String messageKey = "text.id";

    log.debug("memberId : " + memberId);

    return setResponseEntity(checkMemberId(memberId),
        MessageCode.VAID_OVERLAP, MessageCode.VALID_SUCCEED,
        resultParamName, messageKey);
  }

  @GetMapping("/nickname/{nickname}/existence")
  public ResponseEntity<ResponseMessage> isExistNickname(
      @PathVariable("nickname") @NotBlank String nickname) {
    String resultParamName = "none";
    String messageKey = "text.nickname";

    log.debug("nickname : " + nickname);

    return setResponseEntity(checkNickname(nickname),
        MessageCode.VAID_OVERLAP, MessageCode.VALID_SUCCEED,
        resultParamName, messageKey);
  }

  @PostMapping("")
  public ResponseEntity<ResponseMessage> signUp(@RequestBody @Valid MemberDTO memberDTO) {
    String resultParamName = "succeed";
    String messageKey = "";

    log.debug(memberDTO.toString());

    if (!checkMemberId(memberDTO.getMemberId()) || !checkNickname(memberDTO.getNickname())) {
      return setResponseEntity(false,
          MessageCode.INSERT_FAIL_DATA, MessageCode.INSERT_SUCCEED,
          resultParamName, messageKey);
    }

    memberService.insertMemberInfo(memberDTO);

    return setResponseEntity(true,
        MessageCode.INSERT_FAIL_DATA, MessageCode.INSERT_SUCCEED,
        resultParamName, messageKey);
  }

  private boolean checkMemberId(String sqlParameter) {
    return memberService.selectMemberIdCount(sqlParameter) == 0;
  }

  private boolean checkNickname(String sqlParameter) {
    return memberService.selectNickNameCount(sqlParameter) == 0;
  }

  private ResponseEntity<ResponseMessage> setResponseEntity(boolean isTrue,
      MessageCode failMessageCode, MessageCode succeedMessageCode,
      String resultParameterName, String messageKey) {
    HashMap<String, Object> hashMap = new HashMap<>();
    hashMap.put(resultParameterName, isTrue);
    failMessageCode =
        Objects.isNull(failMessageCode) ? MessageCode.INSERT_FAIL_DATA : failMessageCode;
    succeedMessageCode =
        Objects.isNull(succeedMessageCode) ? MessageCode.INSERT_SUCCEED : succeedMessageCode;

    if (!isTrue) {
      String[] messageReplace = {Common.getMessage(messageKey)};

      return ResponseEntity.ok(new SucceedMessage(failMessageCode, messageReplace, hashMap));
    }

    return ResponseEntity.ok(new SucceedMessage(succeedMessageCode, hashMap));
  }

}
