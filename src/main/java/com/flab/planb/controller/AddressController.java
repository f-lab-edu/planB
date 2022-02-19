package com.flab.planb.controller;

import com.flab.planb.common.MessageLookup;
import com.flab.planb.dto.member.AddressDTO;
import com.flab.planb.message.MessageSet;
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

        if (isNotExistingMember(addressDTO.getMemberId())) {
            return getBadRequestResponseEntity(MessageSet.VALID_FAIL);
        }

        addressService.saveAddress(addressDTO);

        return getOkResponseEntity(MessageSet.INSERT_SUCCEED);
    }

    private boolean isNotExistingMember(long memberId) {
        return addressService.existById(memberId) == 0;
    }

    private ResponseEntity<?> getBadRequestResponseEntity(MessageSet messageSet) {
        return ResponseEntity.badRequest()
                             .body(ResponseMessage.builder()
                                                  .statusMessage(messageLookup.getMessage(messageSet.getLookupKey()))
                                                  .data(Map.of("errorCode", messageSet.getCode()))
                                                  .build());
    }

    private ResponseEntity<?> getOkResponseEntity(MessageSet messageSet) {
        return ResponseEntity.ok(ResponseMessage.builder()
                                                .statusMessage(messageLookup.getMessage(messageSet.getLookupKey()))
                                                .build());
    }

}
