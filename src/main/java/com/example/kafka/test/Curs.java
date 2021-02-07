package com.example.kafka.test;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Curs {
  private String name;
  private String currency;
  private String Amount;
}
