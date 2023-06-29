package com.epam.app.rest;

import com.epam.app.model.MessageRequest;
import com.epam.app.producer.TopicTask3Producer;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TaskController {
    private final TopicTask3Producer producer;

    @PostMapping("/message")
    public void addMessage(@RequestBody MessageRequest messageRequest) {
        producer.produceMessage(messageRequest);
    }
}
