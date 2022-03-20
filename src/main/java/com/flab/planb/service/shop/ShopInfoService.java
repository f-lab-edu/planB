package com.flab.planb.service.shop;

import com.flab.planb.dto.subscription.Subscription;
import com.flab.planb.mapper.shop.ShopInfoMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class ShopInfoService {

    private final ShopInfoMapper shopInfoMapper;

    public boolean isNotExist(Subscription subscription) {
        return existsByShopId(subscription.getShopId()) == 0;
    }

    private int existsByShopId(long shopId) {
        return shopInfoMapper.existsByShopId(shopId);
    }


}