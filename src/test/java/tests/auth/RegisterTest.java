package tests.auth;

import core.api.AssertApiResponse;
import core.base.TestListener;
import core.constants.ErrorMessages;
import core.constants.PathConstants;
import core.exceptions.AutomationException;
import core.utils.JsonUtils;
import enums.UserRole;
import io.restassured.response.Response;
import models.RegisterRequest;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import services.AuthService;
import services.UserService;

@Listeners(TestListener.class)
public class RegisterTest {

    private RegisterRequest registerData;
    private RegisterRequest adminData;
    private AuthService authService;
    private UserService userServiceForAdmin;
    private Long createdUserId;

    @BeforeClass(alwaysRun = true)
    public void init() {
        registerData = JsonUtils.readJson(PathConstants.ACCOUNT_JSON, RegisterRequest.class, "anonymous");
        adminData = JsonUtils.readJson(PathConstants.ACCOUNT_JSON, RegisterRequest.class, "admin");
        authService = AuthService.init();
        userServiceForAdmin = UserService.init(UserRole.ADMIN);
    }

    @AfterMethod
    public void cleanup() throws AutomationException {
        if (createdUserId != null) {
            userServiceForAdmin.deleteUser(createdUserId);
            createdUserId = null;
        }
    }

    @Test(description = "Verify user can register successfully with all fields")
    public void verifyUserCanRegisterSuccessfullyWithRequiredData() throws AutomationException {

        Response registerResponse = authService.register(registerData)
                .getResponse();

        AssertApiResponse.createSuccess(registerResponse);
        createdUserId = registerResponse.jsonPath()
                .getLong("result.id");
    }

    @Test(description = "Verify user can register successfully with only required fields")
    public void verifyUserCanRegisterSuccessfullyWithRequiredField() throws AutomationException {
        RegisterRequest payload = new RegisterRequest(registerData.getUsername(),
                registerData.getEmail(),
                registerData.getPassword(),
                registerData.getFullname(),
                registerData.getPhoneNumber(),
                null,
                null);
        Response registerResponse = authService.register(
                        payload
                )
                .getResponse();

        AssertApiResponse.createSuccess(registerResponse);
        createdUserId = registerResponse.jsonPath()
                .getLong("result.id");
    }

    @Test(description = "Verify user cannot register with existing username fields")
    public void verifyUserCanRegisterWithExistingUsernameField() throws AutomationException {
        RegisterRequest payload = new RegisterRequest(adminData.getUsername(),
                registerData.getEmail(),
                registerData.getPassword(),
                registerData.getFullname(),
                registerData.getPhoneNumber(),
                null,
                null);
        Response registerResponse = authService.register(
                        payload
                )
                .getResponse();

        AssertApiResponse.conflict(registerResponse, ErrorMessages.USERNAME_ALREADY_EXISTS);
    }

    @Test(description = "Verify user cannot register with existing email fields")
    public void verifyUserCanRegisterWithExistingEmailField() throws AutomationException {
        RegisterRequest payload = new RegisterRequest(registerData.getUsername(),
                adminData.getEmail(),
                registerData.getPassword(),
                registerData.getFullname(),
                registerData.getPhoneNumber(),
                null,
                null);
        Response registerResponse = authService.register(
                        payload
                )
                .getResponse();

        AssertApiResponse.conflict(registerResponse, ErrorMessages.EMAIL_ALREADY_EXISTS);
    }

    @Test(description = "Verify user cannot register with invalid password fields")
    public void verifyUserCanRegisterWithInvalidPasswordField() throws AutomationException {
        RegisterRequest payload = new RegisterRequest(registerData.getUsername(),
                registerData.getEmail(),
                "abc",
                registerData.getFullname(),
                registerData.getPhoneNumber(),
                null,
                null);
        Response registerResponse = authService.register(
                        payload
                )
                .getResponse();

        AssertApiResponse.unknown(registerResponse, ErrorMessages.INVALID_PASSWORD);
    }
}