package com.flab.planb.message.strategy;

import com.flab.planb.common.MessageLookup;
import com.flab.planb.common.ResponseWriter;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

public interface MessageSenderStrategy {

    void sendMessage(
        HttpServletResponse response,
        ResponseWriter responseWriter,
        MessageLookup messageLookup
    ) throws IOException;

}