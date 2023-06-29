package com.epam.app.rest;

import com.epam.app.model.DeveloperMessage;
import com.epam.app.producer.TopicTask2Producer;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TaskController {
    private final TopicTask2Producer producer;

    @PostMapping("/message")
    public void addMessage(@RequestBody DeveloperMessage message) {
        producer.produceMessage(message);
    }
}
