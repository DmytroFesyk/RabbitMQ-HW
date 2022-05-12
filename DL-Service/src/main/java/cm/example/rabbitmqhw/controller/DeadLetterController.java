package cm.example.rabbitmqhw.controller;

import cm.example.rabbitmqhw.MyMessageRetryResponse;
import cm.example.rabbitmqhw.repository.DeadLetterEntity;
import cm.example.rabbitmqhw.repository.DeadLetterStorageRepository;
import cm.example.rabbitmqhw.service.DeadLetterRetryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Slf4j
public class DeadLetterController {

    private final DeadLetterStorageRepository deadLetterStorageRepository;

    private final DeadLetterRetryService deadLetterRetryService;

    @GetMapping("/messages")
    @ResponseStatus(HttpStatus.OK)
    public List<DeadLetterEntity> getMessages(@RequestParam(name = "routingKey") String routingKey) {
        return deadLetterStorageRepository.findByRoutingKey(routingKey);
    }

    @PostMapping("/messages/retry")
    @ResponseStatus(HttpStatus.OK)
    public List<MyMessageRetryResponse> replyMessageAndDeleteFromStorage(@RequestBody List<String> messageIds) {

        val myMessageRetryResponseList = deadLetterRetryService.retry(messageIds);

        log.info("Messages have been resent: "
                + myMessageRetryResponseList
                .stream()
                .map(
                        myMessageRetryResponse ->
                                "message id [" + myMessageRetryResponse.getMessageId()
                                        + "] routing key [" + myMessageRetryResponse.getFailedRoutingKey()
                                        + "] was send with new message id[" + myMessageRetryResponse.getNewMessageId() + "]"
                )
                .collect(Collectors.joining(", "))
        );
        return myMessageRetryResponseList;
    }
}
