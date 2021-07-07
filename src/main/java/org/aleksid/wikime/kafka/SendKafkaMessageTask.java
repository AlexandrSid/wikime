package org.aleksid.wikime.kafka;

import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.concurrent.ExecutionException;

@Component
public class SendKafkaMessageTask {

    private final Producer producer;

    public SendKafkaMessageTask(Producer producer) {
        this.producer = producer;
    }

    public void send(String message) throws ExecutionException, InterruptedException {
        ListenableFuture<SendResult<String, String>> listenableFuture = this.producer.sendMessage("WIKIME_DATA", "KEY", message);

        SendResult<String, String> result = listenableFuture.get();

        String produced = String.format("Produced:\ntopic: %s\noffset: %d\npartition: %d\nvalue size: %d",
                result.getRecordMetadata().topic(),
                result.getRecordMetadata().offset(),
                result.getRecordMetadata().partition(),
                result.getRecordMetadata().serializedValueSize());
    }
}