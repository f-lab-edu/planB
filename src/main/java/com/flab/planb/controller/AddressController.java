package com.flab.planb.controller;

import com.flab.planb.common.MessageLookup;
import com.flab.planb.dto.member.AddressDTO;
import com.flab.planb.message.MessageCode;
import com.flab.planb.message.ResponseMessage;
import com.flab.planb.service.AddressService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;
import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/address")
public class AddressController {

    private final AddressService addressService;
    private final MessageLookup messageLookup;

    @PostMapping("")
    public ResponseEntity<?> address(@RequestBody @Valid AddressDTO addressDTO) {
        log.debug(addressDTO.toString());

        if (isCountByIdFromMembersZero(addressDTO.getMemberId())) {
            return setFail(MessageCode.VALID_FAIL.getMessageKey(),
                           MessageCode.VALID_FAIL.getMessageCode());
        }

        addressService.saveAddress(addressDTO);

        return setSucceed(MessageCode.INSERT_SUCCEED.getMessageKey());
    }

    private boolean isCountByIdFromMembersZero(long memberId) {
        return addressService.countByIdFromMembers(memberId) == 0;
    }

    private ResponseEntity<?> setFail(String messageKey, String messageCode) {
        return ResponseEntity.badRequest()
                             .body(ResponseMessage.builder()
                                                  .statusMessage(messageLookup.getMessage(messageKey))
                                                  .data(Map.of("errorCode", messageCode))
                                                  .build());
    }

    private ResponseEntity<?> setSucceed(String messageKey) {
        return ResponseEntity.ok(ResponseMessage.builder()
                                                .statusMessage(messageLookup.getMessage(messageKey))
                                                .build());
    }

}
