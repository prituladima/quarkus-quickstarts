package org.acme.hibernate.orm.panache;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.h2.H2DatabaseTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;

@QuarkusTest
@QuarkusTestResource(H2DatabaseTestResource.class)
public class AccountHolderEndpointTest {

    @Test
    public void testListAllAccountHolders() {
        given()
                .when().get("/holders")
                .then()
                .statusCode(200)
                .body(
                        containsString("Dustin Hoffman"),
                        containsString("Tom Cruise")
                );
    }

    @Test
    public void testAccountHolderDoesNotExist() {
        given()
                .when().get("/holders/" + Long.MAX_VALUE)
                .then()
                .statusCode(404);
    }

    @Test
    public void testAccountHolderCreation() {
        given()
                .contentType(ContentType.JSON)
                .body("{\n" +
                        "\t\"name\":\"Emma Watson\",\n" +
                        "\t\"balance\":10\n" +
                        "}")
                .post("/holders")
                .then()
                .statusCode(201);
    }

    @Test
    public void testAccountHolderUpdating() {
        given()
                .contentType(ContentType.JSON)
                .body("{\n" +
                        "\t\"name\":\"Batman\",\n" +
                        "\t\"balance\":1000\n" +
                        "}")
                .put("/holders/2")
                .then()
                .statusCode(200);
    }

    @Test
    public void testAccountHolderUpdatingByNotExistingId() {
        given()
                .contentType(ContentType.JSON)
                .body("{\n" +
                        "\t\"name\":\"Batman\",\n" +
                        "\t\"balance\":1000\n" +
                        "}")
                .put("/holders/" + Long.MAX_VALUE)
                .then()
                .statusCode(404);
    }

}
