package tests.auth;

import core.api.AssertApiResponse;
import core.base.TestListener;
import core.constants.PathConstants;
import core.utils.JsonUtils;
import enums.ErrorMessages;
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
    private AuthService auth;
    private UserService userService;
    private Long createdUserId;

    @BeforeClass
    public void init() {
        registerData = JsonUtils.readJson(PathConstants.ACCOUNT_JSON, RegisterRequest.class, "anonymous");
        adminData = JsonUtils.readJson(PathConstants.ACCOUNT_JSON, RegisterRequest.class, "admin");
        auth = new AuthService(UserRole.anonymous);
        userService = new UserService(UserRole.admin);
    }

    @AfterMethod
    public void cleanup() {
        if (createdUserId != null) {
            userService.deleteUser(createdUserId);
            createdUserId = null;
        }
    }

    @Test(description = "Verify user can register successfully with all fields")
    public void verifyUserCanRegisterSuccessfullyWithRequiredData() {

        Response registerResponse = auth.register(
                registerData.getUsername(),
                registerData.getEmail(),
                registerData.getPassword(),
                registerData.getFullname(),
                registerData.getPhoneNumber(),
                registerData.getGender(),
                registerData.getBirthdate()
        );

        AssertApiResponse.createSuccess(registerResponse);
        createdUserId = registerResponse.jsonPath().getLong("result.id");
    }

    @Test(description = "Verify user can register successfully with only required fields")
    public void verifyUserCanRegisterSuccessfullyWithRequiredField() {

        Response registerResponse = auth.register(
                registerData.getUsername(),
                registerData.getEmail(),
                registerData.getPassword(),
                registerData.getFullname(),
                registerData.getPhoneNumber(),
                null,
                null
        );

        AssertApiResponse.createSuccess(registerResponse);
        createdUserId = registerResponse.jsonPath().getLong("result.id");
    }

    @Test(description = "Verify user cannot register with existing username fields")
    public void verifyUserCanRegisterWithExistingUsernameField() {

        Response registerResponse = auth.register(
                adminData.getUsername(),
                registerData.getEmail(),
                registerData.getPassword(),
                registerData.getFullname(),
                registerData.getPhoneNumber(),
                null,
                null
        );

        AssertApiResponse.conflict(registerResponse, ErrorMessages.USERNAME_ALREADY_EXISTS);
    }

    @Test(description = "Verify user cannot register with existing email fields")
    public void verifyUserCanRegisterWithExistingEmailField() {

        Response registerResponse = auth.register(
                registerData.getUsername(),
                adminData.getEmail(),
                registerData.getPassword(),
                registerData.getFullname(),
                registerData.getPhoneNumber(),
                null,
                null
        );

        AssertApiResponse.conflict(registerResponse, ErrorMessages.EMAIL_ALREADY_EXISTS);
    }

    @Test(description = "Verify user cannot register with invalid password fields")
    public void verifyUserCanRegisterWithInvalidPasswordField() {

        Response registerResponse = auth.register(
                registerData.getUsername(),
                registerData.getEmail(),
                "abc",
                registerData.getFullname(),
                registerData.getPhoneNumber(),
                null,
                null
        );

        AssertApiResponse.unknown(registerResponse, ErrorMessages.INVALID_PASSWORD);
    }
}