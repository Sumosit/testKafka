package com.example.kafka.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.hazelcast.HazelcastInstanceFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.hazelcast.core.HazelcastInstance;

import java.io.IOException;
import java.util.concurrent.ConcurrentMap;

@RestController
public class TestController {
    @Qualifier("hazelcastInstance")
    @Autowired
    private HazelcastInstance hazelcastInstance;

    private final Producer producer;

    private ConcurrentMap<String,String> retrieveMap() {
        return hazelcastInstance.getMap("map");
    }

    @Autowired
    public TestController(Producer producer) {
        this.producer = producer;
    }
    @PostMapping("/publish")
    public void messageToTopic(@RequestParam("message") String message) throws IOException {

        this.producer.sendMessage(message);
    }
}
