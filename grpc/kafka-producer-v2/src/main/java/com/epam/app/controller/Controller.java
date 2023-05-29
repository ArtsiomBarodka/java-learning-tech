package com.epam.app.controller;

import com.avro.EnumType;
import com.avro.User;
import com.epam.app.model.UserSendRequest;
import com.epam.app.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/kafka")
@RequiredArgsConstructor
public class Controller {
    private final NotificationService notificationService;

    @PostMapping("/notify")
    public ResponseEntity<?> sendUserNotification(@RequestBody UserSendRequest userSendRequest) {
        var user = User.newBuilder()
                .setId(userSendRequest.getId())
                .setFirstName(userSendRequest.getFirstName())
                .setLastName(userSendRequest.getLastName())
                .setEmail(userSendRequest.getEmail())
                .setAge(userSendRequest.getAge())
                .setGender(EnumType.valueOf(userSendRequest.getGender()))
                .build();

        notificationService.notify(user);

        return ResponseEntity.ok().build();
    }
}
