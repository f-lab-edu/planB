package com.flab.planb.service.mapper;

import com.flab.planb.dto.subscription.Subscription;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ShopWorkingDayMapper {

    int exists(Subscription subscription);

}
