package com.flab.planb.common;

import com.flab.planb.message.MessageSet;
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
        log.error("유효성 검증 실패 URI : " + request.getRequestURI());
        exception.getBindingResult().getAllErrors()
                 .forEach(error -> log.error(error.getDefaultMessage()));

        return ResponseEntity.badRequest().body(
            ResponseMessage.builder()
                           .statusMessage(
                               messageLookup.getMessage(MessageSet.VALID_FAIL.getLookupKey())
                           ).data(Map.of("errorCode", MessageSet.VALID_FAIL.getCode()))
                           .build()
        );
    }

}
