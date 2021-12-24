package com.flab.planb.common;

import com.flab.planb.message.MessageCode;
import com.flab.planb.message.ResponseMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@RestControllerAdvice
@Slf4j
public class ExceptionAdvice {

    private final MessageLookup messageLookup;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> methodValidException(
        MethodArgumentNotValidException exception,
        HttpServletRequest request
    ) {
        log.error("유효성 검증 실패 URI : " + request.getRequestURI());
        exception.getBindingResult().getAllErrors()
                 .forEach(error -> log.error(error.getDefaultMessage()));

        return ResponseEntity.badRequest().body(
            ResponseMessage.builder()
                           .statusMessage(
                               messageLookup.getMessage(MessageCode.VALID_FAIL.getMessageKey())
                           ).data(Map.of("errorCode", MessageCode.VALID_FAIL.getMessageCode()))
                           .build()
        );
    }

}
