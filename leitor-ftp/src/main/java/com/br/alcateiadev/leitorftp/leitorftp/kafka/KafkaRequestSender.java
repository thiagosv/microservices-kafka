package com.br.alcateiadev.leitorftp.leitorftp.kafka;

import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class KafkaRequestSender {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void sendMessage(String topicRequest, Object request){
        Message<String> message = MessageBuilder
            .withPayload(new Gson().toJson(request))
            .setHeader(KafkaHeaders.TOPIC, topicRequest)
            .build();

        this.kafkaTemplate.send(message);
    }

}
