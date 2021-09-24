package com.takeaway.game.handler;

import com.takeaway.game.listener.PrivateMessageListener;
import com.takeaway.game.listener.PublicMessageListener;
import com.takeaway.game.model.Connection;
import com.takeaway.game.model.Message;
import com.takeaway.game.producer.KafkaProducer;
import com.takeaway.game.type.MessageType;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Log4j2
@Component
public class ConnectionHandler {

    @Value(value = "${kafka.topic}")
    private String topic;

    @Autowired
    private PublicMessageListener publicMessageListener;

    @Autowired
    private PrivateMessageListener privateMessageListener;

    @Autowired
    private KafkaProducer producer;

    public Connection handle(String id) {
        try {
            if (interceptSyncMessage()) {
                System.out.println("## intercepted [SYN] message, message = " + publicMessageListener.getPayload());
                acknowledgeSyncMessage(id, publicMessageListener.getPayload().getSender());
                System.out.println("## transmitted [SYN_ACK] message");
                if (interceptAckMessage()) {
                    System.out.println("## intercepted [ACK] message, message = " + publicMessageListener.getPayload());
                    return Connection.builder()
                            .player1(publicMessageListener.getPayload().getSender())
                            .player2(id)
                            .timestamp(System.currentTimeMillis())
                            .build();
                }
            }
        } catch (InterruptedException e) {
            log.error(e.getMessage());
        }

        try {
            broadcastSyncMessage(id);
            System.out.println("## transmitted [SYNC] message");
            if(interceptSyncAckMessage()) {
                System.out.println("## intercepted [SYNC_ACK] message, message = " + privateMessageListener.getPayload());
                sendAckMessage(id, privateMessageListener.getPayload().getSender());
                System.out.println("## transmitted [ACK] message");
                return Connection.builder()
                        .player1(id)
                        .player2(privateMessageListener.getPayload().getSender())
                        .timestamp(System.currentTimeMillis())
                        .build();
            }
        } catch (InterruptedException e) {
            log.error(e.getMessage());
        }

        return handle(id);
    }

    private boolean interceptSyncMessage() throws InterruptedException {
        publicMessageListener.start();
        publicMessageListener.getLatch().await(10000, TimeUnit.MILLISECONDS);
        publicMessageListener.stop();

        return publicMessageListener.getLatch().getCount() == 0L;
    }

    private void acknowledgeSyncMessage(String sender, String receiver) {
        producer.send(topic, Integer.parseInt(receiver), sender, Message.builder()
                .sender(sender)
                .receiver(receiver)
                .type(MessageType.SYN_ACK)
                .build());
    }

    private boolean interceptAckMessage() throws InterruptedException {
        privateMessageListener.resetLatch();
        privateMessageListener.setMessageType(MessageType.ACK);
        System.out.println("BEFORE");
        privateMessageListener.getLatch().await(5000, TimeUnit.MILLISECONDS);
        System.out.println("AFTER");
        return privateMessageListener.getLatch().getCount() == 0L;
    }

    private void broadcastSyncMessage(String sender) throws InterruptedException {
        producer.send(topic, sender, Message.builder()
                .sender(sender)
                .type(MessageType.SYN)
                .build());
    }

    private boolean interceptSyncAckMessage() throws InterruptedException {
        privateMessageListener.start();
        privateMessageListener.setMessageType(MessageType.SYN_ACK);
        privateMessageListener.getLatch().await(5000, TimeUnit.MILLISECONDS);

        return privateMessageListener.getLatch().getCount() == 0L;
    }

    private void sendAckMessage(String sender, String receiver) {
        producer.send(topic, Integer.parseInt(receiver), sender, Message.builder()
                .sender(sender)
                .receiver(receiver)
                .type(MessageType.ACK)
                .build());
    }
}
