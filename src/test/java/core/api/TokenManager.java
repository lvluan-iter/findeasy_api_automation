package core.api;

import com.fasterxml.jackson.core.type.TypeReference;
import core.constants.HttpStatus;
import core.constants.PathConstants;
import core.exceptions.AutomationException;
import core.utils.JsonUtils;
import enums.UserRole;
import io.restassured.http.ContentType;
import models.ApiResponse;
import models.JwtAuthenticate;
import models.LoginRequest;

import java.util.EnumMap;
import java.util.Map;

public class TokenManager {

    private static final Map<UserRole, String> token = new EnumMap<>(UserRole.class);
    private static final Map<UserRole, Long> expire = new EnumMap<>(UserRole.class);

    public static String getToken(UserRole role) throws AutomationException {
        if (role == UserRole.anonymous) return null;

        if (!token.containsKey(role) || isExpired(role)) {
            refreshToken(role);
        }
        return token.get(role);
    }

    private static boolean isExpired(UserRole role) {
        return System.currentTimeMillis() > expire.getOrDefault(role, 0L);
    }

    private static void refreshToken(UserRole role) throws AutomationException {
        LoginRequest payload = getLoginPayload(role);

        ApiResponse<JwtAuthenticate> response = ApiClient.init()
                .contentType(ContentType.JSON)
                .body(payload)
                .expectedStatusCode(HttpStatus.OK)
                .post()
                .responseToPojo(new TypeReference<>() {
                });

        JwtAuthenticate jwt = response.getResult();
        token.put(role, jwt.getToken());
        expire.put(role, jwt.getExpire());
    }

    private static LoginRequest getLoginPayload(UserRole role) {
        return JsonUtils.readJson(PathConstants.ACCOUNT_JSON, LoginRequest.class, role.toString());
    }
}