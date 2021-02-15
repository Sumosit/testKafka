package com.example.kafka.test.Controllers;

import com.example.kafka.test.Entity.Rates;
import com.example.kafka.test.Graphql.RatesService;
import com.example.kafka.test.KafkaService.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class RatesController {

  @Autowired
  private TestService testService;

  @Autowired
  private RatesService ratesService;

  @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
  @GetMapping("/rates/{ratesId}")
  public Optional<Rates> getRates(@PathVariable final int ratesId) {
    return testService.getRates(ratesId);
  }

  @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
  @GetMapping("/rates-list")
  public List<Rates> getRatesList() {
    return testService.getRatesList();
  }
}
