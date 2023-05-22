package com.epam.app.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiExceptionResponse(String path,
                                   HttpStatus statusCode,
                                   String message,
                                   String caused,
                                   int errorCode,
                                   @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                   LocalDateTime localDateTime) {
    public ApiExceptionResponse(String path, HttpStatus statusCode, String message, int errorCode, @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime localDateTime) {
        this(path, statusCode, message, null, errorCode, localDateTime);
    }
}
