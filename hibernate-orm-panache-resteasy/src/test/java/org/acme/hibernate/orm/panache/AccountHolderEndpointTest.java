package org.acme.hibernate.orm.panache;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.h2.H2DatabaseTestResource;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.core.IsNot.not;

@QuarkusTest
@QuarkusTestResource(H2DatabaseTestResource.class)
public class AccountHolderEndpointTest {

    @Test
    public void testListAllAccountHolder() {
        //List all, should have all 3 fruits the database has initially:
        given()
                .when().get("/holders")
                .then()
                .statusCode(200)
                .body(
                        containsString("George Clooney"),
                        containsString("Dustin Hoffman"),
                        containsString("Tom Cruise")
                );

        //Delete the Cherry:
        given()
                .when().delete("/holders/1")
                .then()
                .statusCode(204);

        //List all, cherry should be missing now:
        given()
                .when().get("/holders")
                .then()
                .statusCode(200)
                .body(
                        not(containsString("George Clooney")),
                        containsString("Dustin Hoffman"),
                        containsString("Tom Cruise")
                );
    }


}
