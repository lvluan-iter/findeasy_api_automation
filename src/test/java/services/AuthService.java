package services;

import core.api.ApiClient;
import core.exceptions.AutomationException;
import endpoints.AuthEndpoints;
import io.restassured.response.Response;
import models.LoginRequest;
import models.RegisterRequest;

import java.sql.Date;

public class AuthService {

    private Response apiResponse;

    private AuthService() {
    }

    public static AuthService init() {
        return new AuthService();
    }

    public AuthService login(String username, String password) throws AutomationException {
        LoginRequest loginRequest = new LoginRequest(username, password);

        apiResponse = ApiClient.init()
                .path(AuthEndpoints.LOGIN)
                .body(loginRequest)
                .post()
                .response();

        return this;
    }

    public AuthService register(String username, String email, String password, String fullname,
                                String phoneNumber, String gender, Date birthdate) throws AutomationException {
        RegisterRequest request = RegisterRequest.builder()
                .username(username)
                .email(email)
                .password(password)
                .fullname(fullname)
                .phoneNumber(phoneNumber)
                .gender(gender)
                .birthdate(birthdate)
                .build();

        apiResponse = ApiClient.init()
                .path(AuthEndpoints.REGISTER)
                .body(request)
                .post()
                .response();

        return this;
    }

    public Response getResponse() {
        return apiResponse;
    }
}