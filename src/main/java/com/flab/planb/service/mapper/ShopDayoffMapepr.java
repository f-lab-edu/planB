package com.flab.planb.service.mapper;

import com.flab.planb.dto.shop.ShopDayoff;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface ShopDayoffMapepr {

    List<ShopDayoff> findByShopId(long shopId);

}
