package com.flab.planb.service.mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ShopInfoMapper {

    int existsByShopId(long shopId);
}
