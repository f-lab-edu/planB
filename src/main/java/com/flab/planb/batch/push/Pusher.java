package com.flab.planb.batch.push;

import com.flab.planb.dto.subscription.PushInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class Pusher {

    public void push(PushInfo pushInfo) {
        log.info("current push info : {}", pushInfo.toString());
    }

}