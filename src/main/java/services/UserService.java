package services;

import core.api.ApiClient;
import core.exceptions.AutomationException;
import endpoints.UserEndpoints;
import enums.UserRole;
import io.restassured.response.Response;

public class UserService {
    private final UserRole role;
    private Response apiResponse;

    private UserService(UserRole role) {
        this.role = role;
    }

    public static UserService init(UserRole role) {
        return new UserService(role);
    }

    public UserService getAllUsers() throws AutomationException {
        apiResponse = ApiClient.init()
                .auth(role)
                .path(UserEndpoints.USER_ENDPOINT)
                .get()
                .response();
        return this;
    }

    public UserService getUserByUsername(String username) throws AutomationException {
        apiResponse = ApiClient.init()
                .auth(role)
                .path(UserEndpoints.GET_USER_BY_USERNAME)
                .pathParam("name", username)
                .get()
                .response();
        return this;
    }

    public UserService deleteUser(Long userId) throws AutomationException {
        apiResponse = ApiClient.init()
                .auth(role)
                .path(UserEndpoints.DELETE_USER)
                .pathParam("id", userId.toString())
                .delete()
                .response();
        return this;
    }

    public UserService changePassword(Long userId, Object payload) throws AutomationException {
        apiResponse = ApiClient.init()
                .auth(role)
                .path(UserEndpoints.CHANGE_PASSWORD)
                .pathParam("id", userId.toString())
                .body(payload)
                .put()
                .response();
        return this;
    }

    public Response getResponse() {
        return apiResponse;
    }
}