package com.flab.planb.batch.push;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;

@Slf4j
public class CustomItemWriter<T> implements ItemWriter<T> {

    private final TestPusher<T> pusherTest;

    public CustomItemWriter() {
        this.pusherTest = new TestPusher<>();
    }

    @Override
    public void write(List<? extends T> items) throws Exception {
        pusherTest.push(items);
    }
}
