package tests.auth;

import core.api.AssertApiResponse;
import core.base.TestListener;
import core.constants.PathConstants;
import core.exceptions.AutomationException;
import core.utils.JsonUtils;
import enums.ErrorMessages;
import io.restassured.response.Response;
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

    @BeforeClass
    public void setUp() {
        adminData = JsonUtils.readJson(
                PathConstants.ACCOUNT_JSON,
                LoginRequest.class,
                "admin"
        );

        userData = JsonUtils.readJson(
                PathConstants.ACCOUNT_JSON,
                LoginRequest.class,
                "user"
        );

        authService = AuthService.init();
    }

    @Test(description = "Verify admin can login successfully", groups = {"smoke"})
    public void verifyAdminCanLoginSuccessfully() throws AutomationException {
        Response loginResponse = authService
                .login(adminData.getUsername(), adminData.getPassword())
                .getResponse();

        AssertApiResponse.success(loginResponse);
    }

    @Test(description = "Verify user can login successfully", groups = {"smoke"})
    public void verifyUserCanLoginSuccessfully() throws AutomationException {
        Response loginResponse = authService
                .login(userData.getUsername(), userData.getPassword())
                .getResponse();

        AssertApiResponse.success(loginResponse);
    }

    @Test(description = "Verify admin cannot login with invalid username or password", groups = {"negative"})
    public void verifyAdminCannotLoginWithInvalidUsernameOrPassword() throws AutomationException {
        Response loginResponse = authService
                .login(adminData.getUsername(), adminData.getUsername())
                .getResponse();

        AssertApiResponse.badRequest(loginResponse, ErrorMessages.BAD_CREDENTIALS);
    }

    @Test(description = "Verify admin cannot login with null username or password", groups = {"negative"})
    public void verifyAdminCannotLoginWithNullUsernameOrPassword() throws AutomationException {
        Response loginResponse = authService
                .login(adminData.getUsername(), null)
                .getResponse();

        AssertApiResponse.badRequest(loginResponse, ErrorMessages.BAD_CREDENTIALS);
    }
}