package com.example.kafka.test;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CursJsonStorage {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY  )
  private Long id;

  @Column(length = 10000)
  private String cursJson;
}
