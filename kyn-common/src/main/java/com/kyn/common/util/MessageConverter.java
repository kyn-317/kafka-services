package com.kyn.common.util;

import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;

import lombok.extern.slf4j.Slf4j;
import reactor.kafka.receiver.ReceiverOffset;
@Slf4j
public class MessageConverter {

    public static <T> Record<T> toRecord(Message<T> message) {
        log.info("MessageConverter.toRecord called: message={}", message);
        var payload = message.getPayload();
        var key = message.getHeaders().get(KafkaHeaders.RECEIVED_KEY, String.class);
        var ack = message.getHeaders().get(KafkaHeaders.ACKNOWLEDGMENT, ReceiverOffset.class);
        log.info("MessageConverter.toRecord result: key={}, payload={}, ack={}", key, payload, ack);
        return new Record<>(key, payload, ack);
    }

}
