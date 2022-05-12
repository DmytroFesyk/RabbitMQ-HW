package cm.example.rabbitmqhw.client;

import cm.example.rabbitmqhw.MyMessage;
import cm.example.rabbitmqhw.MyMessageResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(value = "producer", url = "${app.producer.url}")
public interface ProducerServiceClient {

    @PostMapping("/messages")
    MyMessageResponse retrySendMessage(MyMessage request);

}
