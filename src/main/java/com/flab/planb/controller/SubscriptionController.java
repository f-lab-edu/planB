package com.flab.planb.controller;

import com.flab.planb.common.ResponseEntityBuilder;
import com.flab.planb.dto.subscription.request.SubscriptionRequest;
import com.flab.planb.message.MessageSet;
import com.flab.planb.service.ShopInfoCheck;
import com.flab.planb.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/subscription")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;
    private final ResponseEntityBuilder responseEntityBuilder;
    private final ShopInfoCheck shopInfoCheck;

    @PostMapping("")
    public ResponseEntity<?> regist(@RequestBody @Valid SubscriptionRequest request) {
        log.debug(request.toString());

        if (canNotSubscribe(request)) {
            return responseEntityBuilder.get(HttpStatus.BAD_REQUEST, MessageSet.VALID_FAIL);
        }

        subscriptionService.saveSubscriptionInfo(request);

        return responseEntityBuilder.get(HttpStatus.OK, MessageSet.INSERT_SUCCEED);
    }

    private boolean canNotSubscribe(SubscriptionRequest subscriptionRequest) {
        return shopInfoCheck.isNotAvailable(subscriptionRequest) || isDuplicateSubscription(subscriptionRequest);
    }

    private boolean isDuplicateSubscription(SubscriptionRequest subscriptionRequest) {
        return subscriptionService.existsDuplicateSubscription(subscriptionRequest) > 0;
    }

}
