package com.karatesan.CashRestApi.controllers;

import static org.assertj.core.api.Assertions.assertThat;


import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.karatesan.CashRestApi.model.CashCard;

import net.minidev.json.JSONArray;

import java.net.URI;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class CashCardControllerTest {
    @Autowired
    TestRestTemplate restTemplate;

    @Test
    void shouldReturnACashCardWhenDataIsSaved() {
        ResponseEntity<String> response = restTemplate
                .withBasicAuth("sarah1", "abc123")
                .getForEntity("/cashcards/99", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(response.getBody());
        Number id = documentContext.read("$.id");
        Number amount = documentContext.read("$.amount");
        assertThat(id).isEqualTo(99);
        assertThat(amount).isEqualTo(123.45);
    }

    @Test
    void shouldNotReturnACashCardWithAnUnknownId() {
        ResponseEntity<String> response = restTemplate.withBasicAuth("sarah1", "abc123").getForEntity("/cashcards/1000", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isBlank();
    }

    @Test
    @DirtiesContext
        // dirties context czysci kontekst po zakonczeniu tesu, startuje chyba apke od
        // nowa. W tym przypadku dodaje do bazy nowy element i chce, zeby go nie bylo w
        // nastepnym tescie
    void shouldCreateNewCard() {
        CashCard card = new CashCard(250.00, "sarah1");
        ResponseEntity<Void> createResponseEntity = restTemplate
                .withBasicAuth("sarah1", "abc123")
                .postForEntity("/cashcards", card, Void.class);
        assertThat(createResponseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        URI locationOfNewCashCardUri = createResponseEntity.getHeaders().getLocation();
        // ResponseEntity<CashCard> getResponse =
        // restTemplate.getForEntity(locationOfNewCashCardUri, CashCard.class);
        // assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        // assertThat(card.getAmount()).isEqualByComparingTo(getResponse.getBody().getAmount());
        ResponseEntity<String> getResponse = restTemplate.withBasicAuth("sarah1", "abc123").getForEntity(locationOfNewCashCardUri, String.class);
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        // bierze jsona i mozna na tym potem cos robic
        // read() - podajemy sciezke w pliku ktora chcez wyciagnac - $ to jest root
        // czyli jakby poczatek, "$.id" - id obiektu
        // jak $ jest lista to $ zwraca liste
        // "$..id" - zwraca ciag wszystkich id z listy obiektow, zwraca JSONArray
        DocumentContext documentContext = JsonPath.parse(getResponse.getBody());
        Number id = documentContext.read("$.id");
        Number amount = documentContext.read("$.amount");
        assertThat(id).isNotNull();
        assertThat(amount).isEqualTo(250.00);
    }

    @Test
    public void shouldReturnAllCashCardsWhenListIsRequested() {
        ResponseEntity<String> responseEntity = restTemplate.withBasicAuth("sarah1", "abc123").getForEntity("/cashcards", String.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(responseEntity.getBody());
        int cashCardNumber = documentContext.read("$.length()");
        assertThat(cashCardNumber).isEqualTo(3);
        JSONArray ids = documentContext.read("$..id");
        assertThat(ids).containsExactlyInAnyOrder(99, 100, 101);
        JSONArray amounts = documentContext.read("$..amount");
        assertThat(amounts).containsExactlyInAnyOrder(123.45, 1.0, 150.0);
    }

    @Test
    public void shouldReturnAPageOfCashCards() {
        ResponseEntity<String> response = restTemplate.withBasicAuth("sarah1", "abc123").getForEntity("/cashcards?page=0&size=1", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        DocumentContext documentContext = JsonPath.parse(response.getBody());
        JSONArray page = documentContext.read("$[*]");
        assertThat(page.size()).isEqualTo(1);
    }

    @Test
    public void shouldReturnASortedPageOFCashCards() {

        ResponseEntity<String> responseEntity = restTemplate.withBasicAuth("sarah1", "abc123").getForEntity("/cashcards?page=0&size=1&sort=amount,desc",
                String.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(responseEntity.getBody());
        JSONArray cashcards = documentContext.read("$[*]");
        assertThat(cashcards.size()).isEqualTo(1);
        double amount = documentContext.read("$[0].amount");
        assertThat(amount).isEqualTo(150.00);

    }

    @Test
    public void shouldReturnASortedPageOfCashCardsWithNoParametersAndUseDefaultValues() {

        ResponseEntity<String> response = restTemplate.withBasicAuth("sarah1", "abc123").getForEntity("/cashcards", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        DocumentContext documentContext = JsonPath.parse(response.getBody());
        JSONArray page = documentContext.read("$[*]");
        assertThat(page.size()).isEqualTo(3);
        JSONArray amountsArray = documentContext.read("$..amount");
        assertThat(amountsArray).containsExactly(1.00, 123.45, 150.00);
    }

    @Test
    void shouldNotReturnACashCardWhenUsingBadCredentials() {
        ResponseEntity<String> response = restTemplate.withBasicAuth("BAD-USER", "abc123").getForEntity("/cashcards/1", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        ResponseEntity<String> response2 = restTemplate
                .withBasicAuth("sarah1", "BAD-PASSWORD")
                .getForEntity("/cashcards/1", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    public void shouldRejectUsersWhoAreNotCardOwners() {
        ResponseEntity<String> response = restTemplate
                .withBasicAuth("hank-owns-no-cards", "qrs456")
                .getForEntity("/cashcards/99", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    public void shouldNotAllowAccessToCashCardsTheyDoNotOwn() {
        ResponseEntity<String> response = restTemplate.withBasicAuth("sarah1", "abc123").getForEntity("/cashcards/102", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @DirtiesContext
    public void shouldUpdateExistingCashCard() {

        CashCard cashCardUpdate = new CashCard(null, 19.99, null);
        HttpEntity<CashCard> request = new HttpEntity<>(cashCardUpdate);
        ResponseEntity<Void> response = restTemplate
                .withBasicAuth("sarah1", "abc123")
                .exchange("/cashcards/99", HttpMethod.PUT, request, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        ResponseEntity<String> getResponse = restTemplate
                .withBasicAuth("sarah1", "abc123")
                .getForEntity("/cashcards/99", String.class);

        DocumentContext documentContext = JsonPath.parse(getResponse.getBody());
        Double amount = documentContext.read("$.amount");
        Number id = documentContext.read("$.id");
        assertThat(amount).isEqualTo(19.99);
        assertThat(id).isEqualTo(99);
    }

    @DirtiesContext
    @Test
    public void shouldDeleteExistingCashCard() {
        ResponseEntity<Void> response = restTemplate
                .withBasicAuth("sarah1", "abc123")
                .exchange("/cashcards/99", HttpMethod.DELETE, null, Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        ResponseEntity<String> responseGet = restTemplate
                .withBasicAuth("sarah1", "abc123")
                .getForEntity("/cashcards/99", String.class);
        assertThat(responseGet.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void shouldNotAllowDeletionOfCashCardTheyDoNotOwn() {
        ResponseEntity<Void> response = restTemplate
                .withBasicAuth("sarah1", "abc123")
                .exchange("/cashcards/102", HttpMethod.DELETE, null, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

        ResponseEntity<String> getResponse = restTemplate
                .withBasicAuth("kumar2", "xyz789")
                .getForEntity("/cashcards/102", String.class);
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

    }
}
