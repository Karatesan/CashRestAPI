package com.karatesan.CashRestApi;

import static org.assertj.core.api.Assertions.assertThat;

import com.karatesan.CashRestApi.model.CashCard;
import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

@JsonTest
public class CashCardJsonTest {
  @Autowired
  private JacksonTester<CashCard> json;

  @Test
  public void cashCardSerializationTest() throws IOException {
    CashCard cashCard = new CashCard(99L, 123.45);

    //testing class written to json response. Expected json is in test/resources (you have to create it)
    //json.write - obiekt do json
    assertThat(json.write(cashCard)).isStrictlyEqualToJson("expected.json");
    assertThat(json.write(cashCard)).hasJsonPathNumberValue("@.id");
    assertThat(json.write(cashCard)).extractingJsonPathNumberValue("@.id").isEqualTo(99);
    assertThat(json.write(cashCard)).hasJsonPathNumberValue("@.amount");
    assertThat(json.write(cashCard))
      .extractingJsonPathNumberValue("@.amount")
      .isEqualTo(123.45);
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
	   assertThat(json.parse(expected)).isEqualTo(new CashCard(99L,123.45));
	   assertThat(json.parseObject(expected).id()).isEqualTo(99);
	   assertThat(json.parseObject(expected).amount()).isEqualTo(123.45);
	  
	  
	  
  }
}
