package net.javaguides.springboot.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

//create Spring Configuration classes
@Configuration
public class KafkaTopicConfig {
    //creat kafka topic in a kafka cluster using spring boot application
    //current topic name: javaguides
    @Bean
    public NewTopic javaguidesTopic(){
        return TopicBuilder.name("javaguides")
                .build();
    }

    @Bean
    public NewTopic javaguidesJsonTopic(){
        return TopicBuilder.name("javaguides_json")
                .build();
    }
}
