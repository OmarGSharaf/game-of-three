package com.takeaway.game.listener;

import com.takeaway.game.model.Message;
import lombok.Data;
import org.springframework.kafka.support.Acknowledgment;

import java.util.concurrent.CountDownLatch;

@Data
public abstract class AbstractKafkaListener {

    protected CountDownLatch latch = new CountDownLatch(1);
    protected Message payload = null;

    abstract void listen(Message message, String key, int partition, Acknowledgment acknowledgment);

    abstract void start();

    abstract void stop();

    public void resetLatch() {
        latch = new CountDownLatch(1);
    }
}
