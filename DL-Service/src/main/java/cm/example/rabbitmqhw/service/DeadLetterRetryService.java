package cm.example.rabbitmqhw.service;

import cm.example.rabbitmqhw.MyMessageRetryResponse;
import cm.example.rabbitmqhw.client.ProducerServiceClient;
import cm.example.rabbitmqhw.repository.DeadLetterStorageRepository;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DeadLetterRetryService {

    private final ProducerServiceClient producerServiceClient;

    private final DeadLetterStorageRepository deadLetterStorageRepository;

    public List<MyMessageRetryResponse> retry(List<String> messageIds) {

        val deadLetterEntityList = deadLetterStorageRepository.findByMessageIdIn(messageIds);
        val responseList = deadLetterEntityList
                .stream()
                .map(deadLetterEntity -> {
                            val response = producerServiceClient.retrySendMessage(deadLetterEntity.getMessage());
                            return new MyMessageRetryResponse(
                                    deadLetterEntity.getMessageId(),
                                    deadLetterEntity.getRoutingKey(),
                                    response.getMessageId()
                            );
                        }
                ).filter(
                        myMessageRetryResponse ->
                                myMessageRetryResponse.getNewMessageId() != null && !myMessageRetryResponse.getNewMessageId().isEmpty()
                ).toList();
        val successfulSendMessagesIdList = responseList.stream().map(MyMessageRetryResponse::getMessageId).toList();
        val successfulSendDeadLetterEntityList = deadLetterEntityList
                .stream()
                .filter(deadLetterEntity -> successfulSendMessagesIdList.contains(deadLetterEntity.getMessageId()))
                .toList();

        deadLetterStorageRepository.deleteAll(successfulSendDeadLetterEntityList);

        return responseList;
    }
}
