package com.flab.planb.mapper.subscription;

import com.flab.planb.dto.push.PushInfo;
import com.flab.planb.dto.subscription.Subscription;
import com.flab.planb.dto.subscription.SubscriptionMenu;
import com.flab.planb.dto.subscription.request.SubscriptionRequest;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface SubscriptionMapper {

    void saveSubscription(Subscription subscription);

    int existsDuplicateSubscription(SubscriptionRequest subscriptionRequest);

    void saveSubscriptionMenus(List<SubscriptionMenu> subscriptionMenus);

    void saveSubscriptionMenuOptions(SubscriptionMenu subscriptionMenu);

    List<PushInfo> findPushList();

}
