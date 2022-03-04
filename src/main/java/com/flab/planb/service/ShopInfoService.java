package com.flab.planb.service;

import com.flab.planb.service.mapper.ShopInfoMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class ShopInfoService {

    private final ShopInfoMapper shopInfoMapper;

    public int existsByShopId(long shopId) {
        return shopInfoMapper.existsByShopId(shopId);
    }
}