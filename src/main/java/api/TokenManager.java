package api;

import constants.PathConstants;
import enums.HttpStatus;
import enums.UserRole;
import exceptions.AutomationException;
import io.restassured.response.Response;
import models.JwtAuthenticate;
import models.LoginRequest;
import models.User;
import utils.JsonUtils;

import java.util.EnumMap;
import java.util.Map;

public class TokenManager {

    private static final Map<UserRole, String> tokens = new EnumMap<>(UserRole.class);
    private static final Map<UserRole, Long> tokenExpiry = new EnumMap<>(UserRole.class);

    public static synchronized String getToken(UserRole role) throws AutomationException {
        if (role == UserRole.GUEST) return null;

        if (!tokens.containsKey(role) || isExpired(role)) {
            refreshToken(role);
        }

        return tokens.get(role);
    }

    private static boolean isExpired(UserRole role) {
        return System.currentTimeMillis() >= tokenExpiry.getOrDefault(role, 0L);
    }

    private static void refreshToken(UserRole role) throws AutomationException {
        LoginRequest creds = buildLoginRequestByRole(role);

        Response response = ApiClient.init()
                .path(Endpoints.LOGIN)
                .body(creds)
                .post()
                .response();
        JwtAuthenticate jwt = AssertApiResponse.assertThat(response)
                .status(HttpStatus.OK)
                .succeeded()
                .resultAs(JwtAuthenticate.class);

        tokens.put(role,
                jwt.getToken());
        tokenExpiry.put(role,
                jwt.getExpire());
    }

    private static LoginRequest buildLoginRequestByRole(UserRole role) {
        User userData = JsonUtils.fromFileByKey(PathConstants.ACCOUNT_JSON,
                role.getRoleName(),
                User.class
        );
        return new LoginRequest(userData.getUsername(), userData.getPassword());
    }
}