package api;

public class Endpoints {
    //=============Auth endpoints=============
    public static final String LOGIN = "/api/auth/login";
    public static final String REGISTER = "/api/auth/register";
    public static final String FORGOT_PASSWORD = "/api/auth/forgot-password";

    //=============User endpoints=============
    public static final String USER_ENDPOINT = "/api/users";
    public static final String DELETE_USER = "/api/users/{id}";
    public static final String GET_USER_BY_USERNAME = "/api/users/username/{name}";
    public static final String CHANGE_PASSWORD = "/api/users/{id}/password";

    //=============Location endpoints=============
    public static final String LOCATION_ENDPOINT = "/api/location";

    //=============Amenity endpoints=============
    public static final String AMENITY_ENDPOINT = "/api/amenities";
}
