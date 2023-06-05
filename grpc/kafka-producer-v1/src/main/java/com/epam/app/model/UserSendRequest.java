package com.epam.app.model;

import lombok.Data;

@Data
public class UserSendRequest {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private int age;
}
