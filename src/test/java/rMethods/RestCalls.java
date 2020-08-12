package rMethods;

import io.restassured.response.Response;
import testData.API_Constants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.with;

public class RestCalls extends API_Constants {

    public static Response getRequest(String URI) {
        return given().get(JSON_PLACEHOLDER_HOST + URI);
    }

    public static Response getRequestWithParameter(String URI, String parameterKey, String parameterValue) {
        return with().params(parameterKey, parameterValue).given().get(JSON_PLACEHOLDER_HOST + URI);
    }

    public static Response getRequestWithParameter(String URI, String parameterKey, String[] parameterValue) {
        return with().params(parameterKey, Arrays.asList(parameterValue)).given().get(JSON_PLACEHOLDER_HOST + URI);
    }

    public static Response getRequestWithParameter(String URI, String parameterKey, ArrayList<Integer> parameterValue) {
        return with().params(parameterKey, parameterValue).given().get(JSON_PLACEHOLDER_HOST + URI);
    }

    public static Response getRequestWithParameters(String URI, Map<String, String> parameters) {
        return with().params(parameters).given().get(JSON_PLACEHOLDER_HOST + URI);
    }

    public static Response postRequest(String body, String URI) {
        return with().body(body).request("POST", JSON_PLACEHOLDER_HOST + URI);
    }
}
