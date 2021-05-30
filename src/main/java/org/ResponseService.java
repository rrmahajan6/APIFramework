package org;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.util.Map;
import static io.restassured.RestAssured.given;

public class ResponseService extends Base{
    private static String baseURL;

    static {
        baseURL = "https://rahulshettyacademy.com";
    }

    public enum RequestMethodType {
        POST, GET, DELETE, PUT
    }

    public Response getResponse(String url, RequestSpecification request, RequestMethodType methodType) {
        switch (methodType) {
            case POST:
                return request.post(url);
            case GET:
                return request.get(url);
            case DELETE:
                return request.delete(url);
            case PUT:
                return request.put(url);
            default:
                return null;
        }
    }

    public Response getResponse(String url, String jsonBody, Map<String,Object> queryParam, Map<String, Object> headers, RequestMethodType methodType) {
        String requestURL = baseURL+url;

        RequestSpecification request = given().log().all();

        if (jsonBody != null) {
            if (!jsonBody.equals(""))
                request = request.body(jsonBody);
        }

        if (queryParam != null) {
            if (!queryParam.isEmpty()) {
                request = request.queryParams(queryParam);
            }
        }
        request = request.when();
        if (headers != null) {
            if (!headers.isEmpty())
            request = request.headers(headers);
        }
        Response res = getResponse(requestURL, request, methodType);
        return res;
    }
}