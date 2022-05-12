package com.example.rabbitmqhw.controller;

import cm.example.rabbitmqhw.MyMessage;
import cm.example.rabbitmqhw.MyMessageResponse;
import com.example.rabbitmqhw.rabbit.Producer;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MessageController {

    private final Producer producer;

    @PostMapping("/messages")
    @ResponseStatus(HttpStatus.OK)
    public MyMessageResponse sendMessage(@RequestBody MyMessage message) {
        return producer.send(message);
    }

}
