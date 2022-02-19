package com.flab.planb.service;

import com.flab.planb.dto.shop.ShopDayoff;
import com.flab.planb.service.mapper.ShopDayoffMapepr;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class ShopDayoffService {

    private final ShopDayoffMapepr shopDayoffMapepr;

    public List<ShopDayoff> findByShopId(long shopId) {
        return shopDayoffMapepr.findByShopId(shopId);
    }

}
