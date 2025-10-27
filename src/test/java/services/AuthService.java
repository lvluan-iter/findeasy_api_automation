package services;

import core.api.ApiClient;
import endpoints.AuthEndpoints;
import enums.RequestMode;
import enums.UserRole;
import io.restassured.response.Response;
import models.LoginRequest;

public class AuthService {
    private final ApiClient user;

    public AuthService(UserRole role) {
        user = ApiClient.getInstance(role);
    }


    public Response login(String username, String password) {
        LoginRequest loginRequest = new LoginRequest(username, password);
        return user.post(AuthEndpoints.LOGIN, loginRequest, RequestMode.JSON_NO_AUTH);
    }
}
