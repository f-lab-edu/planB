package com.flab.planb.service.mapper;

import com.flab.planb.dto.subscription.Subscription;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ShopBusinessDayMapper {

    int existsDayoff(Subscription subscription);

    int existsWorkingDay(Subscription subscription);

}
