package cm.example.rabbitmqhw.service;

import cm.example.rabbitmqhw.MyMessage;
import cm.example.rabbitmqhw.repository.DeadLetterEntity;
import cm.example.rabbitmqhw.repository.DeadLetterStorageRepository;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeadLetterStorageService {

    private final DeadLetterStorageRepository deadLetterStorageRepository;

    public void save(MyMessage message,String messageId, String routingKey){
        val deadLetterEntity = new DeadLetterEntity();
        deadLetterEntity.setMessageId(messageId);
        deadLetterEntity.setMessage(message);
        deadLetterEntity.setRoutingKey(routingKey);
        deadLetterStorageRepository.save(deadLetterEntity);
    }
}
