package com.flab.planb.message.strategy;

import com.flab.planb.common.MessageLookup;
import com.flab.planb.common.ResponseWriter;
import com.flab.planb.message.MessageSet;
import com.flab.planb.message.ResponseMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import java.io.IOException;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
@Slf4j
public class ExceptionMessageSenderStrategy implements MessageSenderStrategy {

    @Override
    public void sendMessage(
        HttpServletResponse response,
        ResponseWriter responseWriter,
        MessageLookup messageLookup
    ) throws IOException {
        responseWriter.setHeader(response, HttpStatus.BAD_REQUEST);
        response.getWriter()
                .write(responseWriter.messageToString(
                    new ResponseMessage(messageLookup.getMessage(MessageSet.BAD_REQUEST_FAIL.getLookupKey()),
                                        Map.of("errorCode", MessageSet.BAD_REQUEST_FAIL.getCode()))));
    }

}