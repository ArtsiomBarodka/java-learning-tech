package com.epam.app.service;

import com.epam.app.model.entity.UserEntity;
import com.epam.app.model.exception.ObjectNotFoundException;
import com.epam.app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;


    @Override
    public @NonNull UserEntity getUserById(@NonNull Long id) {
        var user = userRepository.findById(id).orElseThrow(() -> {
            log.warn("User with (id = {}) is not found", id);
            return new ObjectNotFoundException("Users with (id = %d) is not found".formatted(id));
        });

        log.info("User with (id = {}) is found. User: {}", id, user);

        return user;
    }

    @Override
    public @NonNull List<UserEntity> getAllUser() {
        var users = userRepository.findAll();

        if (users.isEmpty()) {
            log.warn("Users are not found");
            throw new ObjectNotFoundException("Users are not found");
        }

        log.info("All users are found. Users: {}", users);

        return users;
    }
}
