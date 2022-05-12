package cm.example.rabbitmqhw;

import lombok.Data;

@Data
public class MyMessage {
    private String user;
    private String message;
}
