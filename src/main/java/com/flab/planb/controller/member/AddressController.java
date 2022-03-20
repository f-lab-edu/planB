package com.flab.planb.controller.member;

import com.flab.planb.response.ResponseEntityBuilder;
import com.flab.planb.dto.member.Address;
import com.flab.planb.dto.member.request.AddressRequest;
import com.flab.planb.response.message.MessageSet;
import com.flab.planb.service.member.AddressService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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
    private final ResponseEntityBuilder responseEntityBuilder;

    @PostMapping("")
    public ResponseEntity<?> address(@RequestBody @Valid Address address) {
        if (addressService.isNotExistingMember(address.getMemberId())) {
            return responseEntityBuilder.get(HttpStatus.BAD_REQUEST, MessageSet.VALID_FAIL);
        }

        addressService.saveAddress(address);

        return responseEntityBuilder.get(HttpStatus.OK, MessageSet.INSERT_SUCCEED);
    }

    @GetMapping("/{memberId}")
    public ResponseEntity<?> getAddressList(@PathVariable long memberId) {
        if (addressService.isNotExistingMember(memberId)) {
            return responseEntityBuilder.get(HttpStatus.BAD_REQUEST, MessageSet.VALID_FAIL);
        }

        return responseEntityBuilder.get(
            HttpStatus.OK, MessageSet.SELECT_SUCCEED,
            Map.of("result", addressService.findByMemberId(memberId)));
    }

    @GetMapping("/{memberId}/{addressId}")
    public ResponseEntity<?> getOneAddress(@PathVariable long memberId, @PathVariable("addressId") long id) {
        if (addressService.notFoundedInformation(memberId, id)) {
            return responseEntityBuilder.get(HttpStatus.BAD_REQUEST, MessageSet.VALID_FAIL);
        }

        return responseEntityBuilder.get(
            HttpStatus.OK, MessageSet.SELECT_SUCCEED,
            Map.of("result", addressService.findByMemberIdAndId(memberId, id)));
    }

    @DeleteMapping("/{memberId}/{addressId}")
    public ResponseEntity<?> deleteAddress(@PathVariable long memberId, @PathVariable("addressId") long id) {
        if (addressService.notFoundedInformation(memberId, id)) {
            return responseEntityBuilder.get(HttpStatus.BAD_REQUEST, MessageSet.VALID_FAIL);
        }

        addressService.deleteByMemberIdAndId(memberId, id);

        return responseEntityBuilder.get(HttpStatus.OK, MessageSet.DELETE_SUCCEED);
    }

    @PatchMapping("/{memberId}/{addressId}")
    public ResponseEntity<?> patchAddress(@PathVariable long memberId, @PathVariable("addressId") long id,
                                          @RequestBody AddressRequest param) {
        if (addressService.notFoundedInformation(memberId, id)) {
            return responseEntityBuilder.get(HttpStatus.BAD_REQUEST, MessageSet.VALID_FAIL);
        }

        param.setId(id);
        param.setMemberId(memberId);
        addressService.updateAddress(param);

        return responseEntityBuilder.get(HttpStatus.OK, MessageSet.UPDATE_SUCCEED);
    }

}
