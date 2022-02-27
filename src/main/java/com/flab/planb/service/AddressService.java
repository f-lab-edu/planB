package com.flab.planb.service;

import com.flab.planb.dto.member.AddressDTO;
import com.flab.planb.dto.member.request.AddressRequest;
import com.flab.planb.service.mapper.AddressMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
@Slf4j
public class AddressService {

    private final AddressMapper addressMapper;

    public void saveAddress(AddressDTO addressDTO) {
        addressMapper.saveAddress(addressDTO);
    }

    public int existById(long memberId) {
        return addressMapper.existById(memberId);
    }

    public int existByMemberIdAndId(long memberId, long id) {
        return addressMapper.existByMemberIdAndId(Map.of("memberId", memberId, "id", id));
    }

    public List<AddressDTO> findByMemberId(long memberId) {
        return addressMapper.findByMemberId(memberId);
    }

    public AddressDTO findByMemberIdAndId(long memberId, long id) {
        return addressMapper.findByMemberIdAndId(Map.of("memberId", memberId, "id", id));
    }

    public void deleteByMemberIdAndId(long memberId, long id) {
        addressMapper.deleteByMemberIdAndId(Map.of("memberId", memberId, "id", id));
    }

    public void updateAddress(AddressRequest param) {
        addressMapper.updateAddress(param);
    }
}
