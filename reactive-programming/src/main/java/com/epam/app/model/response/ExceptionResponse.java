package com.epam.app.model.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ExceptionResponse(String path,
                                HttpStatus statusCode,
                                String message,
                                String caused,
                                int errorCode,
                                @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime localDateTime) {

    public ExceptionResponse(String path, HttpStatus statusCode, String message, int errorCode, @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime localDateTime) {
        this(path, statusCode, message, null, errorCode, localDateTime);
    }
}
