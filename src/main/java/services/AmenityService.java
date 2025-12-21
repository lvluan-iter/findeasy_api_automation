package services;

import api.ApiClient;
import api.Endpoints;
import enums.UserRole;
import io.restassured.response.Response;
import lombok.Getter;
import models.Amenity;

public class AmenityService {
    private final UserRole role;
    @Getter
    private Response response;

    public AmenityService(UserRole role) {
        this.role = role;
    }

    public static AmenityService init(UserRole role) {
        return new AmenityService(role);
    }

    public AmenityService createAmenity(Amenity payload) {
        response = ApiClient.init()
                .path(Endpoints.AMENITY_ENDPOINT)
                .auth(role)
                .body(payload)
                .post()
                .response();
        return this;
    }

    public AmenityService updateAmenity(Amenity payload) {
        response = ApiClient.init()
                .path(Endpoints.AMENITY_ENDPOINT + "/{id}")
                .pathParam("id",
                        payload.getId()
                                .toString())
                .auth(role)
                .body(payload)
                .put()
                .response();
        return this;
    }

    public AmenityService deleteAmenity(Long id) {
        response = ApiClient.init()
                .path(Endpoints.AMENITY_ENDPOINT + "/{id}")
                .pathParam("id", id.toString())
                .auth(role)
                .delete()
                .response();
        return this;
    }

}
