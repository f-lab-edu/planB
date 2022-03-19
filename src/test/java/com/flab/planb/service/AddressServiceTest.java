package com.flab.planb.service;

import com.flab.planb.dto.member.Address;
import com.flab.planb.mapper.member.AddressMapper;
import com.flab.planb.service.member.AddressService;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.PropertySource;

@ExtendWith({MockitoExtension.class})
@PropertySource("file:src/main/resources/logback-dev.xml")
public class AddressServiceTest {

    private static final Logger LOG = LoggerFactory.getLogger(AddressServiceTest.class);
    @Mock
    private AddressMapper addressMapper;
    @InjectMocks
    private AddressService addressService;

    @BeforeEach
    void setup() {}

    @Test
    @DisplayName("주소 저장")
    void test_saveAddress() {
        // given
        Address address = Address.builder().memberId(1L).address("서울특별시 종로구 종로 1").zipCode("03154").build();
        // when
        addressMapper.saveAddress(address);
        // then
        Mockito.verify(addressMapper).saveAddress(address);
    }

    @Test
    @DisplayName("회원 ID 존재여부 확인")
    void existById() {
        int checkCouunt = 1;
        //given
        Mockito.when(addressMapper.existById(ArgumentMatchers.anyLong())).thenReturn(checkCouunt);
        // when
        int idCount = addressMapper.existById(ArgumentMatchers.anyLong());
        // then
        Mockito.verify(addressMapper).existById(ArgumentMatchers.anyLong());
        Assertions.assertEquals(checkCouunt, idCount);
    }

    @Test
    @DisplayName("회원 주소 전체 조회")
    void findByMemberId() {
        //given
        // when
        List<Address> adress = addressMapper.findByMemberId(ArgumentMatchers.anyLong());
        // then
        Mockito.verify(addressMapper).findByMemberId(ArgumentMatchers.anyLong());
    }

    @Test
    @DisplayName("주소 존재여부 확인")
    void existByMemberIdAndId() {
        int checkCouunt = 1;
        //given
        Mockito.when(addressMapper.existByMemberIdAndId(ArgumentMatchers.anyMap())).thenReturn(checkCouunt);
        // when
        int idCount = addressMapper.existByMemberIdAndId(ArgumentMatchers.anyMap());
        // then
        Mockito.verify(addressMapper).existByMemberIdAndId(ArgumentMatchers.anyMap());
        Assertions.assertEquals(checkCouunt, idCount);
    }

    @Test
    @DisplayName("회원 주소 단건 조회")
    void findByMemberIdAndId() {
        //given
        // when
        Address adress = addressMapper.findByMemberIdAndId(ArgumentMatchers.anyMap());
        // then
        Mockito.verify(addressMapper).findByMemberIdAndId(ArgumentMatchers.anyMap());
    }

    @Test
    @DisplayName("회원 주소 단건 삭제")
    void deleteByMemberIdAndId() {
        //given
        // when
        addressMapper.deleteByMemberIdAndId(ArgumentMatchers.anyMap());
        // then
        Mockito.verify(addressMapper).deleteByMemberIdAndId(ArgumentMatchers.anyMap());
    }

    @Test
    @DisplayName("회원 주소 단건 수정")
    void updateAddress() {
        //given
        // when
        addressMapper.updateAddress(ArgumentMatchers.any());
        // then
        Mockito.verify(addressMapper).updateAddress(ArgumentMatchers.any());
    }
}