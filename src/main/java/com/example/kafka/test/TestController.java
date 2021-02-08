package com.example.kafka.test;

import com.example.kafka.test.Repository.CursJsonStorageRepository;
import com.example.kafka.test.Service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import com.hazelcast.core.HazelcastInstance;

import java.io.IOException;
import java.util.concurrent.ConcurrentMap;

@RestController
public class TestController {

    @Autowired
    private CursJsonStorageRepository cursJsonStorageRepository;

    @Autowired
    TestService testService;

    private final Producer producer;

    @Autowired
    public TestController(Producer producer) {
        this.producer = producer;
    }


    @PostMapping("/publish")
    public void getCursJsonStorage(@RequestParam("cursId") Long cursId) throws IOException {
        CursJsonStorage cursJsonStorage = testService.getCursJsonStorage(cursId);
        this.producer.sendMessage(cursJsonStorage.toString());
    }

    @GetMapping("/publish/{cursId}")
    public CursJsonStorage getItem(@PathVariable Long cursId){
        return testService.getCursJsonStorage(cursId);
    }
}
