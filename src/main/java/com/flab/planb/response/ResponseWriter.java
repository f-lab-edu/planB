package com.flab.planb.response;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flab.planb.response.message.MessageLookup;
import com.flab.planb.response.message.ResponseMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
@Component
@Slf4j
public class ResponseWriter {

    private final ObjectMapper objectMapper;

    public String messageToString(ResponseMessage data) throws IOException {
        return objectMapper.writeValueAsString(data);
    }

    public void setHeader(HttpServletResponse response, HttpStatus httpStatus) {
        response.setContentType(MediaType.APPLICATION_JSON.toString());
        response.setCharacterEncoding(MessageLookup.ENCODIG);
        response.setStatus(httpStatus.value());
    }

}
