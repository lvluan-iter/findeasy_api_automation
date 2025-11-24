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
import models.RegisterRequest;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import services.AuthService;
import services.UserService;
import utils.JsonUtils;

@Epic("Authentication")
@Feature("Register")
public class RegisterTest {

    private RegisterRequest registerData;
    private RegisterRequest adminData;
    private AuthService authService;
    private UserService userServiceForAdmin;
    private Long createdUserId;

    @BeforeClass(alwaysRun = true)
    public void init() {
        registerData = JsonUtils.readJson(
                PathConstants.ACCOUNT_JSON,
                RegisterRequest.class,
                UserRole.GUEST.getRoleName()
        );

        adminData = JsonUtils.readJson(
                PathConstants.ACCOUNT_JSON,
                RegisterRequest.class,
                UserRole.ADMIN.getRoleName()
        );

        authService = AuthService.init();
        userServiceForAdmin = UserService.init(UserRole.ADMIN);
    }

    @AfterMethod(alwaysRun = true)
    public void cleanup() throws AutomationException {
        if (createdUserId != null) {
            userServiceForAdmin.deleteUser(createdUserId);
            createdUserId = null;
        }
    }

    @Test(
            description = "Verify user can register successfully with all fields",
            groups = {"smoke", "regression"}
    )
    @Severity(SeverityLevel.BLOCKER)
    public void verifyUserCanRegisterSuccessfullyWithRequiredData() throws AutomationException {

        Response registerResponse = authService.register(registerData)
                .getResponse();

        AssertApiResponse.createSuccess(registerResponse);
        createdUserId = registerResponse.jsonPath()
                .getLong("result.id");
    }

    @Test(
            description = "Verify user can register successfully with only required fields",
            groups = {"smoke", "regression"}
    )
    @Severity(SeverityLevel.BLOCKER)
    public void verifyUserCanRegisterSuccessfullyWithRequiredField() throws AutomationException {

        RegisterRequest payload = new RegisterRequest(
                registerData.getUsername(),
                registerData.getEmail(),
                registerData.getPassword(),
                registerData.getFullname(),
                registerData.getPhoneNumber(),
                null,
                null
        );

        Response registerResponse = authService.register(payload)
                .getResponse();

        AssertApiResponse.createSuccess(registerResponse);
        createdUserId = registerResponse.jsonPath()
                .getLong("result.id");
    }

    @Test(
            description = "Verify user cannot register with existing username field",
            groups = {"regression"}
    )
    @Severity(SeverityLevel.CRITICAL)
    public void verifyUserCanRegisterWithExistingUsernameField() throws AutomationException {

        RegisterRequest payload = new RegisterRequest(
                adminData.getUsername(),
                registerData.getEmail(),
                registerData.getPassword(),
                registerData.getFullname(),
                registerData.getPhoneNumber(),
                null,
                null
        );

        Response registerResponse = authService.register(payload)
                .getResponse();

        AssertApiResponse.conflict(registerResponse, ErrorMessages.USERNAME_ALREADY_EXISTS);
    }

    @Test(
            description = "Verify user cannot register with existing email field",
            groups = {"regression"}
    )
    @Severity(SeverityLevel.CRITICAL)
    public void verifyUserCanRegisterWithExistingEmailField() throws AutomationException {

        RegisterRequest payload = new RegisterRequest(
                registerData.getUsername(),
                adminData.getEmail(),
                registerData.getPassword(),
                registerData.getFullname(),
                registerData.getPhoneNumber(),
                null,
                null
        );

        Response registerResponse = authService.register(payload)
                .getResponse();

        AssertApiResponse.conflict(registerResponse, ErrorMessages.EMAIL_ALREADY_EXISTS);
    }

    @Test(
            description = "Verify user cannot register with invalid password field",
            groups = {"regression"}
    )
    @Severity(SeverityLevel.CRITICAL)
    public void verifyUserCanRegisterWithInvalidPasswordField() throws AutomationException {

        RegisterRequest payload = new RegisterRequest(
                registerData.getUsername(),
                registerData.getEmail(),
                "abc",
                registerData.getFullname(),
                registerData.getPhoneNumber(),
                null,
                null
        );

        Response registerResponse = authService.register(payload)
                .getResponse();

        AssertApiResponse.internalServerError(registerResponse, ErrorMessages.INVALID_PASSWORD);
    }
}