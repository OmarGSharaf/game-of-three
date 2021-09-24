package com.takeaway.game;

import com.takeaway.game.listener.PrivateMessageListener;
import com.takeaway.game.listener.PublicMessageListener;
import com.takeaway.game.model.Message;
import com.takeaway.game.producer.KafkaProducer;
import com.takeaway.game.type.MessageType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;

import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest
@DirtiesContext
@EmbeddedKafka(partitions = 1, brokerProperties = {"listeners=PLAINTEXT://localhost:9092", "port=9092"})
class KafkaTest {

    @Autowired
    private PrivateMessageListener privateMessageListener;

    @Autowired
    private PublicMessageListener publicMessageListener;

    @Autowired
    private KafkaProducer producer;

    @Value("${kafka.topic}")
    private String topic;

    @Test
    public void givenPublicMessageListener_whenSendingMessageToPartition0_thenMessageReceived() throws Exception {
        System.out.println("## START OF PUBLIC TEST");
        publicMessageListener.start();
        producer.send(topic, 0, "test", Message.builder()
                .content("test public message")
                .type(MessageType.SYN)
                .build());
        publicMessageListener.getLatch().await(10000, TimeUnit.MILLISECONDS);
        publicMessageListener.stop();
        System.out.println("## END OF PUBLIC TEST");
        assertThat(publicMessageListener.getLatch().getCount(), equalTo(0L));
        assertThat(publicMessageListener.getPayload().getContent(), containsString("test public message"));
    }

    @Test
    public void givenPrivateMessageListener_whenSendingMessageToPartition2_thenMessageReceived() throws Exception {
        System.out.println("## START OF PRIVATE TEST");
        privateMessageListener.start();
        producer.send(topic, privateMessageListener.getGame().getId(), "test", Message.builder()
                .content("test private message")
                .type(MessageType.SYN)
                .build());
        privateMessageListener.getLatch().await(10000, TimeUnit.MILLISECONDS);
        privateMessageListener.stop();
        System.out.println("## END OF PRIVATE TEST");
        assertThat(privateMessageListener.getLatch().getCount(), equalTo(0L));
        assertThat(privateMessageListener.getPayload().getContent(), containsString("test private message"));
    }
}