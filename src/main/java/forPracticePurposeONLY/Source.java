package forPracticePurposeONLY;

import io.restassured.RestAssured;
import io.restassured.parsing.Parser;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;


public class Source {
    public static void main(String args[]) {
        RestAssured.baseURI = "https://rahulshettyacademy.com";
        postMethod();
    }

    public static void postMethod() {
        ApiData air = given()
                .log().all()
                .queryParam("key", "qaclick123")
                .header("Content-Type", "application/json")
                .body(returnData())
                .expect().defaultParser(Parser.JSON)
                .when()
                .post("/maps/api/place/add/json")
                .then()
                .log().all()
                .assertThat()
                .statusCode(200)
                .body("scope", equalTo("APP"))
//                .header("Server", equalTo("Apache/2.4.18 (Ubuntu)"))
                .extract()
                .as(ApiData.class);
        System.out.println("***************************************************************");
       // System.out.println(air.getScope());
    }

    public static void getMethod() {
        given()
                .log().all()
                .queryParam("key", "qaclick123")
                .queryParam("place_id", "b8b90a6167b67f5dfe031ee25e194e68")
                .header("Content-Type", "application/json")
                .when()
                .get("/maps/api/place/get/json")
                .then()
                .log().all()
                .assertThat()
                .statusCode(200)
                .extract();
    }

    public static void putMethod() {
        given()
                .log().all()
                .queryParam("key", "qaclick123")
                .header("Content-Type", "application/json")
                .body(Data.data())
                .when()
                .put("/maps/api/place/add/json")
                .then()
                .log().all()
                .assertThat()
                .statusCode(200)
                .extract()
                .asString();
    }

    public static ApiData returnData() {
        ApiData data = new ApiData();
        data.setAccuracy("50");
        data.setAddress("40, side layout, cohen 09");
        data.setName("Rahul");
        data.setLanguage("Marathi");
        data.setWebsite("www.google.com");
        data.setPhone_number("789");
        List<String> type = new ArrayList<String>();
        type.add("rahul");
        type.add("Rajdhar");
        data.setTypes(type);
        Location loc = new Location();
        loc.lat = "lattitude";
        loc.lng = "langitude";
        data.setLocation(loc);
        return data;
    }
}
