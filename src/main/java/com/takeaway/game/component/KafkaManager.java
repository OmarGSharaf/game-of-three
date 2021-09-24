package com.takeaway.game.component;

import org.apache.kafka.common.TopicPartition;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class KafkaManager {

    private final KafkaListenerEndpointRegistry registry;

    public KafkaManager(KafkaListenerEndpointRegistry registry) {
        this.registry = registry;
    }

    public void pauseAll() {
        registry.getListenerContainers().forEach(MessageListenerContainer::pause);
    }

    public void startAll() {
        registry.getListenerContainers().forEach(MessageListenerContainer::start);
    }

    public void pause(String id) {
        registry.getListenerContainer(id).pause();
    }

    public void start(String id) {
        registry.getListenerContainer(id).start();
    }

    public void setTopicPartition(String id, String topic, int partition) {
        clearTopicPartitions(id);
        TopicPartition topicPartition = new TopicPartition(topic, partition);
        Objects.requireNonNull(registry.getListenerContainer(id).getAssignedPartitions()).add(topicPartition);
    }

    public void clearTopicPartitions(String id) {
        Objects.requireNonNull(registry.getListenerContainer(id).getAssignedPartitions()).clear();
    }
}