package com.flab.planb.mapper.shop;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ShopInfoMapper {

    int existsByShopId(long shopId);
}
