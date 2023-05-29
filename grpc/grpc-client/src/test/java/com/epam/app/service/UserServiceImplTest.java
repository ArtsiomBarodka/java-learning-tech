package com.epam.app.service;

import com.grpc.user.Gender;
import com.grpc.user.GetAllUserRequest;
import com.grpc.user.GetAllUserResponse;
import com.grpc.user.GetUserByIdRequest;
import com.grpc.user.GetUserByIdResponse;
import com.grpc.user.User;
import com.grpc.user.UserServiceGrpc;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.client.autoconfigure.GrpcClientAutoConfiguration;
import net.devh.boot.grpc.server.autoconfigure.GrpcServerAutoConfiguration;
import net.devh.boot.grpc.server.autoconfigure.GrpcServerFactoryAutoConfiguration;
import net.devh.boot.grpc.server.service.GrpcService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(
        properties = {
                "grpc.server.inProcessName=test", // Enable inProcess server
                "grpc.server.port=-1", // Disable external server
                "grpc.client.user.address=in-process:test" // Configure the client to connect to the inProcess server
        },
        classes = {UserServiceImplTest.TestConfig.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
// Ensures that the grpc-server is properly shutdown after each test
// Avoids "port already in use" during tests
public class UserServiceImplTest {
    private static final String FIRST_NAME = "FirstName";
    private static final String LAST_NAME = "LastName";
    private static final String EMAIL = "Email";
    private static final int AGE = 25;
    private static final Gender GENDER = Gender.OTHER;

    @Autowired
    private UserService userService;

    @DisplayName("Get all users test")
    @Test
    void getAllUserTest() {

        var actualList = userService.getAllUser();
        assertNotNull(actualList);
        assertFalse(actualList.isEmpty());
        assertEquals(1, actualList.size());

        var actualItem = actualList.get(0);

        assertEquals(1L, actualItem.getId());
        assertEquals(FIRST_NAME, actualItem.getFirstName());
        assertEquals(LAST_NAME, actualItem.getLastName());
        assertEquals(EMAIL, actualItem.getEmail());
        assertEquals(AGE, actualItem.getAge());
        assertEquals(GENDER, actualItem.getGender());
    }

    @DisplayName("Get User by id test")
    @Test
    void getUserByIdTest() {
        var id = 1L;

        var actual = userService.getUserById(id);
        assertNotNull(actual);

        assertEquals(id, actual.getId());
        assertEquals(FIRST_NAME, actual.getFirstName());
        assertEquals(LAST_NAME, actual.getLastName());
        assertEquals(EMAIL, actual.getEmail());
        assertEquals(AGE, actual.getAge());
        assertEquals(GENDER, actual.getGender());
    }

    @Configuration
    @ImportAutoConfiguration({
            GrpcServerAutoConfiguration.class, // Create required server beans
            GrpcServerFactoryAutoConfiguration.class, // Select server implementation
            GrpcClientAutoConfiguration.class}) // Support @GrpcClient annotation
    static class TestConfig {
        @Bean
        public UserService userService() {
            return new UserServiceImpl();
        }

        @GrpcService
        static class UserClientForTest extends UserServiceGrpc.UserServiceImplBase {
            @Override
            public void getUserById(GetUserByIdRequest request, StreamObserver<GetUserByIdResponse> responseObserver) {
                var reply = GetUserByIdResponse.newBuilder()
                        .setUser(createUser(request.getId()))
                        .build();

                responseObserver.onNext(reply);
                responseObserver.onCompleted();
            }

            @Override
            public void getAllUser(GetAllUserRequest request, StreamObserver<GetAllUserResponse> responseObserver) {
                var reply = GetAllUserResponse.newBuilder()
                        .addAllUsers(List.of(createUser(1L)))
                        .build();

                responseObserver.onNext(reply);
                responseObserver.onCompleted();
            }

            private User createUser(Long id) {
                return User.newBuilder()
                        .setId(id)
                        .setFirstName(FIRST_NAME)
                        .setLastName(LAST_NAME)
                        .setEmail(EMAIL)
                        .setAge(AGE)
                        .setGender(GENDER)
                        .build();
            }
        }
    }
}

