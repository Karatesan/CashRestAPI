package com.karatesan.CashRestApi.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.karatesan.CashRestApi.model.CashCard;

@Repository
public interface CashCardRepository extends JpaRepository<CashCard, Long> {


    CashCard findByIdAndOwner(Long id, String owner);

    Page<CashCard> findByOwner(String owner, PageRequest amount);
    Boolean existsByIdAndOwner(Long id, String owner);
}

