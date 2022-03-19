package com.flab.planb.mapper.member;

import com.flab.planb.dto.member.Address;
import com.flab.planb.dto.member.request.AddressRequest;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;
import java.util.Map;

@Mapper
public interface AddressMapper {

    void saveAddress(Address address);

    int existById(long memberId);

    int existByMemberIdAndId(Map<String, Long> memberId);

    List<Address> findByMemberId(long memberId);

    Address findByMemberIdAndId(Map<String, Long> param);

    void deleteByMemberIdAndId(Map<String, Long> memberId);

    void updateAddress(AddressRequest param);
}
