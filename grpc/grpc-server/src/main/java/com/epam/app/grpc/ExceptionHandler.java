package com.epam.app.grpc;

import com.epam.app.model.exception.ObjectNotFoundException;
import com.google.protobuf.Any;
import com.google.protobuf.Timestamp;
import com.google.rpc.Code;
import com.google.rpc.Status;
import com.grpc.user.UserExceptionDetailsResponse;
import io.grpc.StatusRuntimeException;
import io.grpc.protobuf.StatusProto;
import net.devh.boot.grpc.server.advice.GrpcAdvice;
import net.devh.boot.grpc.server.advice.GrpcExceptionHandler;

import java.time.Instant;

@GrpcAdvice
public class ExceptionHandler {
    private final static String OBJECT_NOT_FOUND_EXCEPTION = "Object is not found!";
    private static final String OTHER_ERRORS_EXCEPTION = "Server side exception!";

    @GrpcExceptionHandler(ObjectNotFoundException.class)
    public StatusRuntimeException handleObjectNotFoundException(ObjectNotFoundException ex) {
        var exceptionResponse = UserExceptionDetailsResponse.newBuilder()
                .setTimestamp(getTimestamp())
                .setCaused(OBJECT_NOT_FOUND_EXCEPTION)
                .build();

        var status = Status.newBuilder()
                .setCode(Code.NOT_FOUND.getNumber())
                .setMessage(ex.getLocalizedMessage())
                .addDetails(Any.pack(exceptionResponse))
                .build();

        return StatusProto.toStatusRuntimeException(status);
    }

    @GrpcExceptionHandler(Exception.class)
    public StatusRuntimeException handleObjectNotFoundException(Exception ex) {
        var exceptionResponse = UserExceptionDetailsResponse.newBuilder()
                .setTimestamp(getTimestamp())
                .setCaused(OTHER_ERRORS_EXCEPTION)
                .build();

        var status = Status.newBuilder()
                .setCode(Code.INTERNAL.getNumber())
                .setMessage(ex.getLocalizedMessage())
                .addDetails(Any.pack(exceptionResponse))
                .build();

        return StatusProto.toStatusRuntimeException(status);
    }

    private Timestamp getTimestamp() {
        var time = Instant.now();
        return Timestamp.newBuilder()
                .setSeconds(time.getEpochSecond())
                .setNanos(time.getNano())
                .build();
    }
}
