package com.flab.planb.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.flab.planb.message.ResponseMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
@Component
@Slf4j
public class ResponseWriter {

    private final ObjectMapper objectMapper;

    public void writer(
        HttpServletResponse response,
        HttpStatus httpStatus,
        String message,
        Map<String, ?> data
    ) throws IOException {
        setResponse(response, httpStatus);
        objectMapper.registerModule(new JavaTimeModule())
                    .writeValue(
                        response.getWriter(),
                        ResponseMessage.builder().statusMessage(message).data(data).build()
                    );
    }

    public void writer(
        HttpServletResponse response,
        HttpStatus httpStatus,
        Map<String, ?> data
    ) throws IOException {
        setResponse(response, httpStatus);
        objectMapper.registerModule(new JavaTimeModule())
                    .writeValue(response.getWriter(), ResponseMessage.builder().data(data).build());
    }

    public void writer(HttpServletResponse response, HttpStatus httpStatus) throws IOException {
        setResponse(response, httpStatus);
        objectMapper.registerModule(new JavaTimeModule())
                    .writeValue(response.getWriter(), ResponseMessage.builder().build());
    }

    private void setResponse(HttpServletResponse response, HttpStatus httpStatus) {
        response.setContentType(MediaType.APPLICATION_JSON.toString());
        response.setCharacterEncoding(MessageLookup.ENCODIG);
        response.setStatus(httpStatus.value());
    }

}
