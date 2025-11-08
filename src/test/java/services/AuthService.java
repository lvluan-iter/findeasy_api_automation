package services;

import core.api.ApiClient;
import core.exceptions.AutomationException;
import endpoints.AuthEndpoints;
import io.restassured.response.Response;

public class AuthService {

    private Response apiResponse;

    private AuthService() {
    }

    public static AuthService init() {
        return new AuthService();
    }

    public AuthService login(Object payload) throws AutomationException {
        apiResponse = ApiClient.init()
                .path(AuthEndpoints.LOGIN)
                .body(payload)
                .post()
                .response();

        return this;
    }

    public AuthService register(Object payload) throws AutomationException {
        apiResponse = ApiClient.init()
                .path(AuthEndpoints.REGISTER)
                .body(payload)
                .post()
                .response();

        return this;
    }

    public Response getResponse() {
        return apiResponse;
    }
}