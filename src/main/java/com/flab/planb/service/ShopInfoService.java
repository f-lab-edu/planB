package com.flab.planb.service;

import com.flab.planb.dto.shop.ShopInfo;
import com.flab.planb.service.mapper.ShopInfoMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Slf4j
public class ShopInfoService {

    private final ShopInfoMapper shopInfoMapper;

    public ShopInfo findByShopId(long shopId) {
        return Optional.ofNullable(shopInfoMapper.findByShopId(shopId))
                       .orElseThrow(() -> new IllegalArgumentException("가게를 찾지 못함"));
    }

}