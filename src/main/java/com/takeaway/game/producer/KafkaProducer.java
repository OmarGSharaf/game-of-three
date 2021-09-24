package com.takeaway.game.producer;

import com.takeaway.game.model.Message;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class KafkaProducer {

    @Autowired
    private KafkaTemplate<String, Message> kafkaTemplate;

    public void send(String topic, String key, Message message) {
        log.info("## Sending payload={} to topic={} on partition=0", message.toString(), topic);
        kafkaTemplate.send(topic, 0, key, message);
    }

    public void send(String topic, int partition, String key, Message message) {
        log.info("## Sending payload={} to topic={} on partition={}", message.toString(), topic, partition);
        System.out.printf("## Sending payload=%s to topic=%s on partition=%s\n", message.toString(), topic, partition);
        kafkaTemplate.send(topic, partition, key, message);
    }
}