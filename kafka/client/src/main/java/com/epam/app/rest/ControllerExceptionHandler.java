package com.epam.app.rest;

import com.epam.app.exception.ObjectNotFoundException;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {
    private static final String OBJECT_NOT_FOUND_EXCEPTION = "Object is not found!";
    private static final String OTHER_ERRORS_EXCEPTION = "Server can't proceed the request!";

    @ExceptionHandler(value = ObjectNotFoundException.class)
    protected ResponseEntity<Object> handleResourceNotFoundExceptions(ObjectNotFoundException ex) {
        final var exceptionResponse = new ExceptionResponse(HttpStatus.NOT_FOUND,
                HttpStatus.NOT_FOUND.value(),
                OBJECT_NOT_FOUND_EXCEPTION,
                ex.getMessage());

        return buildResponseEntity(exceptionResponse);
    }

    @ExceptionHandler(value = Exception.class)
    protected ResponseEntity<Object> handleALLExceptions() {
        final var exceptionResponse = new ExceptionResponse(HttpStatus.INTERNAL_SERVER_ERROR,
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                OTHER_ERRORS_EXCEPTION);

        return buildResponseEntity(exceptionResponse);
    }

    private ResponseEntity<Object> buildResponseEntity(ExceptionResponse exceptionResponse) {
        return new ResponseEntity<>(exceptionResponse, exceptionResponse.statusCode());
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private record ExceptionResponse(HttpStatus statusCode, int errorCode, String message, String caused) {
        public ExceptionResponse (HttpStatus statusCode, int errorCode, String message) {
            this(statusCode, errorCode, message, null);
        }
    }
}
