package com.flab.planb.common;

import com.flab.planb.dto.response.ErrorResponseEntityParam;
import com.flab.planb.dto.response.OtherResponseEntityParam;
import com.flab.planb.dto.response.ResponseEntityParam;
import com.flab.planb.message.MessageSet;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import java.util.Arrays;
import java.util.Map;

@RequiredArgsConstructor
@Component
@Getter
public class ResponseEntityBuilder {

    private final MessageLookup messageLookup;

    public ResponseEntity<?> getResponseEntity(HttpStatus httpStatus, MessageSet messageSet) {
        return getResponseEntity(httpStatus, messageSet, null, null);
    }

    public ResponseEntity<?> getResponseEntity(HttpStatus httpStatus, MessageSet messageSet, String[] messageArg) {
        return getResponseEntity(httpStatus, messageSet, messageArg, null);
    }

    public ResponseEntity<?> getResponseEntity(HttpStatus httpStatus, MessageSet messageSet,
                                               String[] messageArg, Map<String, ?> data) {
        ResponseEntityParam param = newResponseEntityParam(messageSet, messageArg, data);
        String[] messageArgs = param.getMessageArg();
        lookupMessageArgs(messageArgs);

        return ResponseEntity.status(httpStatus)
                             .body(param.getResponseMessage(
                                 messageLookup.getMessage(param.getMessageSet().getLookupKey(), (Object) messageArgs),
                                 param.getMessageSet().getCode()));
    }

    private ResponseEntityParam newResponseEntityParam(MessageSet messageSet, String[] messageArg,
                                                       Map<String, ?> data) {
        switch (messageSet.getResult()) {
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

    private void lookupMessageArgs(String[] messageArg) {
        if (!ArrayUtils.isEmpty(messageArg)) {
            Arrays.stream(messageArg).forEach(a -> a = messageLookup.getMessage(a));
        }
    }

}