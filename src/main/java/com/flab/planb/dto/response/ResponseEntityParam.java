package com.flab.planb.dto.response;

import com.flab.planb.message.MessageSet;
import com.flab.planb.message.ResponseMessage;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public abstract class ResponseEntityParam {

    protected final MessageSet messageSet;
    protected String[] messageArg;

    public ResponseEntityParam(MessageSet messageSet, String[] messageArg) {
        this.messageSet = messageSet;
        this.messageArg = messageArg;
    }

    public abstract ResponseMessage getResponseMessage(String message, String code);
}
