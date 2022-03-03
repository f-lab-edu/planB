package com.flab.planb.batch.push;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class Pusher<T> {

    public void push(List<? extends T> items) {
        log.info("current items info : {}", items.toString());
    }

}