package tests.auth;

import core.api.AssertApiResponse;
import core.constants.FrameworkConstants;
import core.utils.JsonUtils;
import enums.UserRole;
import io.restassured.response.Response;
import models.RegisterRequest;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import services.AuthService;
import services.UserService;

public class RegisterTest {

    private RegisterRequest registerData;
    private AuthService auth;
    private UserService userService;
    private Long createdUserId;

    @BeforeClass
    public void init() {
        registerData = JsonUtils.readJson(FrameworkConstants.ACCOUNT_JSON, RegisterRequest.class, "anonymous");
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
}