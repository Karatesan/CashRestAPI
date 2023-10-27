package com.karatesan.CashRestApi.services;

import com.karatesan.CashRestApi.model.CashCard;
import com.karatesan.CashRestApi.repositories.CashCardRepository;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
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

    public List<CashCard> findAll() {
        return repository.findAll();
    }

    public Page<CashCard> findAll(Pageable pageable) {
        return repository.findAll(
                PageRequest.of(
                        pageable.getPageNumber(),
                        pageable.getPageSize(),
                        pageable.getSortOr(Sort.by(Sort.Direction.ASC, "amount"))
                )
        );
    }

    public CashCard findByIdAndOwner(Long id, String name) {

        return repository.findByIdAndOwner(id, name);
    }

    public Page<CashCard> findByOwner(Pageable pageable, String name) {
        return repository.findByOwner(
                name,
                PageRequest.of(
                        pageable.getPageNumber(),
                        pageable.getPageSize(),
                        pageable.getSortOr(Sort.by(Sort.Direction.ASC, "amount"))
                )
        );
    }


    public HttpStatus putCashCard(Long requestedId, CashCard cashCardUpdate, Principal principal) {
        Optional<CashCard> cashCardOptional = Optional.ofNullable(repository.findByIdAndOwner(requestedId, principal.getName()));
        if (cashCardOptional.isPresent()) {
            CashCard cashCardToUpdate = cashCardOptional.get();
            cashCardToUpdate.setAmount(cashCardUpdate.getAmount());
            repository.save(cashCardToUpdate);
            return HttpStatus.NO_CONTENT;
        } else return HttpStatus.NOT_FOUND;
    }

    public HttpStatus deleteCashCard(Long id, Principal principal) {
        //Mozna sciagnac z db obiekt i sprawdzic czy istnieje lub wykozystac metode "exists" -
        // co jest szybsze, bo nie trzeba pobierac ncizego z bazy
        //CashCard cardToDelete = this.findByIdAndOwner(id, principal.getName());
        //if (cardToDelete == null) return HttpStatus.NOT_FOUND;
        if (repository.existsByIdAndOwner(id, principal.getName())) {
            repository.deleteById(id);
            return HttpStatus.NO_CONTENT;
        }
        return HttpStatus.NOT_FOUND;
    }
}
