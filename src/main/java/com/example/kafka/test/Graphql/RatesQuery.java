package com.example.kafka.test.Graphql;

import com.example.kafka.test.Entity.Rates;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class RatesQuery {

  @Autowired
  private RatesService ratesService;
  public List<Rates> getAllRates(final int count) {
    return this.ratesService.getAllRates(count);
  }
  public Optional<Rates> getRates(final int id) {
    return this.ratesService.getRates(id);
  }
}
