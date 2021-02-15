package com.example.kafka.test.Controllers;

import com.example.kafka.test.KafkaService.Producer;
import com.example.kafka.test.Repository.CursJsonStorageRepository;
import com.example.kafka.test.KafkaService.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class TestController {

  @Autowired
  private CursJsonStorageRepository cursJsonStorageRepository;

  @Autowired
  TestService testService;

  private final Producer producer;

  @Autowired
  public TestController(Producer producer) {
    this.producer = producer;
  }


  //  @PostMapping("/publish")
//  public void getRates(@RequestParam("ratesId") Long ratesId) throws IOException {
//    Rates rates = testService.getRatesList(ratesId);
//    this.producer.sendMessage(rates.toString());
//  }
  @GetMapping("/api/test/all")
  public String allAccess() {
    return "Public Content.";
  }

  @GetMapping("/api/test/user")
  @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
  public String userAccess() {
    return "User Content.";
  }

  @GetMapping("/api/test/mod")
  @PreAuthorize("hasRole('MODERATOR')")
  public String moderatorAccess() {
    return "Moderator Board.";
  }

  @GetMapping("/api/test/admin")
  @PreAuthorize("hasRole('ADMIN')")
  public String adminAccess() {
    return "Admin Board.";
  }


}
