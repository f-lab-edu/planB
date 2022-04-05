package com.flab.planb.dto.response;

import com.flab.planb.response.message.MessageType;
import com.flab.planb.response.message.ResponseMessage;
import lombok.ToString;
import java.util.Map;

@ToString
public class ErrorResponseEntityParam extends ResponseEntityParam {

    public ErrorResponseEntityParam(MessageType messageType, String[] messageArg) {
        super(messageType, messageArg);
    }

    @Override
    public ResponseMessage getResponseMessage(String message, String code) {
        return new ResponseMessage(message, Map.of("errorCode", code));
    }
}
