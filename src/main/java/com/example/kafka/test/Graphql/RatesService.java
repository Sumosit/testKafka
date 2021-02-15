package com.example.kafka.test.Graphql;

import com.example.kafka.test.Entity.Rates;
import com.example.kafka.test.Repository.RatesRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RatesService {

  private final RatesRepository ratesRepository;

  public RatesService(final RatesRepository ratesRepository) {
    this.ratesRepository = ratesRepository ;
  }

  @Transactional
  public Rates createRates(
      final String code,
      final String currency,
      final String amount,
      final String rate) {
    final Rates rates = new Rates();
    rates.setCode(code);
    rates.setCurrency(currency);
    rates.setAmount(amount);
    rates.setRate(rate);
    return this.ratesRepository.save(rates);
  }
  @Transactional(readOnly = true)
  public List<Rates> getAllRates(final int count) {
    return this.ratesRepository.findAll().stream().limit(count).collect(Collectors.toList());
  }
  @Transactional(readOnly = true)
  public Optional<Rates> getRates(final int id) {
    return this.ratesRepository.findById(id);
  }
}
