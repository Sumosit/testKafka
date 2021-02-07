package com.example.kafka.test.Service;

import com.example.kafka.test.Curs;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

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

    public void run(String id) throws Exception {
        String url = "https://bsbnb.nationalbank.kz/?furl=cursFull&switch=rus";
        Document document = Jsoup.connect(url).get();
        Elements links = document.select("td:lt(4).gen7");

        int counter = 0, totalCounter = 0;
        String total = "";
        List<Curs> cursList = new ArrayList<>();
        Curs curs = new Curs();
        try {
            for (Element link : links) {
                total = total + " " + link.text();
                counter++;
                totalCounter++;
                if (counter == 1) {
                    curs.setName(link.text());
                } else if (counter == 2) {
                    curs.setCurrency(link.text());
                } else if (counter == 3) {
                    curs.setAmount(link.text());
                    cursList.add(curs);
                    curs = new Curs();
                    counter = 0;
                    total = "";
                }
            }
        } catch (Exception e) {
            this.kafkaTemplate.send(TOPIC, e.getMessage());
        } finally {
            this.kafkaTemplate.send(TOPIC, cursList.toString());
            this.kafkaTemplate.send(TOPIC, "Amount of currencies: " + totalCounter / 3);
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
            String arrayToJson = objectMapper.writeValueAsString(cursList);
            this.kafkaTemplate.send(TOPIC, "Convert List to JSON :" + arrayToJson);
            this.kafkaTemplate.send(TOPIC, java.time.LocalTime.now().toString());
            counter = 0;
            totalCounter = 0;
            total = "";
        }
    }
}
