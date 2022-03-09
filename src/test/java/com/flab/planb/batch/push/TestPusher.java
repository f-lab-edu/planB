package com.flab.planb.batch.push;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TestPusher<T extends Count> {

    private final List<Count> failedList;

    public TestPusher() {
        failedList = new CopyOnWriteArrayList<>();
    }

    public List<Count> getFailedList() {
        return failedList;
    }

    public void push(List<? extends T> counts) {
        log.debug("=============================================");
        log.debug("counts size : {}", counts.size());
        failedList.addAll(counts);
        log.debug("=============================================");
    }

}