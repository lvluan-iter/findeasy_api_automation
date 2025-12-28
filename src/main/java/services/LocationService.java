package services;

import api.ApiClient;
import api.Endpoints;
import api.TokenManager;
import exceptions.AutomationException;
import io.restassured.response.Response;

public class LocationService {
    private String token;

    public static LocationService init() {
        return new LocationService();
    }

    public LocationService auth(String username, String password) {
        token = TokenManager.getToken(username, password);
        return this;
    }

    public Response getAllLocations() throws AutomationException {
        return ApiClient.init()
                .path(Endpoints.LOCATION_ENDPOINT)
                .auth(token)
                .get()
                .response();
    }

    public Response createLocation(Object payload) throws AutomationException {
        return ApiClient.init()
                .path(Endpoints.LOCATION_ENDPOINT)
                .auth(token)
                .body(payload)
                .post()
                .response();
    }

    public Response updateLocation(Long locationId, Object payload) throws AutomationException {
        return ApiClient.init()
                .path(Endpoints.LOCATION_ENDPOINT + "/{id}")
                .pathParam("id", locationId.toString())
                .auth(token)
                .body(payload)
                .put()
                .response();
    }

    public Response deleteLocation(Long locationId) throws AutomationException {
        return ApiClient.init()
                .auth(token)
                .path(Endpoints.LOCATION_ENDPOINT + "/{id}")
                .pathParam("id", locationId.toString())
                .delete()
                .response();
    }
}
