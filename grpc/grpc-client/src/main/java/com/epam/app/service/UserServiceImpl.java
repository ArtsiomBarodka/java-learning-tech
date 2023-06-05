package com.epam.app.service;

import com.grpc.user.GetAllUserRequest;
import com.grpc.user.GetUserByIdRequest;
import com.grpc.user.User;
import com.grpc.user.UserServiceGrpc;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    @GrpcClient("user")
    private UserServiceGrpc.UserServiceBlockingStub userServiceClient;

    @Override
    public @NonNull User getUserById(@NonNull Long id) {
        log.info("Getting user with id = {}", id);
        var user = userServiceClient.getUserById(GetUserByIdRequest
                        .newBuilder()
                        .setId(id)
                        .build())
                .getUser();

        log.info("User with (id = {}) is received. User: {}", id, user);

        return user;
    }

    @Override
    public List<User> getAllUser() {
        log.info("Getting all users");
        var usersList = userServiceClient.getAllUser(GetAllUserRequest
                        .getDefaultInstance())
                .getUsersList();

        log.info("All users are received. Users: {}", usersList);

        return usersList;
    }
}
