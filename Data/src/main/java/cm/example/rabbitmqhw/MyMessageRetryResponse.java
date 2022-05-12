package cm.example.rabbitmqhw;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MyMessageRetryResponse {
    private String messageId;
    private String failedRoutingKey;
    private String newMessageId;
}
