package com.flab.planb.controller;

import com.flab.planb.message.ErrorMessage;
import com.flab.planb.message.MessageCode;
import com.flab.planb.message.ResponseMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
@Slf4j
public class ExceptionController {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ResponseMessage> methodValidException(
      MethodArgumentNotValidException exception, HttpServletRequest request) {
    log.error("유효성 검증 실패 URI : " + request.getRequestURI());
    exception.getBindingResult().getAllErrors().forEach(error -> {
      log.error(error.getDefaultMessage());
    });

    return ResponseEntity.badRequest().body(new ErrorMessage(MessageCode.VALID_FAIL));
  }

}
