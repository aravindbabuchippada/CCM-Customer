package com.ccm.customer.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class CustomerFeedbackProducerService {

    private static final String TOPIC = "customer-feedback-topic";

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void sendFeedback(String feedbackMsg) {
        kafkaTemplate.send(TOPIC, feedbackMsg);
    }
}
