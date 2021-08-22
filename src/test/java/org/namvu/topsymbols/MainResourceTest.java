package org.namvu.topsymbols;

import io.quarkus.test.junit.QuarkusTest;
import io.vertx.mutiny.core.Vertx;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.startsWith;

@QuarkusTest
public class MainResourceTest {

//    @Test
//    void testGreeter() {
//      given()
//        .queryParam("name", "Quarkus")
//        .when().get("/vertx/hello")
//        .then()
//        .statusCode(200)
//        .body(startsWith("Hello Quarkus"));
//    }

}
