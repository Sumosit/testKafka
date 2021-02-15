package com.example.kafka.test.KafkaService;

import com.example.kafka.test.Entity.Rates;
import com.example.kafka.test.Graphql.RatesService;
import com.example.kafka.test.Repository.RatesRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TestService {

    private final Log logger = LogFactory.getLog(getClass());

    private int i = 0;

    private static final String TOPIC = "test_topic";

    @Autowired
    private KafkaTemplate<Object, String> kafkaTemplate;

    @Autowired
    private RatesRepository ratesRepository;

    @Autowired
    private RatesService ratesService;

    @Cacheable(value = "ratesList", key = "#id")
    public List<Rates> getRatesList() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        List<Rates> ratesList = ratesService.getAllRates(39);
//        List<Rates> ratesList = ratesRepository.findAll();

        logger.info(ratesList);
        return ratesList;
    }

    @Cacheable(value = "rates", key = "#id")
    public Optional<Rates> getRates(final int ratesId) {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Optional<Rates> rates = ratesService.getRates(ratesId);
//        Rates rates = ratesRepository.findById(ratesId).orElseThrow(RuntimeException::new);
        logger.info(rates);
        return rates;
    }

    public String saveRatesList(List<Rates> ratesList) {
        return ratesRepository.saveAll(ratesList).toString();
    }

    public void saveRates(Rates rates) {
        ratesRepository.save(rates);
    }

    public void run(String id) throws Exception {
        String url = "http://www.finmarket.ru/currency/rates/?id=10123&pv=0&bd=14&bm=2&by=2021&x=35&y=12#archive";
        Document document = Jsoup.connect(url).get();
        Elements code = document.select("td.fs11");
        Elements currency = document.select("tr > td:eq(1) > a:lt(2)");
        Elements amount = document.select("tr > td:eq(2)");
        Elements rate = document.select("tr > td:eq(3)");

        amount.remove(0);
        amount.remove(0);

        List<Rates> ratesList = new ArrayList<>();
        Rates rates;

        try {
            for (int i = 0; i < code.size(); i++) {
                rates = new Rates(
                    0,
                    code.get(i).text(),
                    currency.get(i).text(),
                    amount.get(i).text(),
                    rate.get(i).text());
                saveRates(rates);
                ratesList.add(rates);
            }
        } catch (Exception e) {
            this.kafkaTemplate.send(TOPIC, e.getMessage());
        } finally {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
            String ratesListToJson = objectMapper.writeValueAsString(ratesList);
            this.kafkaTemplate.send(TOPIC, ratesList.toString());
            this.kafkaTemplate.send(TOPIC, ratesListToJson);
        }

//        Elements amount = document.select("td:lt(4):gt(2).gen7");
//        Elements name = document.select("td:lt(2):gt(0).gen7");
//        Elements currency = document.select("td:lt(3):gt(1).gen7");

//        List<Curs> cursList = new ArrayList<>();
//        Curs curs;
//        try {
//            for (int i = 0; i < name.size(); i++) {
//                curs = new Curs(name.get(i).text(),
//                    currency.get(i).text(),
//                    amount.get(i).text());
//                cursList.add(curs);
//            }
//        } catch (Exception e) {
//            this.kafkaTemplate.send(TOPIC, e.getMessage());
//        } finally {
//            ObjectMapper objectMapper = new ObjectMapper();
//            objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
//            String cursToJson = objectMapper.writeValueAsString(cursList);
//            createCursJsonStorage(new CursJsonStorage(null, cursToJson));
//            this.kafkaTemplate.send(TOPIC, "Convert List to JSON :" + cursToJson);
//            this.kafkaTemplate.send(TOPIC, java.time.LocalTime.now().toString());
//        }
    }
}
