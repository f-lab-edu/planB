package com.flab.planb.message;

import java.util.Map;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class SucceedMessage extends ResponseMessage {

  private Map<String, Object> result;

  public SucceedMessage(MessageCode messageCode) {
    super(messageCode);
  }

  public SucceedMessage(MessageCode messageCode, Map<String, Object> result) {
    super(messageCode);
    this.result = result;
  }

  public SucceedMessage(MessageCode messageCode, Object[] args) {
    super(messageCode, args);
  }

  public SucceedMessage(MessageCode messageCode, Object[] args, Map<String, Object> result) {
    super(messageCode, args);
    this.result = result;
  }

}