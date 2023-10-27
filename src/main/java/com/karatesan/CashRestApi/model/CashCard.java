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
  private String owner;
  

  public CashCard(Double amount,String owner ) {
    super();
    this.owner = owner;
    this.amount = amount;
  }

  public CashCard(Long id,Double amount, String owner ) {
    super();
    Id = id;
    this.owner = owner;
    this.amount = amount;
  }

  public String getOwner() {
    return owner;
  }

  public void setOwner(String owner) {
    this.owner = owner;
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
    return "CashCard [Id=" + Id + ", owner=" + owner + ", amount=" + amount + "]";
  }

  @Override
  public int hashCode() {
    return Objects.hash(Id, amount, owner);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    CashCard other = (CashCard) obj;
    return (
      Objects.equals(Id, other.Id) &&
      Objects.equals(amount, other.amount) &&
      Objects.equals(owner, other.owner)
    );
  }
}
