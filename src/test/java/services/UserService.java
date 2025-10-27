package services;

import core.api.ApiClient;
import endpoints.UserEndpoints;
import enums.RequestMode;
import enums.UserRole;
import io.restassured.response.Response;

public class UserService {
    private final ApiClient user;

    public UserService(UserRole role) {
        user = ApiClient.getInstance(role);
    }


    public Response deleteUser(Long userId) {
        return user.delete(String.format(UserEndpoints.DELETE_USER, userId), RequestMode.JSON_AUTH);
    }

}
