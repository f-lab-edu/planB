package com.flab.planb.batch.push;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class PushItemWriter<T> implements ItemWriter<T> {

    private final Pusher<T> pusher;

    @Override
    public void write(List<? extends T> items) throws Exception {
        pusher.push(items);
    }
}
