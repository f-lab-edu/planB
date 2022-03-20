package com.flab.planb.service.subscription;

import com.flab.planb.dto.subscription.Subscription;
import com.flab.planb.dto.subscription.SubscriptionMenu;
import com.flab.planb.dto.subscription.request.SubscriptionRequest;
import com.flab.planb.mapper.subscription.SubscriptionMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class SubscriptionService {

    private final SubscriptionMapper subscriptionMapper;

    @Transactional
    public void saveSubscriptionInfo(SubscriptionRequest request) {
        saveSubscription(request);
        log.debug("subscription id : {}", request.getId());

        List<SubscriptionMenu> menus = getSubscriptionMenus(request);
        saveSubscriptionMenus(menus);
        saveSubscriptionMenuOptions(menus);
    }

    public void saveSubscription(Subscription subscription) {
        subscriptionMapper.saveSubscription(subscription);
    }

    public boolean isDuplicateSubscription(SubscriptionRequest subscriptionRequest) {
        return subscriptionMapper.existsDuplicateSubscription(subscriptionRequest) > 0;
    }

    public void saveSubscriptionMenus(List<SubscriptionMenu> menus) {
        subscriptionMapper.saveSubscriptionMenus(menus);
        menus.forEach(m -> log.debug("subscription id : {}, menu id : {}", m.getSubscriptionId(), m.getMenuId()));
    }

    public void saveSubscriptionMenuOptions(List<SubscriptionMenu> subscriptionMenus) {
        subscriptionMenus.stream().filter(m -> !m.getMenuOptions().isEmpty())
                         .forEach(subscriptionMapper::saveSubscriptionMenuOptions);
    }

    private List<SubscriptionMenu> getSubscriptionMenus(SubscriptionRequest request) {
        return request.getSubscriptionMenus()
                      .entrySet().stream()
                      .map(e -> new SubscriptionMenu(
                          request.getId(),
                          e.getKey(),
                          e.getValue()))
                      .toList();
    }

}
