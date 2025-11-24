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
import models.ForgotPasswordRequest;
import models.User;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import services.AuthService;
import utils.JsonUtils;
import utils.Randomizer;

@Epic("Authentication")
@Feature("Forgot Password")
public class ForgotPasswordTest {

    private AuthService authService;
    private User userData;

    @BeforeClass(alwaysRun = true)
    public void setUp() {
        userData = JsonUtils.readJson(
                PathConstants.ACCOUNT_JSON,
                User.class,
                UserRole.USER.getRoleName()
        );
        authService = AuthService.init();
    }

    @Test(
            description = "Verify user can get link to reset password successfully",
            groups = {"smoke", "regression"}
    )
    @Severity(SeverityLevel.BLOCKER)
    public void verifyUserCanGetLinkToResetPasswordSuccessfully() throws AutomationException {

        ForgotPasswordRequest payload = new ForgotPasswordRequest(userData.getEmail());

        Response response = authService.forgotPassword(payload)
                .getResponse();

        AssertApiResponse.success(response);
    }

    @Test(
            description = "Verify user cannot get reset link when email does not exist",
            groups = {"regression"}
    )
    @Severity(SeverityLevel.CRITICAL)
    public void verifyUserCannotGetLinkWhenUserNotFound() throws AutomationException {

        String email = Randomizer.randomEmail();
        ForgotPasswordRequest payload = new ForgotPasswordRequest(email);

        Response response = authService.forgotPassword(payload)
                .getResponse();

        AssertApiResponse.notFound(response, ErrorMessages.USER_NOT_FOUND_WITH_EMAIL + email);
    }
}