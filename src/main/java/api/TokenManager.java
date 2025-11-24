package api;

import com.fasterxml.jackson.core.type.TypeReference;
import constants.PathConstants;
import enums.UserRole;
import exceptions.AutomationException;
import models.ApiResponse;
import models.JwtAuthenticate;
import models.LoginRequest;
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
        LoginRequest creds = getLoginRequestByRole(role);

        ApiResponse<JwtAuthenticate> response = ApiClient.init()
                .path(Endpoints.LOGIN)
                .body(creds)
                .post()
                .toPojo(new TypeReference<>() {
                });

        tokens.put(role,
                response.getResult()
                        .getToken());
        tokenExpiry.put(role,
                response.getResult()
                        .getExpire());
    }

    private static LoginRequest getLoginRequestByRole(UserRole role) {
        return JsonUtils.readJson(PathConstants.ACCOUNT_JSON,
                LoginRequest.class,
                role.getRoleName());
    }
}