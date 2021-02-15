package com.example.kafka.test.Repository;

import com.example.kafka.test.Entity.Rates;
import org.springframework.data.jpa.repository.JpaRepository;

import java.awt.print.Pageable;
import java.util.List;

public interface RatesRepository extends JpaRepository<Rates, Integer> {
}
