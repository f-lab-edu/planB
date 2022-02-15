package com.flab.planb.service;

import com.flab.planb.dto.member.AddressDTO;
import com.flab.planb.service.mapper.AddressMapper;
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
        AddressDTO addressDTO = AddressDTO.builder()
                                          .memberId(1L)
                                          .address("서울특별시 종로구 종로 1")
                                          .zipCode("03154")
                                          .build();
        // when
        addressMapper.saveAddress(addressDTO);
        // then
        Mockito.verify(addressMapper).saveAddress(addressDTO);
    }

    @Test
    @DisplayName("회원 ID 개수 확인")
    void countByIdFromMembers() {
        int checkCouunt = 1;
        //given
        Mockito.when(addressMapper.countByIdFromMembers(ArgumentMatchers.anyLong())).thenReturn(checkCouunt);
        // when
        int idCount = addressMapper.countByIdFromMembers(ArgumentMatchers.anyLong());
        // then
        Mockito.verify(addressMapper).countByIdFromMembers(ArgumentMatchers.anyLong());
        Assertions.assertEquals(checkCouunt, idCount);
    }
}