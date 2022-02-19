package com.flab.planb.service;

import com.flab.planb.dto.subscription.Subscription;
import com.flab.planb.service.mapper.ShopBusinessDayMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class ShopBusinessDayService {

    private final ShopBusinessDayMapper shopBusinessDayMapper;

    public int existsDayoff(Subscription subscription) {
        return shopBusinessDayMapper.existsDayoff(subscription);
    }

    public int existsWorkingDay(Subscription subscription) {
        return shopBusinessDayMapper.existsWorkingDay(subscription);
    }
}
