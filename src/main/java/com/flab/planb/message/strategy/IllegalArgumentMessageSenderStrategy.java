package com.flab.planb.message.strategy;

import com.flab.planb.common.MessageLookup;
import com.flab.planb.common.ResponseWriter;
import com.flab.planb.message.MessageCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import java.io.IOException;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
@Slf4j
public class IllegalArgumentMessageSenderStrategy implements MessageSenderStrategy {

    @Override
    public void writer(
        HttpServletResponse response,
        ResponseWriter responseWriter,
        MessageLookup messageLookup
    ) throws IOException {
        try {
            responseWriter.writer(
                response,
                HttpStatus.BAD_REQUEST,
                messageLookup.getMessage(MessageCode.ILLEGAL_ARGUMENT_FAIL.getMessageKey()),
                Map.of("errorCode", MessageCode.ILLEGAL_ARGUMENT_FAIL.getMessageCode())
            );
        } catch (IOException e) {
            log.error("{}", e.getMessage());
        }
    }
}
