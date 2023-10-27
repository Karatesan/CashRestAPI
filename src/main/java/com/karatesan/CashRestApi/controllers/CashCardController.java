package com.karatesan.CashRestApi.controllers;

import com.karatesan.CashRestApi.model.CashCard;
import com.karatesan.CashRestApi.services.CashCardService;

import java.net.URI;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
    public ResponseEntity<CashCard> findById(@PathVariable Long id, Principal principal) {
        Optional<CashCard> cashCardOptional = Optional.ofNullable(service.findByIdAndOwner(id, principal.getName()));
        return cashCardOptional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

//  @GetMapping
//  public ResponseEntity<Iterable<CashCard>> findAll(){
//	  return ResponseEntity.ok(service.findAll());
//  }

    @PostMapping
    public ResponseEntity<Void> createCashCard(
            @RequestBody CashCard newCashCard,
            UriComponentsBuilder ucb,
            Principal principal
    ) {
        CashCard cashCardWithOwner = new CashCard(newCashCard.getAmount(), principal.getName());
        CashCard savedCard = service.saveCashCard(newCashCard);
        URI locationOfNeweCashCardUri = ucb
                .path("cashcards/{id}")
                .buildAndExpand(savedCard.getId())
                .toUri();
        return ResponseEntity.created(locationOfNeweCashCardUri).build();
    }

    @GetMapping
    public ResponseEntity<List<CashCard>> findAll(Pageable pageable, Principal principal) {
        Page<CashCard> page = service.findByOwner(pageable, principal.getName());
        return ResponseEntity.ok(page.getContent());
    }

    @PutMapping("/{requestedId}")
    public ResponseEntity<Void> updateCashCard(@PathVariable Long requestedId, @RequestBody CashCard cashCardUpdate, Principal principal) {
        HttpStatus status = service.putCashCard(requestedId, cashCardUpdate, principal);
        return ResponseEntity.status(status).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void>deleteCashCard(@PathVariable Long id,Principal principal){
        HttpStatus status = service.deleteCashCard(id,principal);
        return ResponseEntity.status(status).build();

    }
}
