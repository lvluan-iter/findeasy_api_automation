package services;

import core.api.ApiClient;
import core.exceptions.AutomationException;
import endpoints.LocationEndpoints;
import enums.UserRole;
import io.restassured.response.Response;

public class LocationService {
    private final UserRole role;
    private Response apiResponse;

    private LocationService(UserRole role) {
        this.role = role;
    }

    public static LocationService init(UserRole role) {
        return new LocationService(role);
    }

    public LocationService getAllLocations() throws AutomationException {
        apiResponse = ApiClient.init()
                .path(LocationEndpoints.GET_ALL_LOCATION)
                .auth(role)
                .get()
                .response();

        return this;
    }

    public Response getResponse() {
        return apiResponse;
    }
}
