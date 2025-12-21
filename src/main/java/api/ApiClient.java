package api;

import constants.CommonConstants;
import enums.UserRole;
import exceptions.AutomationException;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.EncoderConfig;
import io.restassured.config.HttpClientConfig;
import io.restassured.http.ContentType;
import io.restassured.http.Cookie;
import io.restassured.http.Cookies;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import utils.EnvReader;

import java.util.Map;

import static io.restassured.RestAssured.given;

public class ApiClient {
    private RequestSpecBuilder requestSpecBuilder;
    private Response apiResponse;

    private ApiClient() throws AutomationException {
        initSpec();
        initTimeout();
    }

    public static ApiClient init() throws AutomationException {
        return new ApiClient();
    }

    private void initSpec() throws AutomationException {
        EncoderConfig encoderConfig = new EncoderConfig();
        requestSpecBuilder = new RequestSpecBuilder();

        requestSpecBuilder.setBaseUri(EnvReader.getBaseUrl());
        requestSpecBuilder.setContentType(ContentType.JSON);

        requestSpecBuilder.setConfig(
                RestAssured.config()
                        .encoderConfig(
                                encoderConfig.appendDefaultContentCharsetToContentTypeIfUndefined(false)
                        )
        );
    }

    private void initTimeout() {
        RestAssured.config = RestAssured.config()
                .httpClient(
                        HttpClientConfig.httpClientConfig()
                                .setParam("http.connection.timeout", CommonConstants.API_TIMEOUT)
                                .setParam("http.socket.timeout", CommonConstants.API_TIMEOUT)
                );
    }

    public ApiClient path(String path) {
        requestSpecBuilder.setBasePath(path);
        return this;
    }

    public ApiClient pathParam(String key, String value) {
        requestSpecBuilder.addPathParam(key, value);
        return this;
    }

    public ApiClient queryParam(String key, String value) {
        requestSpecBuilder.addQueryParam(key, value);
        return this;
    }

    public ApiClient contentType(ContentType type) {
        requestSpecBuilder.setContentType(type);
        return this;
    }

    public ApiClient headers(Map<String, String> headers) {
        requestSpecBuilder.addHeaders(headers);
        return this;
    }

    public ApiClient cookies(Map<String, String> cookies) {
        requestSpecBuilder.addCookies(cookies);
        return this;
    }

    public ApiClient cookies(Cookies cookies) {
        requestSpecBuilder.addCookies(cookies);
        return this;
    }

    public ApiClient cookie(Cookie cookie) {
        requestSpecBuilder.addCookie(cookie);
        return this;
    }

    public ApiClient body(Object body) {
        requestSpecBuilder.setBody(body);
        return this;
    }

    public ApiClient auth(UserRole role) throws AutomationException {
        if (role != null) {
            String token = TokenManager.getToken(role);
            requestSpecBuilder.addHeader("Authorization", "Bearer " + token);
        }
        return this;
    }

    private ApiClient execute(String method) {
        RequestSpecification requestSpecification = requestSpecBuilder.build();

        apiResponse = given()
                .spec(requestSpecification)
                .when()
                .request(method)
                .then()
                .extract()
                .response();

        return this;
    }

    public ApiClient get() {
        return execute("GET");
    }

    public ApiClient post() {
        return execute("POST");
    }

    public ApiClient put() {
        return execute("PUT");
    }

    public ApiClient delete() {
        return execute("DELETE");
    }

    public Response response() {
        return apiResponse;
    }

    public String asString() {
        return apiResponse.asString();
    }
}