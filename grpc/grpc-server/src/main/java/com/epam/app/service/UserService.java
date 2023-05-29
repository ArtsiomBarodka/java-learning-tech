package com.epam.app.service;

import com.epam.app.model.entity.UserEntity;
import org.springframework.lang.NonNull;

import java.util.List;

public interface UserService {
    @NonNull UserEntity getUserById(@NonNull Long id);

    @NonNull List<UserEntity> getAllUser();
}
