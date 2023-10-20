package com.karatesan.CashRestApi.controllers;

import com.karatesan.CashRestApi.model.CashCard;
import com.karatesan.CashRestApi.services.CashCardService;
import java.net.URI;
import java.util.Optional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/cashcards")
public class CashCardController {
  private final CashCardService service;

  public CashCardController(CashCardService service) {
    super();
    this.service = service;
  }

  @GetMapping("/{id}")
  public ResponseEntity<CashCard> findById(@PathVariable Long id) {
    Optional<CashCard> cashCardOptional = service.findById(id);
    if (cashCardOptional.isPresent()) return ResponseEntity.ok(
      cashCardOptional.get()
    ); else return ResponseEntity.notFound().build();
  }
  
  @GetMapping
  public ResponseEntity<Iterable<CashCard>> findALl(){
	  return ResponseEntity.ok(service.findAll());
  }

  @PostMapping
  public ResponseEntity<Void> createCashCard(
    @RequestBody CashCard newCashCard,
    UriComponentsBuilder ucb
  ) {
    CashCard card = service.saveCashCard(newCashCard);
    URI locationOfNeweCashCardUri = ucb
      .path("cashcards/{id}")
      .buildAndExpand(card.getId())
      .toUri();
    return ResponseEntity.created(locationOfNeweCashCardUri).build();
  }
  
  
}
