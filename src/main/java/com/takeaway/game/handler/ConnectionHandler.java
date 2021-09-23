package com.takeaway.game.handler;

import com.takeaway.game.component.KafkaManager;
import com.takeaway.game.consumer.KafkaConsumer;
import com.takeaway.game.model.Connection;
import com.takeaway.game.model.Message;
import com.takeaway.game.producer.KafkaProducer;
import com.takeaway.game.type.MessageType;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@Log
@Component
public class ConnectionHandler {

    @Value(value = "${kafka.topic}")
    private String topic;

    @Autowired
    private KafkaManager kafkaManager;

    @Autowired
    private KafkaConsumer consumer;

    @Autowired
    private KafkaProducer producer;

    public Connection handle() {
        String id = generateId();

        consumer.setMyKey(id);
        consumer.setMessageType(MessageType.SYN);
        consumer.resetLatch();

        kafkaManager.start("1");

        try {
            consumer.getLatch().await(10000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            log.severe(e.getMessage());
        }

        if (consumer.getLatch().getCount() == 0L) {
            synAck(id, consumer.getPayload().getSender());

            consumer.setMessageType(MessageType.ACK);
            consumer.resetLatch();

            try {
                consumer.getLatch().await(10000, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                log.severe(e.getMessage());
            }

            if (consumer.getLatch().getCount() == 0L) {
                Message message = consumer.getPayload();
                return Connection.builder()
                        .player1(message.getSender())
                        .player2(id)
                        .timestamp(System.currentTimeMillis())
                        .build();
            }
        }

        consumer.setMessageType(MessageType.SYN_ACK);
        consumer.resetLatch();

        syn(id);

        try {
            consumer.getLatch().await(60000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            log.severe(e.getMessage());
        }

        if (consumer.getLatch().getCount() == 0L) {
            String sender = consumer.getPayload().getSender();

            ack(id, sender);

            return Connection.builder()
                    .player1(id)
                    .player2(sender)
                    .timestamp(System.currentTimeMillis())
                    .build();
        }
        return handle();
    }

    private void syn(String sender) {
        log.info(String.format(" ==> sending SYN message -> sender: %s", sender));
        producer.send(topic, sender, Message.builder()
                .sender(sender)
                .type(MessageType.SYN)
                .build());
    }

    private void synAck(String sender, String receiver) {
        log.info(String.format("==> sending SYN_ACK message -> sender: %s -> receiver: %s", sender, receiver));
        producer.send(topic, sender, Message.builder()
                .sender(sender)
                .receiver(receiver)
                .type(MessageType.SYN_ACK)
                .build());
    }

    private void ack(String sender, String receiver) {
        log.info(String.format("==> sending ACK message -> sender: %s -> receiver: %s", sender, receiver));
        producer.send(topic, sender, Message.builder()
                .sender(sender)
                .receiver(receiver)
                .type(MessageType.ACK)
                .build());
    }

    private String generateId() {
        return String.format("%d-%d", new Random().nextInt(10000), System.currentTimeMillis() / 1000);
    }
}
