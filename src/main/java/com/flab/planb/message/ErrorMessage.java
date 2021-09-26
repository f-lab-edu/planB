package com.flab.planb.message;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ErrorMessage extends ResponseMessage {

  private String errorCode;

  public ErrorMessage(MessageCode messageCode) {
    super(messageCode);
    this.errorCode = messageCode.getMessageCode();
  }

  public ErrorMessage(MessageCode messageCode, Object[] args) {
    super(messageCode, args);
    this.errorCode = messageCode.getMessageCode();
  }

}