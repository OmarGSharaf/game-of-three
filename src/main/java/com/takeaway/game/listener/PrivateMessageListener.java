package com.takeaway.game.listener;

import com.takeaway.game.component.Game;
import com.takeaway.game.component.KafkaManager;
import com.takeaway.game.model.Message;
import com.takeaway.game.type.MessageType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

@Log4j2
@Data
@EqualsAndHashCode(callSuper = true)
@Component
public class PrivateMessageListener extends AbstractKafkaListener {

    private static final String KAFKA_LISTENER_ID = "private-listener";

    @Value(value = "${kafka.topic}")
    private String topic;

    @Autowired
    private Game game;

    @Autowired
    private KafkaManager kafkaManager;

    private Enum<MessageType> messageType = MessageType.SYN_ACK;

    @KafkaListener(id = KAFKA_LISTENER_ID, autoStartup = "false", topics = "${kafka.topic}")
    public void listen(@Payload Message message,
                       @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) String key,
                       @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition,
                       Acknowledgment acknowledgment) {
        log.info("## receiving private message='{}'", message.toString());

        System.out.println("##########################");
        System.out.println(message);
        System.out.println(messageType);
        System.out.println("##########################");

        if (ObjectUtils.nullSafeEquals(messageType, message.getType())) {
            log.info("## consuming private message='{}'", message.toString());

            payload = message;
            latch.countDown();
        }
        acknowledgment.acknowledge();
    }

    @Override
    public void start() {
        resetLatch();
        kafkaManager.setTopicPartition(KAFKA_LISTENER_ID, topic, game.getId());
        kafkaManager.start(KAFKA_LISTENER_ID);
    }

    @Override
    public void stop() {
        kafkaManager.pause(KAFKA_LISTENER_ID);
    }

}