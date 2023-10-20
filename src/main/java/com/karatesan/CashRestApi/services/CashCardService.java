package com.karatesan.CashRestApi.services;

import com.karatesan.CashRestApi.model.CashCard;
import com.karatesan.CashRestApi.repositories.CashCardRepository;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CashCardService {
  private CashCardRepository repository;

  @Autowired
  public CashCardService(CashCardRepository repository) {
    this.repository = repository;
  }

  public Optional<CashCard> findById(Long id) {
    return repository.findById(id);
  }
  
  public CashCard saveCashCard(CashCard card) {
	  return repository.save(card);
  }

public  List<CashCard> findAll(){
	return repository.findAll();
}
}
