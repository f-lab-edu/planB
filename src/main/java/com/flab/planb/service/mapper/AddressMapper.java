package com.flab.planb.service.mapper;

import com.flab.planb.dto.member.AddressDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AddressMapper {

    void saveAddress(AddressDTO addressDTO);

    int existById(long memberId);

}
