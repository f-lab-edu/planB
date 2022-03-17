package com.flab.planb.message;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Getter;
import lombok.ToString;
import java.time.LocalDateTime;
import java.util.Map;

@JsonInclude(Include.NON_NULL)
@Getter
@ToString
public class ResponseMessage {

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime timestamp = LocalDateTime.now();
    private String statusMessage;
    private Map<String, ?> data;

    public ResponseMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public ResponseMessage(Map<String, ?> data) {
        this.data = data;
    }

    public ResponseMessage(String statusMessage, Map<String, ?> data) {
        this.statusMessage = statusMessage;
        this.data = data;
    }

}
