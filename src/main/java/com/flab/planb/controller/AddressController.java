package com.flab.planb.controller;

import com.flab.planb.common.MessageLookup;
import com.flab.planb.dto.member.AddressDTO;
import com.flab.planb.dto.member.request.AddressRequest;
import com.flab.planb.message.MessageSet;
import com.flab.planb.message.ResponseMessage;
import com.flab.planb.service.AddressService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @GetMapping("/{memberId}")
    public ResponseEntity<?> getAddressList(@PathVariable long memberId) {
        log.debug("memberId : {}", memberId);

        if (isNotExistingMember(memberId)) {
            return getBadRequestResponseEntity(MessageSet.VALID_FAIL);
        }

        return getOkResponseEntity(MessageSet.SELECT_SUCCEED,
                                   Map.of("result", addressService.findByMemberId(memberId)));
    }

    @GetMapping("/{memberId}/{addressId}")
    public ResponseEntity<?> getOneAddress(@PathVariable long memberId, @PathVariable("addressId") long id) {
        log.debug("memberId : {}, addressId : {}", memberId, id);

        if (notFoundedInformation(memberId, id)) {
            return getBadRequestResponseEntity(MessageSet.VALID_FAIL);
        }

        return getOkResponseEntity(MessageSet.SELECT_SUCCEED,
                                   Map.of("result", addressService.findByMemberIdAndId(memberId, id)));
    }

    @DeleteMapping("/{memberId}/{addressId}")
    public ResponseEntity<?> deleteAddress(@PathVariable long memberId, @PathVariable("addressId") long id) {
        log.debug("memberId : {}, addressId : {}", memberId, id);

        if (notFoundedInformation(memberId, id)) {
            return getBadRequestResponseEntity(MessageSet.VALID_FAIL);
        }

        addressService.deleteByMemberIdAndId(memberId, id);

        return getOkResponseEntity(MessageSet.DELETE_SUCCEED);
    }

    @PatchMapping("/{memberId}/{addressId}")
    public ResponseEntity<?> patchAddress(@PathVariable long memberId, @PathVariable("addressId") long id,
                                          @RequestBody AddressRequest param) {
        log.debug("memberId : {}, addressId : {}, param : {}", memberId, id, param.toString());

        if (notFoundedInformation(memberId, id)) {
            return getBadRequestResponseEntity(MessageSet.VALID_FAIL);
        }

        param.setId(id);
        param.setMemberId(memberId);
        addressService.updateAddress(param);

        return getOkResponseEntity(MessageSet.UPDATE_SUCCEED);
    }

    private boolean notFoundedInformation(long memberId, long id) {
        return isNotExistingMember(memberId) || isNotExistingAddress(memberId, id);
    }

    private boolean isNotExistingMember(long memberId) {
        return addressService.existById(memberId) == 0;
    }

    private boolean isNotExistingAddress(long memberId, long id) {
        return addressService.existByMemberIdAndId(memberId, id) == 0;
    }

    private ResponseEntity<?> getBadRequestResponseEntity(MessageSet messageSet) {
        return ResponseEntity.badRequest().body(
            ResponseMessage.builder().statusMessage(messageLookup.getMessage(messageSet.getLookupKey()))
                           .data(Map.of("errorCode", messageSet.getCode())).build());
    }

    private ResponseEntity<?> getOkResponseEntity(MessageSet messageSet) {
        return ResponseEntity.ok(
            ResponseMessage.builder().statusMessage(messageLookup.getMessage(messageSet.getLookupKey())).build());
    }

    private ResponseEntity<?> getOkResponseEntity(MessageSet messageSet, Map<String, ?> data) {
        return ResponseEntity.ok(
            ResponseMessage.builder().statusMessage(messageLookup.getMessage(messageSet.getLookupKey())).data(data)
                           .build());
    }

}
