package com.flab.planb.controller;

import com.flab.planb.dto.shop.ShopDayoff;
import com.flab.planb.dto.shop.ShopInfo;
import com.flab.planb.dto.subscription.Subscription;
import com.flab.planb.service.ShopDayoffService;
import com.flab.planb.service.ShopInfoService;
import com.flab.planb.service.ShopWorkingDayService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


@RequiredArgsConstructor
@Component
@Slf4j
public class ShopInfoCheck {

    private final ShopInfoService shopInfoService;
    private final ShopDayoffService shopDayoffService;
    private final ShopWorkingDayService shopWorkingDayService;

    public boolean isNotavailable(Subscription subscription) {
        return isNotExist(subscription) || isNotOpen(subscription) || isDayOff(subscription);
    }

    private boolean isNotExist(Subscription subscription) {
        getShopInfo(subscription.getShopId());
        return false;
    }

    private boolean isDayOff(Subscription subscription) {
        log.debug("shop id : {}, target day : {}", subscription.getShopId(), subscription.getSubscriptionDay());

        return getShopDayoff(subscription.getShopId())
            .stream().anyMatch(o -> o.getDay().getCode() == subscription.getSubscriptionDay());
    }

    private boolean isNotOpen(Subscription subscription) {
        return shopWorkingDayService.exists(subscription) == 0;
    }

    private ShopInfo getShopInfo(long shopId) {
        return shopInfoService.findByShopId(shopId);
    }

    private List<ShopDayoff> getShopDayoff(long shopId) {
        return Optional.ofNullable(shopDayoffService.findByShopId(shopId)).orElseGet(Collections::emptyList);
    }

}
