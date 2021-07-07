package org.aleksid.wikime.kafka;

import org.aleksid.wikime.WikimeTelegramBot;
import org.aleksid.wikime.controller.MainController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(value = "kafka.consumer-enabled", havingValue = "true")
public class Consumer {

    private static final Logger logger = LogManager.getLogger(Consumer.class);

    @KafkaListener(topics = {"WIKIME_DATA"})
    public void consume(final @Payload String message,
                        final @Header(KafkaHeaders.OFFSET) Integer offset,
                        final @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) String key,
                        final @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition,
                        final @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                        final @Header(KafkaHeaders.RECEIVED_TIMESTAMP) long ts,
                        final Acknowledgment acknowledgment
    ) {
        String consumed = String.format("#### -> Consumed message -> TIMESTAMP: %d\n%s\noffset: %d\nkey: %s\npartition: %d\ntopic: %s",
                ts,
                message,
                offset,
                key,
                partition,
                topic);

        acknowledgment.acknowledge();

        WikimeTelegramBot.send(message);

        logger.info("message delivered though kafka " + consumed);
    }
}