package api;

import enums.HttpStatus;
import io.restassured.response.Response;
import models.JwtAuthenticate;
import models.LoginRequest;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TokenManager {

    private static final Map<String, String> tokens = new ConcurrentHashMap<>();
    private static final Map<String, Long> tokenExpiry = new ConcurrentHashMap<>();

    public static String getToken(String username, String password) {
        if (!tokens.containsKey(username) || isExpired(username)) {
            refreshToken(username, password);
        }

        return tokens.get(username);
    }

    private static boolean isExpired(String username) {
        return System.currentTimeMillis() >= tokenExpiry.getOrDefault(username, 0L);
    }

    private static void refreshToken(String username, String password) {
        LoginRequest loginRequest = LoginRequest.builder()
                .username(username)
                .password(password)
                .build();

        Response response = ApiClient.init()
                .path(Endpoints.LOGIN)
                .body(loginRequest)
                .post()
                .response();
        JwtAuthenticate jwt = AssertApiResponse.assertThat(response)
                .status(HttpStatus.OK)
                .succeeded()
                .resultAs(JwtAuthenticate.class);

        tokens.put(username,
                jwt.getToken());
        tokenExpiry.put(username,
                jwt.getExpire());
    }
}