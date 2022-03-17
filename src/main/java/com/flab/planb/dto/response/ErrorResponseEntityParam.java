package com.flab.planb.dto.response;

import com.flab.planb.message.MessageSet;
import com.flab.planb.message.ResponseMessage;
import lombok.ToString;
import java.util.Map;

@ToString
public class ErrorResponseEntityParam extends ResponseEntityParam {

    public ErrorResponseEntityParam(MessageSet messageSet, String[] messageArg) {
        super(messageSet, messageArg);
    }

    @Override
    public ResponseMessage getResponseMessage(String message, String code) {
        return new ResponseMessage(message, Map.of("errorCode", code));
    }
}
