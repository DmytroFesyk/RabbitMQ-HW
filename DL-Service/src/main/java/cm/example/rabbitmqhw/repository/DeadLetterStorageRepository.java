package cm.example.rabbitmqhw.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeadLetterStorageRepository extends MongoRepository<DeadLetterEntity, String> {
    List<DeadLetterEntity> findByRoutingKey(String routingKey);
    List<DeadLetterEntity> findByMessageIdIn(List<String> messageIds);
}
