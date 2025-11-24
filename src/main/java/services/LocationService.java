package services;

import api.ApiClient;
import api.Endpoints;
import enums.UserRole;
import exceptions.AutomationException;
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
                .path(Endpoints.LOCATION_ENDPOINT)
                .auth(role)
                .get()
                .response();

        return this;
    }

    public LocationService createLocation(Object payload) throws AutomationException {
        apiResponse = ApiClient.init()
                .path(Endpoints.LOCATION_ENDPOINT)
                .auth(role)
                .body(payload)
                .post()
                .response();
        return this;
    }

    public LocationService updateLocation(Long locationId, Object payload) throws AutomationException {
        apiResponse = ApiClient.init()
                .path(Endpoints.LOCATION_ENDPOINT + "/{id}")
                .pathParam("id", locationId.toString())
                .auth(role)
                .body(payload)
                .put()
                .response();
        return this;
    }

    public LocationService deleteLocation(Long locationId) throws AutomationException {
        apiResponse = ApiClient.init()
                .auth(role)
                .path(Endpoints.LOCATION_ENDPOINT + "/{id}")
                .pathParam("id", locationId.toString())
                .delete()
                .response();
        return this;
    }

    public Response getResponse() {
        return apiResponse;
    }
}
