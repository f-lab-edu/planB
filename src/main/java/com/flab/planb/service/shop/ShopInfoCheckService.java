package com.flab.planb.service.shop;

import com.flab.planb.dto.subscription.Subscription;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Slf4j
public class ShopInfoCheckService {

    private final ShopInfoService shopInfoService;

    private final ShopBusinessDayService shopBusinessDayService;

    public boolean isNotAvailable(Subscription subscription) {
        return isNotExist(subscription) || isNotOpen(subscription) || isDayOff(subscription);
    }

    private boolean isNotExist(Subscription subscription) {
        return shopInfoService.existsByShopId(subscription.getShopId()) == 0;
    }

    private boolean isDayOff(Subscription subscription) {
        log.debug("shop id : {}, target day : {}", subscription.getShopId(), subscription.getSubscriptionDay());
        return shopBusinessDayService.existsDayoff(subscription) == 1;
    }

    private boolean isNotOpen(Subscription subscription) {
        return shopBusinessDayService.existsWorkingDay(subscription) == 0;
    }

}
