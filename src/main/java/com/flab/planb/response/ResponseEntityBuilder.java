package com.flab.planb.response;

import com.flab.planb.dto.response.ErrorResponseEntityParam;
import com.flab.planb.dto.response.OtherResponseEntityParam;
import com.flab.planb.dto.response.ResponseEntityParam;
import com.flab.planb.response.message.MessageLookup;
import com.flab.planb.response.message.MessageType;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import java.util.Arrays;
import java.util.Map;

@RequiredArgsConstructor
@Component
public class ResponseEntityBuilder {

    private final MessageLookup messageLookup;

    public ResponseEntity<?> get(HttpStatus httpStatus, MessageType messageType) {
        return get(httpStatus, messageType, null, null);
    }

    public ResponseEntity<?> get(HttpStatus httpStatus, MessageType messageType, String[] messageArg) {
        return get(httpStatus, messageType, messageArg, null);
    }

    public ResponseEntity<?> get(HttpStatus httpStatus, MessageType messageType, Map<String, ?> data) {
        return get(httpStatus, messageType, null, data);
    }

    public ResponseEntity<?> get(HttpStatus httpStatus, MessageType messageType,
                                 String[] messageArg, Map<String, ?> data) {
        ResponseEntityParam param = create(messageType, messageArg, data);

        return ResponseEntity
            .status(httpStatus)
            .body(param.getResponseMessage(
                messageLookup.getMessage(param.getMessageType().getLookupKey(), lookupMessageArgs(messageArg)),
                param.getMessageType().getCode()));
    }

    private ResponseEntityParam create(MessageType messageType, String[] messageArg, Map<String, ?> data) {
        switch (messageType.getResponseResult()) {
            case ERROR -> {
                return new ErrorResponseEntityParam(messageType, messageArg);
            }
            case SUCCEED -> {
                return new OtherResponseEntityParam(messageType, messageArg, data);
            }
            default -> {
                return new ErrorResponseEntityParam(MessageType.FAIL, null);
            }
        }
    }

    private Object[] lookupMessageArgs(String[] messageArg) {
        if (ArrayUtils.isEmpty(messageArg)) {
            return null;
        }
        return Arrays.stream(messageArg).map(messageLookup::getMessage).toArray(Object[]::new);
    }

}