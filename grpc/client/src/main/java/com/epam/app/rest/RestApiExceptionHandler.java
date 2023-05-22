package com.epam.app.rest;

import com.epam.app.model.ApiExceptionResponse;
import com.google.rpc.Code;
import com.google.rpc.Status;
import com.grpc.user.UserExceptionDetailsResponse;
import io.grpc.StatusRuntimeException;
import io.grpc.protobuf.StatusProto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@RestControllerAdvice
public class RestApiExceptionHandler {
    private static final String OTHER_ERRORS_EXCEPTION = "Server side exception!";

    @SneakyThrows
    @ExceptionHandler(value = StatusRuntimeException.class)
    protected ResponseEntity<ApiExceptionResponse> handleGrpcExceptions(StatusRuntimeException ex, HttpServletRequest request) {
        var status = StatusProto.fromThrowable(ex);
        if (status != null) {
            var userExceptionResponse = status.getDetailsList().stream()
                    .filter(i -> i.is(UserExceptionDetailsResponse.class))
                    .findFirst();

            if (userExceptionResponse.isPresent()) {
                var extractedResponse = userExceptionResponse.get().unpack(UserExceptionDetailsResponse.class);
                var timestamp = extractedResponse.getTimestamp();
                var caused = extractedResponse.getCaused();
                var localDatetime = Instant
                        .ofEpochSecond(timestamp.getSeconds(), timestamp.getNanos())
                        .atZone(ZoneId.systemDefault())
                        .toLocalDateTime();

                return buildApiExceptionResponse(request.getRequestURI(), getStatusCode(status), caused, status.getMessage(), localDatetime);
            }
        }

        return buildApiExceptionResponse(
                request.getRequestURI(),
                HttpStatus.INTERNAL_SERVER_ERROR,
                OTHER_ERRORS_EXCEPTION,
                ex.getMessage(),
                LocalDateTime.now());
    }

    @ExceptionHandler(value = Exception.class)
    protected ResponseEntity<ApiExceptionResponse> handleOtherExceptions(Exception ex, HttpServletRequest request) {
        return buildApiExceptionResponse(
                request.getRequestURI(),
                HttpStatus.INTERNAL_SERVER_ERROR,
                OTHER_ERRORS_EXCEPTION,
                null,
                LocalDateTime.now());
    }

    private HttpStatus getStatusCode(Status status) {
        if (status == null) {
            return HttpStatus.BAD_REQUEST;
        }

        var code = Code.forNumber(status.getCode());
        if (code == null) {
            return HttpStatus.BAD_REQUEST;
        }

        return switch (code) {
            case NOT_FOUND, INTERNAL -> HttpStatus.NOT_FOUND;
            default -> HttpStatus.BAD_REQUEST;
        };
    }

    private ResponseEntity<ApiExceptionResponse> buildApiExceptionResponse(String path,
                                                                           HttpStatus statusCode,
                                                                           String message,
                                                                           String caused,
                                                                           LocalDateTime localDateTime) {
        var exceptionResponse = new ApiExceptionResponse(path, statusCode, message, caused, statusCode.value(), localDateTime);
        return new ResponseEntity<>(exceptionResponse, statusCode);
    }
}
