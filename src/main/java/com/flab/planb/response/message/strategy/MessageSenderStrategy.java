package com.flab.planb.response.message.strategy;

import com.flab.planb.response.message.MessageLookup;
import com.flab.planb.response.ResponseWriter;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

public interface MessageSenderStrategy {

    void sendMessage(
        HttpServletResponse response,
        ResponseWriter responseWriter,
        MessageLookup messageLookup
    ) throws IOException;

}