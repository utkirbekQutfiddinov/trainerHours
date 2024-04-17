package com.epam.trainerhours.messaging;

import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
public class Sender {

    private final JmsTemplate jmsTemplate;

    public Sender(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    public void sendMessage(String destination, String message){
        jmsTemplate.convertAndSend(destination,message);
    }
}
