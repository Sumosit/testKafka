package com.example.kafka.test.Repository;

import com.example.kafka.test.Entity.CursJsonStorage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CursJsonStorageRepository extends JpaRepository<CursJsonStorage, Long> {
  Optional<CursJsonStorage> findById(Long cursId);
}
