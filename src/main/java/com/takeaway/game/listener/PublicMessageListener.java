package com.takeaway.game.listener;

import com.takeaway.game.component.Player;
import com.takeaway.game.component.KafkaManager;
import com.takeaway.game.model.Message;
import com.takeaway.game.type.MessageType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
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
public class PublicMessageListener extends AbstractKafkaListener {

    private static final String KAFKA_LISTENER_ID = "public-listener";

    @Autowired
    private Player player;

    @Autowired
    private KafkaManager kafkaManager;

    @KafkaListener(id = KAFKA_LISTENER_ID, autoStartup = "false",
            topicPartitions = @TopicPartition(topic = "${kafka.topic}", partitions = {"0"}))
    public void listen(@Payload Message message,
                       @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) String key,
                       @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition,
                       Acknowledgment acknowledgment) {
        log.info("## receiving public message='{}'", message.toString());

        if (ObjectUtils.nullSafeEquals(MessageType.SYN, message.getType()) && !ObjectUtils.nullSafeEquals(key, player.getId())) {
            log.info("## consuming private message='{}'", message.toString());

            payload = message;
            latch.countDown();
            acknowledgment.acknowledge();
        }
    }

    @Override
    public void start() {
        resetLatch();
        kafkaManager.start(KAFKA_LISTENER_ID);
    }

    @Override
    public void stop() {
        kafkaManager.pause(KAFKA_LISTENER_ID);
    }
}