package com.epam.app.model;

public record UserResponse(long id, String firstname, String lastName, String email, int age, String gender) {
}
