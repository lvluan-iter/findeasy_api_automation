package services;

import api.ApiClient;
import api.Endpoints;
import api.TokenManager;
import exceptions.AutomationException;
import io.restassured.response.Response;

public class UserService {
    private String token;

    public static UserService init() {
        return new UserService();
    }

    public UserService auth(String username, String password) {
        this.token = TokenManager.getToken(username, password);
        return this;
    }

    public Response getAllUsers() throws AutomationException {
        return ApiClient.init()
                .auth(token)
                .path(Endpoints.USER_ENDPOINT)
                .get()
                .response();
    }

    public Response getUserByUsername(String username) throws AutomationException {
        return ApiClient.init()
                .auth(token)
                .path(Endpoints.GET_USER_BY_USERNAME)
                .pathParam("name", username)
                .get()
                .response();
    }

    public Response deleteUser(Long userId) throws AutomationException {
        return ApiClient.init()
                .auth(token)
                .path(Endpoints.DELETE_USER)
                .pathParam("id", userId.toString())
                .delete()
                .response();
    }

    public Response changePassword(Long userId, Object payload) throws AutomationException {
        return ApiClient.init()
                .auth(token)
                .path(Endpoints.CHANGE_PASSWORD)
                .pathParam("id", userId.toString())
                .body(payload)
                .put()
                .response();
    }
}