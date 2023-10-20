package com.karatesan.CashRestApi.controllers;

import static org.assertj.core.api.Assertions.assertThat;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.karatesan.CashRestApi.model.CashCard;
import java.net.URI;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class CashCardControllerTest {
  @Autowired
  TestRestTemplate restTemplate;

  @Test
  void shouldReturnACashCardWhenDataIsSaved() {
    ResponseEntity<String> response = restTemplate.getForEntity(
      "/cashcards/99",
      String.class
    );
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

    DocumentContext documentContext = JsonPath.parse(response.getBody());
    Number id = documentContext.read("$.id");
    Number amount = documentContext.read("$.amount");
    assertThat(id).isEqualTo(99);
    assertThat(amount).isEqualTo(123.45);
  }

  @Test
  void shouldNotReturnACashCardWithAnUnknownId() {
    ResponseEntity<String> response = restTemplate.getForEntity(
      "/cashcards/1000",
      String.class
    );

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    assertThat(response.getBody()).isBlank();
  }

  @Test
  void shouldCreateNerwCard() {
    CashCard card = new CashCard(250.00);
    ResponseEntity<Void> createResponseEntity = restTemplate.postForEntity(
      "/cashcards",
      card,
      Void.class
    );
    assertThat(createResponseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);

    URI locationOfNewCashCardUri = createResponseEntity.getHeaders().getLocation();
    //	  ResponseEntity<CashCard> getResponse = restTemplate.getForEntity(locationOfNewCashCardUri, CashCard.class);
    //	  assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    //	  assertThat(card.getAmount()).isEqualByComparingTo(getResponse.getBody().getAmount());
    ResponseEntity<String> getResponse = restTemplate.getForEntity(
      locationOfNewCashCardUri,
      String.class
    );
    assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    DocumentContext documentContext = JsonPath.parse(getResponse.getBody());
    Number id = documentContext.read("$.id");
    Number amount = documentContext.read("$.amount");
    assertThat(id).isNotNull();
    assertThat(amount).isEqualTo(250.00);
  }
  
  @Test
  public void shouldReturnAllCashCardsWhenListIsRequested() {
	  ResponseEntity<String>responseEntity = restTemplate.getForEntity("/cashcards", String.class);
	  assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
	  
	  DocumentContext documentContext = JsonPath.parse(responseEntity.getBody());
  }
}









