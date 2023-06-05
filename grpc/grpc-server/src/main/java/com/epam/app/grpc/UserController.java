package com.epam.app.grpc;

import com.epam.app.model.entity.UserEntity;
import com.epam.app.service.UserService;
import com.grpc.user.GetAllUserRequest;
import com.grpc.user.GetAllUserResponse;
import com.grpc.user.GetUserByIdRequest;
import com.grpc.user.GetUserByIdResponse;
import com.grpc.user.User;
import com.grpc.user.UserServiceGrpc;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
@RequiredArgsConstructor
public class UserController extends UserServiceGrpc.UserServiceImplBase {
    private final UserService userService;

    @Override
    public void getUserById(GetUserByIdRequest request, StreamObserver<GetUserByIdResponse> responseObserver) {
        var user = userService.getUserById(request.getId());

        var reply = GetUserByIdResponse.newBuilder()
                .setUser(toUserResponse(user))
                .build();

        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }

    @Override
    public void getAllUser(GetAllUserRequest request, StreamObserver<GetAllUserResponse> responseObserver) {
        var users = userService.getAllUser()
                .stream()
                .map(this::toUserResponse)
                .toList();

        var reply = GetAllUserResponse.newBuilder()
                .addAllUsers(users)
                .build();

        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }

    private User toUserResponse(UserEntity userEntity) {
        return User.newBuilder()
                .setId(userEntity.getId())
                .setFirstName(userEntity.getFirstName())
                .setLastName(userEntity.getLastName())
                .setEmail(userEntity.getEmail())
                .setAge(userEntity.getAge())
                .setGender(userEntity.getGender())
                .build();
    }
}
