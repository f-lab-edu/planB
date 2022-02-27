package com.flab.planb.service.mapper;

import com.flab.planb.dto.member.AddressDTO;
import com.flab.planb.dto.member.request.AddressRequest;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;
import java.util.Map;

@Mapper
public interface AddressMapper {

    void saveAddress(AddressDTO addressDTO);

    int existById(long memberId);

    int existByMemberIdAndId(Map<String, Long> memberId);

    List<AddressDTO> findByMemberId(long memberId);

    AddressDTO findByMemberIdAndId(Map<String, Long> param);

    void deleteByMemberIdAndId(Map<String, Long> memberId);

    void updateAddress(AddressRequest param);
}
