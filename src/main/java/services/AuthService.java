package services;

import api.ApiClient;
import api.Endpoints;
import exceptions.AutomationException;
import io.restassured.response.Response;

public class AuthService {
    private AuthService() {
    }

    public static AuthService init() {
        return new AuthService();
    }

    public Response login(Object payload) throws AutomationException {
        return ApiClient.init()
                .path(Endpoints.LOGIN)
                .body(payload)
                .post()
                .response();
    }

    public Response register(Object payload) throws AutomationException {
        return ApiClient.init()
                .path(Endpoints.REGISTER)
                .body(payload)
                .post()
                .response();
    }

    public Response forgotPassword(Object payload) throws AutomationException {
        return ApiClient.init()
                .path(Endpoints.FORGOT_PASSWORD)
                .body(payload)
                .post()
                .response();
    }
}