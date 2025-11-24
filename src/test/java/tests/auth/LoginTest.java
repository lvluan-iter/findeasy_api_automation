package tests.auth;

import api.AssertApiResponse;
import constants.ErrorMessages;
import constants.PathConstants;
import enums.UserRole;
import exceptions.AutomationException;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.restassured.response.Response;
import models.LoginRequest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import services.AuthService;
import utils.JsonUtils;

@Epic("Authentication")
@Feature("Login")
public class LoginTest {

    private LoginRequest adminData;
    private LoginRequest userData;
    private AuthService authService;

    @BeforeClass(alwaysRun = true)
    public void setUp() {
        adminData = JsonUtils.readJson(
                PathConstants.ACCOUNT_JSON,
                LoginRequest.class,
                UserRole.ADMIN.getRoleName()
        );

        userData = JsonUtils.readJson(
                PathConstants.ACCOUNT_JSON,
                LoginRequest.class,
                UserRole.USER.getRoleName()
        );

        authService = AuthService.init();
    }

    @Test(
            description = "Verify admin can login successfully",
            groups = {"smoke", "regression"}
    )
    @Severity(SeverityLevel.BLOCKER)
    public void verifyAdminCanLoginSuccessfully() throws AutomationException {
        Response loginResponse = authService
                .login(adminData)
                .getResponse();

        AssertApiResponse.success(loginResponse);
    }

    @Test(
            description = "Verify user can login successfully",
            groups = {"smoke", "regression"}
    )
    @Severity(SeverityLevel.BLOCKER)
    public void verifyUserCanLoginSuccessfully() throws AutomationException {
        Response loginResponse = authService
                .login(userData)
                .getResponse();

        AssertApiResponse.success(loginResponse);
    }

    @Test(
            description = "Verify admin cannot login with invalid username or password",
            groups = {"regression"}
    )
    @Severity(SeverityLevel.CRITICAL)
    public void verifyAdminCannotLoginWithInvalidUsernameOrPassword() throws AutomationException {
        LoginRequest invalidPayload = new LoginRequest(adminData.getUsername(), userData.getUsername());

        Response loginResponse = authService
                .login(invalidPayload)
                .getResponse();

        AssertApiResponse.badRequest(loginResponse, ErrorMessages.BAD_CREDENTIALS);
    }

    @Test(
            description = "Verify admin cannot login with null username or password",
            groups = {"regression"}
    )
    @Severity(SeverityLevel.CRITICAL)
    public void verifyAdminCannotLoginWithNullUsernameOrPassword() throws AutomationException {
        LoginRequest invalidPayload = new LoginRequest(adminData.getUsername(), null);

        Response loginResponse = authService
                .login(invalidPayload)
                .getResponse();

        AssertApiResponse.badRequest(loginResponse, ErrorMessages.BAD_CREDENTIALS);
    }
}
