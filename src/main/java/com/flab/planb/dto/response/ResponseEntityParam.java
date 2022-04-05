package com.flab.planb.dto.response;

import com.flab.planb.response.message.MessageType;
import com.flab.planb.response.message.ResponseMessage;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public abstract class ResponseEntityParam {

    protected final MessageType messageType;
    protected String[] messageArg;

    public ResponseEntityParam(MessageType messageType, String[] messageArg) {
        this.messageType = messageType;
        this.messageArg = messageArg;
    }

    public abstract ResponseMessage getResponseMessage(String message, String code);
}
