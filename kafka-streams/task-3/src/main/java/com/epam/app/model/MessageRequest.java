package com.epam.app.model;

import lombok.Data;

@Data
public class MessageRequest {
    private String topicOneMessage;
    private String topicTwoMessage;
}
