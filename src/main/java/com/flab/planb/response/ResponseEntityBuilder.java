package com.flab.planb.response;

import com.flab.planb.dto.response.ErrorResponseEntityParam;
import com.flab.planb.dto.response.OtherResponseEntityParam;
import com.flab.planb.dto.response.ResponseEntityParam;
import com.flab.planb.response.message.MessageLookup;
import com.flab.planb.response.message.MessageSet;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import java.util.Map;

@RequiredArgsConstructor
@Component
public class ResponseEntityBuilder {

    private final MessageLookup messageLookup;

    public ResponseEntity<?> get(HttpStatus httpStatus, MessageSet messageSet) {
        return get(httpStatus, messageSet, null, null);
    }

    public ResponseEntity<?> get(HttpStatus httpStatus, MessageSet messageSet, String[] messageArg) {
        return get(httpStatus, messageSet, messageArg, null);
    }

    public ResponseEntity<?> get(HttpStatus httpStatus, MessageSet messageSet, Map<String, ?> data) {
        return get(httpStatus, messageSet, null, data);
    }

    public ResponseEntity<?> get(HttpStatus httpStatus, MessageSet messageSet,
                                 String[] messageArg, Map<String, ?> data) {
        ResponseEntityParam param = create(messageSet, messageArg, data);

        return ResponseEntity
            .status(httpStatus)
            .body(param.getResponseMessage(
                messageLookup.getMessage(param.getMessageSet().getLookupKey(), (Object) lookupMessageArgs(messageArg)),
                param.getMessageSet().getCode()));
    }

    private ResponseEntityParam create(MessageSet messageSet, String[] messageArg, Map<String, ?> data) {
        switch (messageSet.getResponseResult()) {
            case ERROR -> {
                return new ErrorResponseEntityParam(messageSet, messageArg);
            }
            case SUCCEED -> {
                return new OtherResponseEntityParam(messageSet, messageArg, data);
            }
            default -> {
                return new ErrorResponseEntityParam(MessageSet.FAIL, null);
            }
        }
    }

    private String[] lookupMessageArgs(String[] messageArg) {
        if (ArrayUtils.isEmpty(messageArg)) {
            return null;
        }
        for (String message : messageArg) {
            messageLookup.getMessage(message);
        }
        return messageArg;
    }

}