package com.flab.planb.batch.push;

import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;


@RequiredArgsConstructor
@Slf4j
public class CustomItemWriter<T extends Count> implements ItemWriter<T> {

    private final TestPusher<Count> pusherTest;

    @Override
    public void write(@NotNull List<? extends T> items) {
        pusherTest.push(items);
    }
}
