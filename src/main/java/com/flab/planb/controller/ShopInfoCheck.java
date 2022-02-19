package com.flab.planb.controller;

import com.flab.planb.dto.shop.ShopInfo;
import com.flab.planb.dto.subscription.Subscription;
import com.flab.planb.service.ShopBusinessDayService;
import com.flab.planb.service.ShopInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Slf4j
public class ShopInfoCheck {

    private final ShopInfoService shopInfoService;

    private final ShopBusinessDayService shopBusinessDayService;

    public boolean isNotAvailable(Subscription subscription) {
        return isNotExist(subscription) || isNotOpen(subscription) || isDayOff(subscription);
    }

    private boolean isNotExist(Subscription subscription) {
        getShopInfo(subscription.getShopId());
        return false;
    }

    private boolean isDayOff(Subscription subscription) {
        log.debug("shop id : {}, target day : {}", subscription.getShopId(), subscription.getSubscriptionDay());
        return shopBusinessDayService.existsDayoff(subscription) == 1;
    }

    private boolean isNotOpen(Subscription subscription) {
        return shopBusinessDayService.existsWorkingDay(subscription) == 0;
    }

    private ShopInfo getShopInfo(long shopId) {
        return shopInfoService.findByShopId(shopId);
    }

}
