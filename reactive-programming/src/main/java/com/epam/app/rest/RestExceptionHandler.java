package com.epam.app.rest;

import com.epam.app.exception.OperationException;
import com.epam.app.exception.ResourceNotFoundException;
import com.epam.app.model.response.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class RestExceptionHandler {
    private static final String RESOURCE_NOT_FOUND_EXCEPTION = "Resource is not found!";
    private static final String OPERATION_EXCEPTION = "Operation is failed";
    private static final String OTHER_ERRORS_EXCEPTION = "Server is not working now!";

    @ExceptionHandler(value = ResourceNotFoundException.class)
    protected ResponseEntity<ExceptionResponse> handleResourceNotFoundExceptions(ResourceNotFoundException ex, ServerHttpRequest serverHttpRequest) {
        return buildExceptionResponse(
                serverHttpRequest.getURI().toString(),
                HttpStatus.NOT_FOUND,
                RESOURCE_NOT_FOUND_EXCEPTION,
                ex.getMessage());
    }

    @ExceptionHandler(value = OperationException.class)
    protected ResponseEntity<ExceptionResponse> handleOperationExceptions(OperationException ex, ServerHttpRequest serverHttpRequest) {
        return buildExceptionResponse(
                serverHttpRequest.getURI().toString(),
                HttpStatus.BAD_REQUEST,
                OPERATION_EXCEPTION,
                ex.getMessage());
    }

    @ExceptionHandler(value = Exception.class)
    protected ResponseEntity<ExceptionResponse> handleOtherExceptions(Exception ex, ServerHttpRequest serverHttpRequest) {
        return buildExceptionResponse(
                serverHttpRequest.getURI().toString(),
                HttpStatus.INTERNAL_SERVER_ERROR,
                OTHER_ERRORS_EXCEPTION,
                ex.getMessage());
    }

    private ResponseEntity<ExceptionResponse> buildExceptionResponse(String path,
                                                                     HttpStatus statusCode,
                                                                     String message,
                                                                     String caused) {
        var exceptionResponse = new ExceptionResponse(path, statusCode, message, caused, statusCode.value(), LocalDateTime.now());
        return new ResponseEntity<>(exceptionResponse, statusCode);
    }
}
