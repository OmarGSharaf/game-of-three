//package com.takeaway.game;
//
//import com.takeaway.game.component.KafkaManager;
//import com.takeaway.game.model.Message;
//import com.takeaway.game.producer.KafkaProducer;
//import com.takeaway.game.consumer.KafkaConsumer;
//import com.takeaway.game.type.MessageType;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.kafka.test.context.EmbeddedKafka;
//import org.springframework.test.annotation.DirtiesContext;
//
//import java.util.concurrent.TimeUnit;
//
//import static org.hamcrest.MatcherAssert.assertThat;
//import static org.hamcrest.Matchers.containsString;
//import static org.hamcrest.Matchers.equalTo;
//
//@SpringBootTest
//@DirtiesContext
//@EmbeddedKafka(partitions = 1, brokerProperties = {"listeners=PLAINTEXT://localhost:9092", "port=9092"})
//class KafkaTest {
//
//    @Autowired
//    private KafkaConsumer consumer;
//
//    @Autowired
//    private KafkaProducer producer;
//
//    @Autowired
//    private KafkaManager kafkaManager;
//
//    @Value("${kafka.topic}")
//    private String topic;
//
//    @Test
//    public void givenKafkaBroker_whenSendingToProducer_thenMessageReceived() throws Exception {
//        consumer.setMyKey("-1");
//        consumer.setMessageType(MessageType.SYN);
//
//        kafkaManager.start("1");
//
//        producer.send(topic, "myKey", Message.builder()
//                .content("test message")
//                .type(MessageType.SYN)
//                .build());
//        consumer.getLatch().await(10000, TimeUnit.MILLISECONDS);
//
//        assertThat(consumer.getLatch().getCount(), equalTo(0L));
//        assertThat(consumer.getPayload().getContent(), containsString("test message"));
//    }
//}