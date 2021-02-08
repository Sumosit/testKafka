package com.example.kafka.test.Service;

import com.example.kafka.test.Curs;
import com.example.kafka.test.CursJsonStorage;
import com.example.kafka.test.Repository.CursJsonStorageRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class TestService {

    private final Log logger = LogFactory.getLog(getClass());

    private int i = 0;

    private static final String TOPIC = "test_topic";

    @Autowired
    private KafkaTemplate<Object, String> kafkaTemplate;

    @Autowired
    private CursJsonStorageRepository cursJsonStorageRepository;

    @Cacheable(value = "cursJsonStorage", key = "#id")
    public CursJsonStorage getCursJsonStorage(Long cursId) {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        CursJsonStorage cursJsonStorage = cursJsonStorageRepository.findById(cursId).orElseThrow(RuntimeException::new);
        logger.info(cursJsonStorage);
        return cursJsonStorage;
    }

    public CursJsonStorage createCursJsonStorage(CursJsonStorage cursJsonStorage) {
        return cursJsonStorageRepository.save(cursJsonStorage);
    }

    public void run(String id) throws Exception {
        String url = "https://bsbnb.nationalbank.kz/?furl=cursFull&switch=rus";
        Document document = Jsoup.connect(url).get();
        Elements amount = document.select("td:lt(4):gt(2).gen7");
        Elements name = document.select("td:lt(2):gt(0).gen7");
        Elements currency = document.select("td:lt(3):gt(1).gen7");

        List<Curs> cursList = new ArrayList<>();
        Curs curs;
        try {
            for (int i = 0; i < name.size(); i++) {
                curs = new Curs(name.get(i).text(),
                    currency.get(i).text(),
                    amount.get(i).text());
                cursList.add(curs);
            }
        } catch (Exception e) {
            this.kafkaTemplate.send(TOPIC, e.getMessage());
        } finally {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
            String cursToJson = objectMapper.writeValueAsString(cursList);
            createCursJsonStorage(new CursJsonStorage(null, cursToJson));
            this.kafkaTemplate.send(TOPIC, "Convert List to JSON :" + cursToJson);
            this.kafkaTemplate.send(TOPIC, java.time.LocalTime.now().toString());
        }
    }
}
