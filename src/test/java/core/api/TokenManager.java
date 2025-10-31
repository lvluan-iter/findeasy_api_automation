package core.api;

import core.constants.PathConstants;
import core.utils.JsonUtils;
import endpoints.AuthEndpoints;
import enums.RequestMode;
import enums.UserRole;
import io.restassured.response.Response;
import models.JwtAuthenticate;
import models.LoginRequest;

import java.util.EnumMap;
import java.util.Map;

public class TokenManager {

    private static final Map<UserRole, String> token = new EnumMap<>(UserRole.class);
    private static final Map<UserRole, Long> expire = new EnumMap<>(UserRole.class);

    public static String getToken(UserRole role) {
        if (role == UserRole.anonymous) return null;

        if (!token.containsKey(role) || isExpired(role)) {
            refreshToken(role);
        }
        return token.get(role);
    }

    private static boolean isExpired(UserRole role) {
        return System.currentTimeMillis() > expire.getOrDefault(role, 0L);
    }

    private static void refreshToken(UserRole role) {
        LoginRequest payload = getLoginPayload(role);

        Response res = ApiClient.getInstance(UserRole.anonymous)
                .post(AuthEndpoints.LOGIN, payload, RequestMode.JSON_NO_AUTH);
        JwtAuthenticate jwt = res.jsonPath().getObject("result", JwtAuthenticate.class);

        token.put(role, jwt.getToken());
        expire.put(role, jwt.getExpire());
    }

    private static LoginRequest getLoginPayload(UserRole role) {
        return JsonUtils.readJson(PathConstants.ACCOUNT_JSON, LoginRequest.class, role.toString());
    }
}