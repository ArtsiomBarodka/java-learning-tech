package com.epam.app.service;

import com.grpc.user.User;
import org.springframework.lang.NonNull;

import java.util.List;

public interface UserService {
    @NonNull User getUserById(@NonNull Long id);

    @NonNull List<User> getAllUser();
}
