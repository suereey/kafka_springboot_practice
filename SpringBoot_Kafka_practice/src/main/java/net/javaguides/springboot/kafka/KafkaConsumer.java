package net.javaguides.springboot.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaConsumer.class);
    //create a subscriber method that subscribe to topic
    //use @kafkaklistner from spring library
    //consumer groupId is configured in application.properties.
    @KafkaListener(topics = "javaguides", groupId = "myGroup")
    public void consumer(String message){
        LOGGER.info(String.format("Message received -> %s", message));

    }

}
