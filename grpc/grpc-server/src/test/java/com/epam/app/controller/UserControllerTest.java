package com.epam.app.controller;

import com.epam.app.grpc.UserController;
import com.epam.app.model.entity.UserEntity;
import com.epam.app.service.UserService;
import com.grpc.user.Gender;
import com.grpc.user.GetAllUserRequest;
import com.grpc.user.GetUserByIdRequest;
import com.grpc.user.UserServiceGrpc;
import net.devh.boot.grpc.client.autoconfigure.GrpcClientAutoConfiguration;
import net.devh.boot.grpc.client.inject.GrpcClient;
import net.devh.boot.grpc.server.autoconfigure.GrpcServerAutoConfiguration;
import net.devh.boot.grpc.server.autoconfigure.GrpcServerFactoryAutoConfiguration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest(
        properties = {
                "grpc.server.inProcessName=test", // Enable inProcess server
                "grpc.server.port=-1", // Disable external server
                "grpc.client.inProcess.address=in-process:test" // Configure the client to connect to the inProcess server
        },
        classes = {UserControllerTest.TestConfig.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD) // Ensures that the grpc-server is properly shutdown after each test
// Avoids "port already in use" during tests
public class UserControllerTest {

    @GrpcClient("inProcess")
    private UserServiceGrpc.UserServiceBlockingStub userGrpcService;

    @MockBean
    private UserService userService;

    @DisplayName("Get User by id test")
    @Test
    void getUserByIdTest() {
        var id = 1L;
        var expected = new UserEntity(id, "FirstName", "LastName", "Email", 25, Gender.OTHER);
        var request = GetUserByIdRequest
                .newBuilder()
                .setId(id)
                .build();

        when(userService.getUserById(anyLong())).thenReturn(expected);

        var actualResponse = userGrpcService.getUserById(request);
        assertNotNull(actualResponse);

        var actualUser = actualResponse.getUser();
        assertNotNull(actualUser);
        assertEquals(expected.getId(), actualUser.getId());
        assertEquals(expected.getFirstName(), actualUser.getFirstName());
        assertEquals(expected.getLastName(), actualUser.getLastName());
        assertEquals(expected.getEmail(), actualUser.getEmail());
        assertEquals(expected.getAge(), actualUser.getAge());
        assertEquals(expected.getGender(), actualUser.getGender());
    }

    @DisplayName("Get all Users test")
    @Test
    void getAllUserTest() {
        var expected = new UserEntity(1L, "FirstName", "LastName", "Email", 25, Gender.OTHER);
        var request = GetAllUserRequest.getDefaultInstance();

        when(userService.getAllUser()).thenReturn(List.of(expected));

        var actualResponse = userGrpcService.getAllUser(request);
        assertNotNull(actualResponse);

        var actualUserList = actualResponse.getUsersList();
        assertNotNull(actualUserList);
        assertFalse(actualUserList.isEmpty());
        assertEquals(1, actualUserList.size());

        var actualUser = actualUserList.get(0);
        assertEquals(expected.getId(), actualUser.getId());
        assertEquals(expected.getFirstName(), actualUser.getFirstName());
        assertEquals(expected.getLastName(), actualUser.getLastName());
        assertEquals(expected.getEmail(), actualUser.getEmail());
        assertEquals(expected.getAge(), actualUser.getAge());
        assertEquals(expected.getGender(), actualUser.getGender());
    }

    @Configuration
    @ImportAutoConfiguration({
            GrpcServerAutoConfiguration.class, // Create required server beans
            GrpcServerFactoryAutoConfiguration.class, // Select server implementation
            GrpcClientAutoConfiguration.class}) // Support @GrpcClient annotation
    static class TestConfig {
        @Bean
        public UserService userService() {
            return mock(UserService.class);
        }

        @Bean
        public UserController userController(UserService userService) {
            return new UserController(userService);
        }
    }
}
