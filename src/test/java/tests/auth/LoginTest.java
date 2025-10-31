package tests.auth;

import core.api.AssertApiResponse;
import core.base.TestListener;
import core.constants.PathConstants;
import core.exceptions.AutomationException;
import core.utils.JsonUtils;
import enums.ErrorMessages;
import enums.UserRole;
import io.restassured.response.Response;
import models.LoginRequest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import services.AuthService;

@Listeners(TestListener.class)
public class LoginTest {
    private AuthService user;
    private LoginRequest adminData;
    private LoginRequest userData;

    @BeforeClass
    public void init() {
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

        user = new AuthService(UserRole.anonymous);
    }

    @Test(description = "Verify admin can login successfully", groups = {"smoke"})
    public void verifyAdminCanLoginSuccessfully() throws AutomationException {
        Response loginResponse = user.login(adminData.getUsername(), adminData.getPassword());
        AssertApiResponse.success(loginResponse);
    }

    @Test(description = "Verify user can login successfully", groups = {"smoke"})
    public void verifyUserCanLoginSuccessfully() {
        Response loginResponse = user.login(userData.getUsername(), userData.getPassword());
        AssertApiResponse.success(loginResponse);
    }

    @Test(description = "Verify admin cannot login with invalid username or password")
    public void verifyAdminCannotLoginWithInvalidUsernameOrPassword() {
        Response loginResponse = user.login(adminData.getUsername(), adminData.getUsername());
        AssertApiResponse.badRequest(loginResponse, ErrorMessages.BAD_CREDENTIALS);
    }

    @Test(description = "Verify admin cannot login with null username or password")
    public void verifyAdminCannotLoginWithNullUsernameOrPassword() {
        Response loginResponse = user.login(adminData.getUsername(), null);
        AssertApiResponse.badRequest(loginResponse, ErrorMessages.BAD_CREDENTIALS);
    }
}