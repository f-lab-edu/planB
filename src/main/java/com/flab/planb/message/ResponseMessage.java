package com.flab.planb.message;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.flab.planb.common.Common;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ResponseMessage {

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private final LocalDateTime timestamp = LocalDateTime.now();
  protected String statusMessage;

  protected ResponseMessage(MessageCode messageCode) {
    this.statusMessage = Common.getMessage(messageCode.getMessageKey());
  }

  protected ResponseMessage(MessageCode messageCode, Object[] args) {
    this.statusMessage = Common.getMessage(messageCode.getMessageKey(), args);
  }

}
