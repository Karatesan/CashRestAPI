package com.karatesan.CashRestApi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.karatesan.CashRestApi.model.CashCard;

@Repository
public interface CashCardRepository extends JpaRepository<CashCard, Long> {}

