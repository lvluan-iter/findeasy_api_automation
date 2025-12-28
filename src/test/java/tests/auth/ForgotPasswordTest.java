package tests.auth;

import api.AssertApiResponse;
import constants.ErrorMessages;
import constants.PathConstants;
import enums.HttpStatus;
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
import utils.DataGenerateUtils;
import utils.JsonUtils;

@Epic("Authentication")
@Feature("Forgot Password")
public class ForgotPasswordTest {
    private AuthService authService;
    private User userData;

    @BeforeClass(alwaysRun = true)
    public void setUp() {
        userData = JsonUtils.fromFileByKey(
                PathConstants.ACCOUNT_JSON,
                UserRole.USER.getRoleName(),
                User.class
        );
        authService = AuthService.init();
    }

    @Test(
            testName = "Verify user can get link to reset password successfully",
            groups = {"smoke"}
    )
    @Severity(SeverityLevel.BLOCKER)
    public void verifyUserCanGetLinkToResetPasswordSuccessfully() throws AutomationException {
        ForgotPasswordRequest payload = new ForgotPasswordRequest(userData.getEmail());

        Response response = authService.forgotPassword(payload);

        AssertApiResponse.assertThat(response)
                .status(HttpStatus.OK)
                .succeeded();
    }

    @Test(
            testName = "Verify user cannot get reset link when email does not exist",
            groups = {"regression"}
    )
    @Severity(SeverityLevel.CRITICAL)
    public void verifyUserCannotGetLinkWhenUserNotFound() throws AutomationException {
        String email = DataGenerateUtils.email();
        ForgotPasswordRequest payload = new ForgotPasswordRequest(email);

        Response response = authService.forgotPassword(payload);

        AssertApiResponse.assertThat(response)
                .status(HttpStatus.NOT_FOUND)
                .errorContains(ErrorMessages.USER_NOT_FOUND_WITH_EMAIL + email);
    }
}