package services;

import core.api.ApiClient;
import endpoints.AuthEndpoints;
import enums.RequestMode;
import enums.UserRole;
import io.restassured.response.Response;
import models.LoginRequest;
import models.RegisterRequest;

import java.sql.Date;

public class AuthService {
    private final ApiClient user;

    public AuthService(UserRole role) {
        user = ApiClient.getInstance(role);
    }


    public Response login(String username, String password) {
        LoginRequest loginRequest = new LoginRequest(username, password);
        return user.post(AuthEndpoints.LOGIN, loginRequest, RequestMode.JSON_NO_AUTH);
    }

    public Response register(String username, String email, String password, String fullname, String phoneNumber, String gender, Date birthdate) {
        RegisterRequest request = RegisterRequest.builder()
                .username(username)
                .email(email)
                .password(password)
                .fullname(fullname)
                .phoneNumber(phoneNumber)
                .gender(gender)
                .birthdate(birthdate)
                .build();
        return user.post(AuthEndpoints.REGISTER, request, RequestMode.JSON_NO_AUTH);
    }
}
