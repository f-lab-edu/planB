package com.flab.planb.service.shop;

import com.flab.planb.dto.subscription.Subscription;
import com.flab.planb.mapper.shop.ShopBusinessDayMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class ShopBusinessDayService {

    private final ShopBusinessDayMapper shopBusinessDayMapper;

    public boolean isDayOff(Subscription subscription) {
        return existsDayoff(subscription) == 1;
    }

    public boolean isNotOpen(Subscription subscription) {
        return existsWorkingDay(subscription) == 0;
    }

    public boolean isNotAvailable(Subscription subscription) {
        return isNotOpen(subscription) || isDayOff(subscription);
    }

    private int existsDayoff(Subscription subscription) {
        return shopBusinessDayMapper.existsDayoff(subscription);
    }

    private int existsWorkingDay(Subscription subscription) {
        return shopBusinessDayMapper.existsWorkingDay(subscription);
    }

}
