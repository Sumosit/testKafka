package com.example.kafka.test.Graphql;

import com.example.kafka.test.Entity.Rates;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RatesMutation {
  @Autowired
  private RatesService ratesService;
  public Rates createRates(
      final String code,
      final String currency,
      final String amount,
      final String rate) {
    return this.ratesService.createRates(
        code,
        currency,
        amount,
        rate);
  }
}
