package core.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import core.constants.HttpStatus;
import core.exceptions.AutomationException;
import core.utils.ConfigReader;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.EncoderConfig;
import io.restassured.http.ContentType;
import io.restassured.http.Cookie;
import io.restassured.http.Cookies;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.io.IOException;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class ApiClient {

    private RequestSpecBuilder requestSpecBuilder;
    private RequestSpecification requestSpecification;
    private Response apiResponse;

    private HttpStatus expectedStatusCode = HttpStatus.OK;
    private String expectedResponseContentType = ContentType.JSON.toString();

    public static ApiClient init() throws AutomationException {
        return new ApiClient();
    }

    public ApiClient() throws AutomationException {
        initializeRequestSpec();
    }

    private void initializeRequestSpec() throws AutomationException {
        EncoderConfig encoderConfig = new EncoderConfig();
        requestSpecBuilder = new RequestSpecBuilder();
        requestSpecBuilder.setBaseUri(ConfigReader.init()
                .getProperty("baseUrl"));
        requestSpecBuilder.setConfig(RestAssured.config()
                .encoderConfig(encoderConfig.appendDefaultContentCharsetToContentTypeIfUndefined(false)));
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

    public ApiClient contentType(ContentType contentType) {
        requestSpecBuilder.setContentType(contentType);
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

    public ApiClient expectedStatusCode(HttpStatus expectedStatusCode) {
        this.expectedStatusCode = expectedStatusCode;
        return this;
    }

    public ApiClient expectedResponseContentType(ContentType contentType) {
        this.expectedResponseContentType = contentType.toString();
        return this;
    }

    public ApiClient expectedResponseContentType(String contentType) {
        this.expectedResponseContentType = contentType;
        return this;
    }

    public ApiClient put() {
        requestSpecification = requestSpecBuilder.build();
        apiResponse = given()
                .log()
                .all()
                .filter(new ApiResponseFilter())
                .spec(requestSpecification)
                .when()
                .put()
                .then()
                .assertThat()
                .statusCode(expectedStatusCode.getCode())
                .contentType(expectedResponseContentType)
                .and()
                .extract()
                .response();
        return this;
    }

    public ApiClient delete() {
        requestSpecification = requestSpecBuilder.build();
        apiResponse = given()
                .log()
                .all()
                .filter(new ApiResponseFilter())
                .spec(requestSpecification)
                .when()
                .delete()
                .then()
                .assertThat()
                .statusCode(expectedStatusCode.getCode())
                .contentType(expectedResponseContentType)
                .and()
                .extract()
                .response();
        return this;
    }

    public ApiClient post() {
        requestSpecification = requestSpecBuilder.build();
        apiResponse = given()
                .log()
                .all()
                .filter(new ApiResponseFilter())
                .spec(requestSpecification)
                .when()
                .post()
                .then()
                .assertThat()
                .statusCode(expectedStatusCode.getCode())
                .contentType(expectedResponseContentType)
                .and()
                .extract()
                .response();
        return this;
    }

    public ApiClient get() {
        requestSpecification = requestSpecBuilder.build();
        apiResponse = given()
                .log()
                .all()
                .filter(new ApiResponseFilter())
                .spec(requestSpecification)
                .when()
                .get()
                .then()
                .assertThat()
                .statusCode(expectedStatusCode.getCode())
                .contentType(expectedResponseContentType)
                .and()
                .extract()
                .response();
        return this;
    }

    public Response response() {
        return apiResponse;
    }

    public String getApiResponseAsString() {
        return apiResponse.asString();
    }

    public <T> T responseToPojo(Class<T> type) throws AutomationException {
        try {
            return new ObjectMapper()
                    .enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
                    .readValue(getApiResponseAsString(), type);
        } catch (IOException e) {
            throw new AutomationException("Response did not match expected POJO: " + type.getName() + e);
        }
    }

    public <T> T responseToPojo(TypeReference<T> type) throws AutomationException {
        try {
            return new ObjectMapper()
                    .enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
                    .readValue(getApiResponseAsString(), type);
        } catch (IOException e) {
            throw new AutomationException(e);
        }
    }
}