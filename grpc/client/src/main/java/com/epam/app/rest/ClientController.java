package com.epam.app.rest;

import com.epam.app.model.UserResponse;
import com.grpc.user.GetAllUserRequest;
import com.grpc.user.GetUserByIdRequest;
import com.grpc.user.User;
import com.grpc.user.UserServiceGrpc;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/client")
public class ClientController {
    @GrpcClient("user")
    private UserServiceGrpc.UserServiceBlockingStub userServiceClient;

    @GetMapping("/users/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        var user = userServiceClient.getUserById(GetUserByIdRequest
                .newBuilder()
                .setId(id)
                .build())
                .getUser();
        return ResponseEntity.ok(toUserResponse(user));
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        var users = userServiceClient.getAllUser(GetAllUserRequest.getDefaultInstance())
                .getUsersList()
                .stream()
                .map(this::toUserResponse)
                .toList();

        return ResponseEntity.ok(users);
    }

    private UserResponse toUserResponse(User user) {
        return new UserResponse(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getAge(), user.getGender().name());
    }
}
