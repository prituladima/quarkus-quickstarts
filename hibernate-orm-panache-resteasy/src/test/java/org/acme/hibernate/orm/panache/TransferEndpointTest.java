package org.acme.hibernate.orm.panache;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.h2.H2DatabaseTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

@QuarkusTest
@QuarkusTestResource(H2DatabaseTestResource.class)
public class TransferEndpointTest {

    @Test
    public void testNullFromAccountId(){
        given()
                .contentType(ContentType.JSON)
                .body("{}")
                .post("/transfer")
                .then()
                .statusCode(404);
    }

    @Test
    public void testNullToAccountId(){
        given()
                .contentType(ContentType.JSON)
                .body("{\"fromHolderId\":4}")
                .post("/transfer")
                .then()
                .statusCode(404);
    }

    @Test
    public void testNullSum(){
        given()
                .contentType(ContentType.JSON)
                .body("{" +
                        "\"fromHolderId\":4," +
                        "\"toHolderId\":4" +
                        "}")
                .post("/transfer")
                .then()
                .statusCode(405);
    }

    @Test
    public void testOneAndTheSameAccount(){
        given()
                .contentType(ContentType.JSON)
                .body("{" +
                        "\"fromHolderId\":4," +
                        "\"toHolderId\":4," +
                        "\"sum\":9}")
                .post("/transfer")
                .then()
                .statusCode(405);
    }

    @Test
    public void testNegateSum(){
        given()
                .contentType(ContentType.JSON)
                .body("{" +
                        "\"fromHolderId\":4," +
                        "\"toHolderId\":4," +
                        "\"sum\":-9}")
                .post("/transfer")
                .then()
                .statusCode(405);
    }

    @Test
    public void testNotEnoughMoney(){
        given()
                .contentType(ContentType.JSON)
                .body("{" +
                        "\"fromHolderId\":7," +
                        "\"toHolderId\":4," +
                        "\"sum\":90000000}")
                .post("/transfer")
                .then()
                .statusCode(405);
    }

    @Test
    public void testTransferOk(){
        given()
                .contentType(ContentType.JSON)
                .body("{" +
                        "\"fromHolderId\":7," +
                        "\"toHolderId\":4," +
                        "\"sum\":1}")
                .post("/transfer")
                .then()
                .statusCode(204);
    }





}
