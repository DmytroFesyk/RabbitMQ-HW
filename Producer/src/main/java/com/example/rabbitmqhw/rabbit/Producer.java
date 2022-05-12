package com.example.rabbitmqhw.rabbit;

import cm.example.rabbitmqhw.MyMessage;
import cm.example.rabbitmqhw.MyMessageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.stereotype.Service;

import static cm.example.rabbitmqhw.config.Config.MAIN_ROUTING_KEY;

@Service
@RequiredArgsConstructor
@Slf4j
public class Producer {

    private final AmqpTemplate template;
    private final MessageConverter jsonConverter;

    public MyMessageResponse send(MyMessage inputMessage) {
        val message = jsonConverter.toMessage(inputMessage, new MessageProperties());
        template.send(
                MAIN_ROUTING_KEY,
                MAIN_ROUTING_KEY,
                message
        );
        val response = new MyMessageResponse(message.getMessageProperties().getMessageId());
        log.info("Message was sent with id: " + response.getMessageId());
        return response;
    }
}
