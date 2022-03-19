package com.flab.planb.response.message;

import com.flab.planb.response.message.strategy.ExceptionMessageSenderStrategy;
import com.flab.planb.response.message.strategy.IllegalArgumentMessageSenderStrategy;
import com.flab.planb.response.message.strategy.MessageSenderStrategy;
import java.util.HashMap;
import java.util.Map;

public class MessageSenderStrategyGroup {

    private static final Map<String, MessageSenderStrategy> messageStrategy = new HashMap<>();

    static {
        messageStrategy.put("exception", new ExceptionMessageSenderStrategy());
        messageStrategy.put("illegalargumentexception", new IllegalArgumentMessageSenderStrategy());
    }

    public static MessageSenderStrategy findMessageStrategy(String strategyName) {
        return messageStrategy.getOrDefault(
            strategyName.toLowerCase(),
            messageStrategy.get("exception")
        );
    }
}
