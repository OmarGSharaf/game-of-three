package com.takeaway.game.component;

import com.takeaway.game.listener.PrivateMessageListener;
import com.takeaway.game.listener.PublicMessageListener;
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
public class KafkaConnectionManager implements ConnectionManager {

    @Autowired
    private PublicMessageListener publicMessageListener;

    @Autowired
    private PrivateMessageListener privateMessageListener;

    @Autowired
    private KafkaProducer producer;

    @Value(value = "${kafka.topic}")
    private String topic;

    private String me;

    private String opponent;

    private boolean player1;

    public boolean connect(String me) {
        this.me = me;

        try {
            if (interceptSyncMessage()) {
                System.out.println("==> [SYN] message = " + publicMessageListener.getPayload());

                acknowledgeSyncMessage(me, publicMessageListener.getPayload().getSender());
                System.out.println("<== [SYN_ACK] message");

                if (interceptAckMessage()) {
                    System.out.println("==> [ACK] message = " + privateMessageListener.getPayload());

                    opponent = privateMessageListener.getPayload().getSender();
                    player1 = false;

                    return true;
                }
            }
        } catch (InterruptedException e) {
            log.error(e.getMessage());
        }

        try {
            broadcastSyncMessage(me);
            System.out.println("<== [SYNC] message");

            if(interceptSyncAckMessage()) {
                System.out.println("==> [SYNC_ACK] message = " + privateMessageListener.getPayload());

                sendAckMessage(me, privateMessageListener.getPayload().getSender());
                System.out.println("<== [ACK] message");

                opponent = privateMessageListener.getPayload().getSender();
                player1 = true;

                return true;
            }
        } catch (InterruptedException e) {
            log.error(e.getMessage());
        }

        System.out.println("\n## Synchronization failed!");
        System.out.println("## Retry...\n");

        return connect(me);
    }

    @Override
    public void send(Integer message) {
        producer.send(topic, Integer.parseInt(opponent), me, Message.builder()
                .content(message)
                .sender(me)
                .receiver(opponent)
                .type(MessageType.DEFAULT)
                .build());
    }

    @Override
    public Integer receive() {
        try {
            privateMessageListener.start();
            privateMessageListener.setMessageType(MessageType.DEFAULT);
            privateMessageListener.getLatch().await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return privateMessageListener.getPayload().getContent();
    }

    @Override
    public boolean isPLayer1() {
        return player1;
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
        privateMessageListener.getLatch().await(5000, TimeUnit.MILLISECONDS);

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
