package core.api;

import enums.RequestMode;
import enums.UserRole;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class ApiClient {
    private final String token;

    public ApiClient(UserRole role) {
        token = TokenManager.getToken(role);
    }

    public static ApiClient getInstance(UserRole role) {
        return new ApiClient(role);
    }

    public Response get(String path, RequestMode mode) {
        return given()
                .spec(RequestBuilder.getRequestSpec(mode, token))
                .when()
                .get(path);
    }

    public Response post(String path, Object body, RequestMode mode) {
        return given()
                .spec(RequestBuilder.getRequestSpec(mode, token))
                .body(body)
                .when()
                .post(path);
    }

    public Response put(String path, Object body, RequestMode mode) {
        return given()
                .spec(RequestBuilder.getRequestSpec(mode, token))
                .body(body)
                .when()
                .put(path);
    }

    public Response patch(String path, Object body, RequestMode mode) {
        return given()
                .spec(RequestBuilder.getRequestSpec(mode, token))
                .body(body)
                .when()
                .patch(path);
    }

    public Response delete(String path, RequestMode mode) {
        return given()
                .spec(RequestBuilder.getRequestSpec(mode, token))
                .when()
                .delete(path);
    }

    public Response upload(String path, String fileParamName, Object file, RequestMode mode) {
        return given()
                .spec(RequestBuilder.getRequestSpec(mode, token))
                .multiPart(fileParamName, file)
                .when()
                .post(path);
    }
}