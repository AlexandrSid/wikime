package org.aleksid.wikime.kafka;

import org.springframework.boot.SpringApplication;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;

@EnableKafka
public class ConsumerRun {
    @KafkaListener(topics = "wikime_event_topic", groupId = "wikime_group_id")
    public void listenGroupWikime(String message) {
        System.out.println("Received Message in group foo: " + message);
    }

    public static void main(String[] args) {
        SpringApplication.run(ConsumerRun.class,args);
    }
}
