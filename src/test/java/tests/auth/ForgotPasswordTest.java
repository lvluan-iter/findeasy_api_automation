package tests.auth;

import core.api.AssertApiResponse;
import core.constants.ErrorMessages;
import core.constants.PathConstants;
import core.exceptions.AutomationException;
import core.utils.JsonUtils;
import core.utils.Randomizer;
import enums.UserRole;
import io.restassured.response.Response;
import listeners.TestListener;
import models.ForgotPasswordRequest;
import models.User;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import services.AuthService;

@Listeners(TestListener.class)
public class ForgotPasswordTest {

    private AuthService authService;
    private User userData;

    @BeforeClass(alwaysRun = true)
    public void setUp() throws AutomationException {
        userData = JsonUtils.readJson(
                PathConstants.ACCOUNT_JSON,
                User.class,
                UserRole.USER.getRoleName()
        );
        authService = AuthService.init();
    }

    @Test(description = "Verify user can get link to reset password successfully")
    public void verifyUserCanGetLinkToResetPasswordSuccessfully() throws AutomationException {
        ForgotPasswordRequest payload = new ForgotPasswordRequest(userData.getEmail());

        Response response = authService.forgotPassword(payload)
                .getResponse();
        AssertApiResponse.success(response);
    }

    @Test(description = "Verify user cannot get reset link when email does not exist")
    public void verifyUserCannotGetLinkWhenUserNotFound() throws AutomationException {
        String email = Randomizer.randomEmail();
        ForgotPasswordRequest payload = new ForgotPasswordRequest(email);

        Response response = authService.forgotPassword(payload)
                .getResponse();
        AssertApiResponse.notFound(response, ErrorMessages.USER_NOT_FOUND_WITH_EMAIL + email);
    }
}