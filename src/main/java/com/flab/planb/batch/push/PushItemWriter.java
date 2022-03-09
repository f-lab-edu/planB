package com.flab.planb.batch.push;

import com.flab.planb.dto.push.PushInfo;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class PushItemWriter<T extends PushInfo> implements ItemWriter<T> {

    private final Pusher<PushInfo> pusher;
    private final String title;

    @Override
    public void write(@NonNull List<? extends T> items) {
        pusher.push(title, items);
    }
}