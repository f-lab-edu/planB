package com.flab.planb.service;

import com.flab.planb.dto.member.AddressDTO;
import com.flab.planb.service.mapper.AddressMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class AddressService {

    private final AddressMapper addressMapper;

    public void saveAddress(AddressDTO addressDTO) {
        addressMapper.saveAddress(addressDTO);
    }

    public int countByIdFromMembers(long memberId) {
        return addressMapper.countByIdFromMembers(memberId);
    }

}
