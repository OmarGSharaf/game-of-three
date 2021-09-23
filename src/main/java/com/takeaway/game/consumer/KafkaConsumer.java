package com.takeaway.game.consumer;

import com.takeaway.game.model.Message;
import com.takeaway.game.type.MessageType;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.concurrent.CountDownLatch;

@Log4j2
@Data
@Component
public class KafkaConsumer {

    private Enum<MessageType> messageType = MessageType.SYN;
    private CountDownLatch latch = new CountDownLatch(1);
    private Message payload = null;
    private String myKey;

    @KafkaListener(id = "1", topics = "${kafka.topic}", autoStartup = "false")
    public void listen(@Payload Message message,
                       @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) String key,
                       Acknowledgment acknowledgment) {
        log.info("==> receiving message='{}'", message.toString());
        if (ObjectUtils.nullSafeEquals(messageType, message.getType())
                && !ObjectUtils.nullSafeEquals(myKey, key)) {
            log.info("==> consuming message");
            payload = message;
            latch.countDown();
            acknowledgment.acknowledge();
        }
    }

    public void resetLatch() {
        latch = new CountDownLatch(1);
    }
}