package tests.auth;

import core.api.AssertApiResponse;
import core.constants.ErrorMessages;
import core.constants.PathConstants;
import core.exceptions.AutomationException;
import core.utils.JsonUtils;
import enums.UserRole;
import io.restassured.response.Response;
import listeners.TestListener;
import models.LoginRequest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import services.AuthService;

@Listeners(TestListener.class)
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

    @Test(description = "Verify admin can login successfully", groups = {"smoke"})
    public void verifyAdminCanLoginSuccessfully() throws AutomationException {
        Response loginResponse = authService
                .login(adminData)
                .getResponse();

        AssertApiResponse.success(loginResponse);
    }

    @Test(description = "Verify user can login successfully", groups = {"smoke"})
    public void verifyUserCanLoginSuccessfully() throws AutomationException {
        Response loginResponse = authService
                .login(userData)
                .getResponse();

        AssertApiResponse.success(loginResponse);
    }

    @Test(description = "Verify admin cannot login with invalid username or password", groups = {"negative"})
    public void verifyAdminCannotLoginWithInvalidUsernameOrPassword() throws AutomationException {
        LoginRequest invalidPayload = new LoginRequest(adminData.getUsername(), userData.getUsername());
        Response loginResponse = authService
                .login(invalidPayload)
                .getResponse();

        AssertApiResponse.badRequest(loginResponse, ErrorMessages.BAD_CREDENTIALS);
    }

    @Test(description = "Verify admin cannot login with null username or password", groups = {"negative"})
    public void verifyAdminCannotLoginWithNullUsernameOrPassword() throws AutomationException {
        LoginRequest invalidPayload = new LoginRequest(adminData.getUsername(), null);
        Response loginResponse = authService
                .login(invalidPayload)
                .getResponse();

        AssertApiResponse.badRequest(loginResponse, ErrorMessages.BAD_CREDENTIALS);
    }
}