package org.namvu.topsymbols;

import io.quarkus.test.junit.QuarkusTest;
import io.vertx.mutiny.core.Vertx;
import org.hamcrest.core.Every;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;

@QuarkusTest
public class MainResourceTest {

    @Test
    void topQuoteVolume() throws InterruptedException {
      Thread.sleep(5000); //Wait for the schedule update data
      given()
        .when().get("/app/topQuoteVolume")
        .then()
        .statusCode(200)
        .body("size()", is(5))
        .body("symbol", everyItem(endsWith("BTC")))
        .body("quoteVolume", everyItem(notNullValue()));
    }

    @Test
    void topTradeCount() {
      given()
        .when().get("/app/topTradeCount")
        .then()
        .statusCode(200)
        .body("size()", is(5))
        .body("symbol", everyItem(endsWith("USDT")))
        .body("tradeNumber", everyItem(notNullValue()));
    }

  @Test
  void topNotionalValue() {
    given()
      .when().get("/app/topNotionalValue")
      .then()
      .statusCode(200)
      .body("size()", is(5))
      .body("symbol", everyItem(endsWith("BTC")))
      .body("notionalValueBids", everyItem(notNullValue()))
      .body("notionalValueAsks", everyItem(notNullValue()));
  }

  @Test
  void topTradePriceSpread() {
    given()
      .when().get("/app/topTradePriceSpread")
      .then()
      .statusCode(200)
      .body("size()", is(5))
      .body("symbol", everyItem(endsWith("USDT")))
      .body("priceSpread", everyItem(notNullValue()));
  }

  @Test
  void testMetricFormat() throws InterruptedException {
    Thread.sleep(10000);
    given()
      .when().get("/metrics")
      .then()
      .statusCode(200);
  }
}
