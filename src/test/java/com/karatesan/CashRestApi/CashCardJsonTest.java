package com.karatesan.CashRestApi;

import static org.assertj.core.api.Assertions.assertThat;

import com.karatesan.CashRestApi.model.CashCard;
import java.io.IOException;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

@JsonTest
public class CashCardJsonTest {
  @Autowired
  private JacksonTester<CashCard> json;
  
  @Autowired
  private JacksonTester<CashCard[]>jsonList;
  
  private CashCard[] cashCards;
  
  @BeforeEach
  void setUp() {
	  cashCards = new CashCard[] {
              new CashCard(99L, 123.45),
              new CashCard(100L, 1.00),
              new CashCard(101L, 150.00)};
  }

  @Test
  public void cashCardSerializationTest() throws IOException {
    CashCard cashCard = new CashCard(99L, 123.45);

    //testing class written to json response. Expected json is in test/resources (you have to create it)
    //jacksonTester.write - obiekt do json
    assertThat(json.write(cashCard)).isStrictlyEqualToJson("single.json");
    assertThat(json.write(cashCard)).hasJsonPathNumberValue("@.id");
    assertThat(json.write(cashCard)).extractingJsonPathNumberValue("@.id").isEqualTo(99);
    assertThat(json.write(cashCard)).hasJsonPathNumberValue("@.amount");
    assertThat(json.write(cashCard))
      .extractingJsonPathNumberValue("@.amount")
      .isEqualTo(123.45);
  }
  
  @Test
  public void cashCardListSerializationTest() throws IOException {
	  assertThat(jsonList.write(cashCards)).isStrictlyEqualToJson("list.json");
  }

  @Test
  public void cashCardDeserializationTest() throws IOException {
	  
	  //triplelien quotation is multiline string
	   String expected = """
	           {
	               "id":99,
	               "amount":123.45
	           }
	           """;
	   //json.parse - json to object
	   //jacksonTester.parseObject - z jsona do obiektu
	   assertThat(json.parse(expected)).isEqualTo(new CashCard(99L,123.45));
	   assertThat(json.parseObject(expected).getId()).isEqualTo(99);
	   assertThat(json.parseObject(expected).getAmount()).isEqualTo(123.45);
	  
	  
	  
  }
  
  @Test
  public void cashCardListDeserializationTest() throws IOException {
	  
	  String expected = """
		  [
	  		{"id": 99, "amount": 123.45 },
	  		{"id": 100, "amount": 1.00 },
	  		{"id": 101, "amount": 150.00 }
		  ]""";
 assertThat(jsonList.parse(expected)).isEqualTo(cashCards);
	
	  
  }
}
