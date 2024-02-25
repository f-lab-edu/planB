package com.flab.planb.dto.response;

import com.flab.planb.response.message.MessageType;
import com.flab.planb.response.message.ResponseMessage;
import lombok.Getter;
import lombok.ToString;
import java.util.Map;

@Getter
@ToString
public class OtherResponseEntityParam extends ResponseEntityParam {

    private final Map<String, ?> data;

    public OtherResponseEntityParam(MessageType messageType, String[] messageArg, Map<String, ?> data) {
        super(messageType, messageArg);
        this.data = data;
    }

    @Override
    public ResponseMessage getResponseMessage(String message, String code) {
        return new ResponseMessage(message, data);
    }
}
