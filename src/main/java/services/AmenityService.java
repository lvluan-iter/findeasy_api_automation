package services;

import api.ApiClient;
import api.Endpoints;
import api.TokenManager;
import exceptions.AutomationException;
import io.restassured.response.Response;

public class AmenityService {
    private String token;

    public static AmenityService init() {
        return new AmenityService();
    }

    public AmenityService auth(String username, String password) {
        this.token = TokenManager.getToken(username, password);
        return this;
    }

    public Response getAllAmenities() throws AutomationException {
        return ApiClient.init()
                .path(Endpoints.AMENITY_ENDPOINT)
                .auth(token)
                .get()
                .response();
    }

    public Response createAmenity(Object payload) throws AutomationException {
        return ApiClient.init()
                .path(Endpoints.AMENITY_ENDPOINT)
                .auth(token)
                .body(payload)
                .post()
                .response();
    }

    public Response updateAmenity(Long amenityId, Object payload) throws AutomationException {
        return ApiClient.init()
                .path(Endpoints.AMENITY_ENDPOINT + "/{id}")
                .pathParam("id", amenityId.toString())
                .auth(token)
                .body(payload)
                .put()
                .response();
    }

    public Response deleteAmenity(Long amenityId) throws AutomationException {
        return ApiClient.init()
                .path(Endpoints.AMENITY_ENDPOINT + "/{id}")
                .pathParam("id", amenityId.toString())
                .auth(token)
                .delete()
                .response();
    }
}