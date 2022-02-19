package com.flab.planb.service;

import com.flab.planb.dto.subscription.Subscription;
import com.flab.planb.service.mapper.ShopWorkingDayMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class ShopWorkingDayService {

    private final ShopWorkingDayMapper shopWorkingDayMapper;


    public int exists(Subscription subscription) {
        return shopWorkingDayMapper.exists(subscription);
    }

}
