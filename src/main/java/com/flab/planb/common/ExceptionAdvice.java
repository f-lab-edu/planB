package com.flab.planb.common;

import com.flab.planb.message.MessageCode;
import com.flab.planb.message.ResponseMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RequiredArgsConstructor
@RestControllerAdvice
@Slf4j
public class ExceptionAdvice {

    private final MessageLookup messageLookup;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> methodValidException(MethodArgumentNotValidException exception,
                                                  HttpServletRequest request) {
        exception.getBindingResult().getAllErrors()
                 .forEach(error -> log.error(
                     "MethodArgumentNotValidException URI : {} | message : {}",
                     request.getRequestURI(), error.getDefaultMessage()));

        return ResponseEntity.badRequest().body(
            ResponseMessage.builder()
                           .statusMessage(
                               messageLookup.getMessage(MessageCode.VALID_FAIL.getKey())
                           ).data(Map.of("errorCode", MessageCode.VALID_FAIL.getValue()))
                           .build()
        );
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> methodValidException(IllegalArgumentException exception,
                                                  HttpServletRequest request) {
        log.error("IllegalArgumentException URI : {} | message : {}", request.getRequestURI(), exception.getMessage());

        return ResponseEntity.badRequest().body(
            ResponseMessage.builder()
                           .statusMessage(
                               messageLookup.getMessage(MessageCode.VALID_FAIL.getKey())
                           ).data(Map.of("errorCode", MessageCode.VALID_FAIL.getValue()))
                           .build()
        );
    }

}
