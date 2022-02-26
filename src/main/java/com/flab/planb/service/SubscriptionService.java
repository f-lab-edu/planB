package com.flab.planb.service;

import com.flab.planb.dto.subscription.PushInfo;
import com.flab.planb.dto.subscription.Subscription;
import com.flab.planb.dto.subscription.SubscriptionMenu;
import com.flab.planb.dto.subscription.request.SubscriptionRequest;
import com.flab.planb.service.mapper.SubscriptionMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class SubscriptionService {

    private final SubscriptionMapper subscriptionMapper;

    public void saveSubscription(Subscription subscription) {
        subscriptionMapper.saveSubscription(subscription);
    }

    public int existsDuplicateSubscription(SubscriptionRequest subscriptionRequest) {
        return subscriptionMapper.existsDuplicateSubscription(subscriptionRequest);
    }

    public void saveSubscriptionMenus(List<SubscriptionMenu> subscriptionMenus) {
        subscriptionMapper.saveSubscriptionMenus(subscriptionMenus);
    }

    public void saveSubscriptionMenuOptions(List<SubscriptionMenu> subscriptionMenus) {
        subscriptionMenus.stream().filter(m -> !m.getMenuOptions().isEmpty())
                         .forEach(subscriptionMapper::saveSubscriptionMenuOptions);
    }

    public List<PushInfo> findPushList() {
        return subscriptionMapper.findPushList();
    }
}
