package com.karatesan.CashRestApi.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import java.util.Objects;

@Entity
public class CashCard {
  @Id
  @GeneratedValue
  private Long Id;

  private Double amount;

  public CashCard(Double amount) {
    this.amount = amount;
  }

  public CashCard(Long id, Double amount) {
    super();
    Id = id;
    this.amount = amount;
  }

  public CashCard() {}

  public Long getId() {
    return Id;
  }

  public void setId(Long id) {
    Id = id;
  }

  public Double getAmount() {
    return amount;
  }

  public void setAmount(Double amount) {
    this.amount = amount;
  }

  @Override
  public String toString() {
    return "CashCard [Id=" + Id + ", amount=" + amount + "]";
  }

  @Override
  public int hashCode() {
    return Objects.hash(Id, amount);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    CashCard other = (CashCard) obj;
    return Objects.equals(Id, other.Id) && Objects.equals(amount, other.amount);
  }
}
