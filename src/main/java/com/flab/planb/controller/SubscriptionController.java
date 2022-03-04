package com.flab.planb.controller;

import com.flab.planb.common.MessageLookup;
import com.flab.planb.dto.subscription.SubscriptionMenu;
import com.flab.planb.dto.subscription.request.SubscriptionRequest;
import com.flab.planb.message.MessageCode;
import com.flab.planb.message.ResponseMessage;
import com.flab.planb.service.ShopInfoCheck;
import com.flab.planb.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.Map;
import javax.validation.Valid;


@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/subscription")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;
    private final MessageLookup messageLookup;
    private final ShopInfoCheck shopInfoCheck;

    @PostMapping("")
    public ResponseEntity<?> regist(@RequestBody @Valid SubscriptionRequest request) {
        log.debug(request.toString());

        if (canNotSubscribe(request)) {
            return getBadRequestResponseEntity(MessageCode.VALID_FAIL);
        }

        saveSubscriptionInfo(request);

        return getOkResponseEntity(MessageCode.INSERT_SUCCEED);
    }

    @Transactional
    protected void saveSubscriptionInfo(SubscriptionRequest request) {
        subscriptionService.saveSubscription(request);
        log.debug("subscription id : {}", request.getId());

        List<SubscriptionMenu> menus = getSubscriptionMenus(request);
        saveSubscriptionMenus(menus);
        saveSubscriptionMenuOptions(menus);
    }

    private void saveSubscriptionMenus(List<SubscriptionMenu> menus) {
        subscriptionService.saveSubscriptionMenus(menus);
        menus.forEach(m -> log.debug("subscription id : {}, menu id : {}", m.getSubscriptionId(), m.getMenuId()));
    }

    private void saveSubscriptionMenuOptions(List<SubscriptionMenu> menus) {
        subscriptionService.saveSubscriptionMenuOptions(menus);
    }

    private List<SubscriptionMenu> getSubscriptionMenus(SubscriptionRequest request) {
        return request.getSubscriptionMenus()
                      .entrySet().stream()
                      .map(e -> new SubscriptionMenu(request.getId(),
                                                     e.getKey(),
                                                     e.getValue()))
                      .toList();
    }

    private boolean canNotSubscribe(SubscriptionRequest subscriptionRequest) {
        return shopInfoCheck.isNotAvailable(subscriptionRequest) || isDuplicateSubscription(subscriptionRequest);
    }

    private boolean isDuplicateSubscription(SubscriptionRequest subscriptionRequest) {
        return subscriptionService.existsDuplicateSubscription(subscriptionRequest) > 0;
    }

    private ResponseEntity<?> getBadRequestResponseEntity(MessageCode messageCode) {
        return ResponseEntity.badRequest()
                             .body(ResponseMessage.builder()
                                                  .statusMessage(messageLookup.getMessage(messageCode.getKey()))
                                                  .data(Map.of("errorCode", messageCode.getValue()))
                                                  .build());
    }

    private ResponseEntity<?> getOkResponseEntity(MessageCode messageCode) {
        return ResponseEntity.ok(ResponseMessage.builder()
                                                .statusMessage(messageLookup.getMessage(messageCode.getKey()))
                                                .build());
    }

}
