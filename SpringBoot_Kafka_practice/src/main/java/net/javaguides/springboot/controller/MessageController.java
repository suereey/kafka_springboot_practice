package net.javaguides.springboot.controller;

import net.javaguides.springboot.kafka.KafkaProducer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

//annotation making this class a spring mvc
@RestController
@RequestMapping("/api/v1/kafka")
public class MessageController {
    //inject a kafka producer
    private KafkaProducer kafkaProducer;

    public MessageController(KafkaProducer kafkaProducer) {
        this.kafkaProducer = kafkaProducer;
    }

    //create a rest endpoint
    //http:localhost:8080/api/v1/kafka/publish?message=hello world
    @GetMapping("/publish-nonJson")
    //get value from url, add @RequestParam, with key = "message"
    public ResponseEntity<String> publish(@RequestParam("message") String message){
        kafkaProducer.sendMessage(message);
        return ResponseEntity.ok("Message sent to the topic");
    }
}
