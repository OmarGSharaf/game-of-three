package com.takeaway.game.producer;

import com.takeaway.game.model.Message;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Log
@Component
public class KafkaProducer {

    @Autowired
    private KafkaTemplate<String, Message> kafkaTemplate;

    public void send(String topic, String key, Message message) {
        log.info(String.format("==> Sending payload='{%s}' to topic='%s'", message.toString(), topic));
        kafkaTemplate.send(topic, key, message);
    }
}