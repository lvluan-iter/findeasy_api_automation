package services;

import api.ApiClient;
import api.Endpoints;
import enums.HttpStatus;
import exceptions.AutomationException;
import io.restassured.response.Response;

public class AuthService {

    private Response apiResponse;

    private AuthService() {
    }

    public static AuthService init() {
        return new AuthService();
    }

    public static isNegativeTest(HttpStatus status)

    public AuthService login(Object payload) throws AutomationException {
        apiResponse = ApiClient.init()
                .path(Endpoints.LOGIN)
                .body(payload)
                .post()
                .response();
        return this;
    }

    public AuthService register(Object payload) throws AutomationException {
        apiResponse = ApiClient.init()
                .path(Endpoints.REGISTER)
                .body(payload)
                .post()
                .response();
        return this;
    }

    public AuthService forgotPassword(Object payload) throws AutomationException {
        apiResponse = ApiClient.init()
                .path(Endpoints.FORGOT_PASSWORD)
                .body(payload)
                .post()
                .response();
        return this;
    }

    public Response getResponse() {
        return apiResponse;
    }
}