package api;

import com.fasterxml.jackson.core.type.TypeReference;
import enums.HttpStatus;
import io.restassured.response.Response;
import models.ApiResponse;
import utils.JsonUtils;

import java.util.Collections;
import java.util.List;

public class AssertApiResponse {

    private final int statusCode;
    private final ApiResponse<Object> body;

    private AssertApiResponse(Response response) {
        this.statusCode = response.getStatusCode();
        this.body = JsonUtils.fromResponse(
                response,
                new TypeReference<ApiResponse<Object>>() {
                }
        );
    }

    public static AssertApiResponse assertThat(Response response) {
        return new AssertApiResponse(response);
    }

    public AssertApiResponse status(HttpStatus status) {
        if (statusCode != status.getCode()) {
            throw new AssertionError(
                    "Expected status " + status.getCode() + " but got " + statusCode
            );
        }
        return this;
    }

    public AssertApiResponse succeeded() {
        if (!body.isSucceeded()) {
            throw new AssertionError("Expected succeeded=true but got false");
        }
        return this;
    }

    public AssertApiResponse failed() {
        if (body.isSucceeded()) {
            throw new AssertionError("Expected succeeded=false but got true");
        }
        return this;
    }

    public AssertApiResponse errorContains(String expected) {
        if (expected == null || expected.isBlank()) return this;

        boolean found = errors().stream()
                .anyMatch(e -> e != null && e.contains(expected));

        if (!found) {
            throw new AssertionError(
                    "Expected error contains: " + expected + " but got: " + errors()
            );
        }
        return this;
    }

    public List<String> errors() {
        return body.getErrors() == null
                ? Collections.emptyList()
                : body.getErrors();
    }

    public <T> T resultAs(Class<T> clazz) {
        Object result = body.getResult();
        if (result == null) {
            throw new AssertionError("Result is null, cannot convert to " + clazz.getSimpleName());
        }
        return JsonUtils.convert(result, clazz);
    }

    public <T> T resultAs(TypeReference<T> typeRef) {
        Object result = body.getResult();
        if (result == null) {
            throw new AssertionError("Result is null, cannot convert result");
        }
        return JsonUtils.convert(result, typeRef);
    }
}