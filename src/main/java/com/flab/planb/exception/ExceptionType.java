package com.flab.planb.exception;

import com.flab.planb.response.message.MessageType;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import java.util.Arrays;

@Getter
public enum ExceptionType {

    EXCEPTION("Exception", HttpStatus.INTERNAL_SERVER_ERROR, MessageType.FAIL),
    ILLEGAL_ARGUMENT_EXCEPTION("IllegalArgumentException", HttpStatus.BAD_REQUEST, MessageType.ILLEGAL_ARGUMENT_FAIL);

    private final String exceptionName;
    private final HttpStatus httpStatus;
    private final MessageType messageType;

    ExceptionType(String exceptionName, HttpStatus httpStatus, MessageType messageType) {
        this.exceptionName = exceptionName;
        this.httpStatus = httpStatus;
        this.messageType = messageType;
    }

    public static ExceptionType getExceptionType(String exceptionName) {
        return Arrays.stream(ExceptionType.values())
                     .filter(exceptionType -> exceptionType.getExceptionName().equals(exceptionName))
                     .findFirst().orElse(ExceptionType.EXCEPTION);
    }

    public String getLookupKey() {
        return this.messageType.getLookupKey();
    }

    public String getCode() {
        return this.messageType.getCode();
    }

}
