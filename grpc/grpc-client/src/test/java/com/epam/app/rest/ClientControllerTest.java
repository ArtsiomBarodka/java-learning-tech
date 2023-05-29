package com.epam.app.rest;

import com.epam.app.service.UserService;
import com.grpc.user.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ClientController.class)
public class ClientControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserService userService;

    @DisplayName("Get User by id test")
    @Test
    void getUserByIdTest() throws Exception {
        var expected = User.newBuilder()
                .build();

        when(userService.getUserById(anyLong())).thenReturn(expected);

        mvc.perform(get("/api/v1/client/users/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @DisplayName("Get all users test")
    @Test
    void getAllUsersTest() throws Exception {
        var expected = List.of(User.newBuilder()
                .build());

        when(userService.getAllUser()).thenReturn(expected);

        mvc.perform(get("/api/v1/client/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
