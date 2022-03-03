package com.flab.planb.common;

import com.flab.planb.message.MessageSet;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import javax.servlet.http.HttpServletRequest;

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

        return responseEntityBuilder.get(HttpStatus.BAD_REQUEST, MessageSet.VALID_FAIL);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> methodValidException(IllegalArgumentException exception,
                                                  HttpServletRequest request) {
        log.error("IllegalArgumentException URI : {} | message : {}", request.getRequestURI(), exception.getMessage());

        return responseEntityBuilder.get(HttpStatus.BAD_REQUEST, MessageSet.VALID_FAIL);
    }

}
