package com.flab.planb.batch.push;

import com.flab.planb.dto.push.PushInfo;
import com.google.firebase.messaging.BatchResponse;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.SendResponse;
import lombok.extern.slf4j.Slf4j;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.IntStream;
import java.util.List;

@Slf4j
public class Pusher<T extends PushInfo> {

    private final List<PushInfo> failedList;

    public Pusher() {
        failedList = new CopyOnWriteArrayList<>();
    }

    public List<PushInfo> getFailedList() {
        return failedList;
    }

    public void resetFailList() {
        failedList.clear();
    }

    public void push(String title, List<? extends T> items) {
        try {
            BatchResponse response = FirebaseMessaging.getInstance().sendAll(createMessages(title, items));
            addFailedPush(items, response);
        } catch (FirebaseMessagingException e) {
            log.error("Push fail caused by {}", e.getMessage());
        }
    }

    private void addFailedPush(List<? extends T> items, BatchResponse batchResponse) {
        if (batchResponse.getFailureCount() > 0) {
            List<SendResponse> responses = batchResponse.getResponses();
            failedList.addAll(
                IntStream.range(0, responses.size())
                         .filter(i -> !responses.get(i).isSuccessful())
                         .mapToObj(i -> {
                             log.error("Failure push info : {}", responses.get(i).toString());
                             return items.get(i);
                         }).toList());
        }
    }

    private List<Message> createMessages(String title, List<? extends T> items) {
        return items
            .stream()
            .map(item -> Message
                .builder()
                .setToken(item.getFcmToken())
                .putAllData(item.createData(title))
                .build()).toList();
    }

}