package com.epam.app.service;

import com.epam.app.model.entity.UserEntity;
import com.epam.app.model.exception.ObjectNotFoundException;
import com.epam.app.repository.UserRepository;
import com.grpc.user.Gender;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @DisplayName("Get User by id test")
    @Test
    void getUserByIdTest() {
        var id = 1L;
        var expected = new UserEntity(id, "FirstName", "LastName", "Email", 25, Gender.OTHER);

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(expected));

        var actual = userService.getUserById(id);

        assertNotNull(actual);
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getFirstName(), actual.getFirstName());
        assertEquals(expected.getLastName(), actual.getLastName());
        assertEquals(expected.getEmail(), actual.getEmail());
        assertEquals(expected.getAge(), actual.getAge());
        assertEquals(expected.getGender(), actual.getGender());
    }

    @DisplayName("Get User by id test. User is not found")
    @Test
    void getUserByIdTest_UserIsNotFound() {
        var id = 1L;

        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> userService.getUserById(id));
    }

    @DisplayName("Get all Users test")
    @Test
    void getAllUserTest() {
        var expected = new UserEntity(1L, "FirstName", "LastName", "Email", 25, Gender.OTHER);

        when(userRepository.findAll()).thenReturn(List.of(expected));

        var actualList = userService.getAllUser();

        assertFalse(actualList.isEmpty());
        assertEquals(1, actualList.size());

        var actualItem = actualList.get(0);
        assertEquals(expected.getId(), actualItem.getId());
        assertEquals(expected.getFirstName(), actualItem.getFirstName());
        assertEquals(expected.getLastName(), actualItem.getLastName());
        assertEquals(expected.getEmail(), actualItem.getEmail());
        assertEquals(expected.getAge(), actualItem.getAge());
        assertEquals(expected.getGender(), actualItem.getGender());
    }

    @DisplayName("Get all Users test. Users are not found")
    @Test
    void getAllUserTest_UsersAreNotFound() {
        when(userRepository.findAll()).thenReturn(Collections.emptyList());

        assertThrows(ObjectNotFoundException.class, () -> userService.getAllUser());
    }
}
