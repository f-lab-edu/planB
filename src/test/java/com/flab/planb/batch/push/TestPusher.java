package com.flab.planb.batch.push;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TestPusher<T> {

    public void push(List<? extends T> count) {
        log.debug("=============================================");
        log.debug("items.size : " + count.size());
        count.forEach(item -> log.debug(item.toString()));
        log.debug("=============================================");
    }

}