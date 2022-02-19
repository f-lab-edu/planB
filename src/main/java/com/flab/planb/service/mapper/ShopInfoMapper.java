package com.flab.planb.service.mapper;

import com.flab.planb.dto.shop.ShopInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ShopInfoMapper {

    ShopInfo findByShopId(long shopId);

}
