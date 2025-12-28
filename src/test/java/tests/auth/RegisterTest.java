package tests.auth;

import api.AssertApiResponse;
import constants.ErrorMessages;
import constants.PathConstants;
import enums.HttpStatus;
import enums.UserRole;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.restassured.response.Response;
import models.RegisterRequest;
import models.User;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import services.AuthService;
import services.UserService;
import utils.DataGenerateUtils;
import utils.JsonUtils;
import utils.Randomizer;

@Epic("Authentication")
@Feature("Register")
public class RegisterTest {

    private AuthService authService;
    private UserService adminUserService;

    private User existingUserData;
    private RegisterRequest registerData;
    private Long createdUserId;

    @BeforeClass(alwaysRun = true)
    public void setUp() {
        existingUserData = JsonUtils.fromFileByKey(
                PathConstants.ACCOUNT_JSON,
                UserRole.ADMIN.getRoleName(),
                User.class
        );

        User adminData = JsonUtils.fromFileByKey(PathConstants.ACCOUNT_JSON, UserRole.ADMIN.getRoleName(), User.class);

        authService = AuthService.init();
        adminUserService = UserService.init()
                .auth(adminData.getUsername(), adminData.getPassword());
    }

    @BeforeMethod(alwaysRun = true)
    public void prepareData() {
        registerData = RegisterRequest.builder()
                .username(DataGenerateUtils.username())
                .email(DataGenerateUtils.email())
                .password(DataGenerateUtils.password())
                .fullname(DataGenerateUtils.fullName())
                .gender(DataGenerateUtils.gender())
                .birthdate(DataGenerateUtils.birthday())
                .phoneNumber(DataGenerateUtils.phone())
                .build();
    }

    @Test(
            testName = "Verify user can register successfully with all fields",
            groups = {"smoke"}
    )
    @Severity(SeverityLevel.BLOCKER)
    public void verifyUserCanRegisterSuccessfullyWithAllFields() {
        Response response = authService
                .register(registerData);

        createdUserId = AssertApiResponse.assertThat(response)
                .status(HttpStatus.CREATED)
                .succeeded()
                .resultAs(User.class)
                .getId();
    }

    @Test(
            testName = "Verify user can register successfully with only required fields",
            groups = {"smoke"}
    )
    @Severity(SeverityLevel.BLOCKER)
    public void verifyUserCanRegisterSuccessfullyWithRequiredFields() {
        registerData.setGender(null);
        registerData.setBirthdate(null);

        Response response = authService
                .register(registerData);

        createdUserId = AssertApiResponse.assertThat(response)
                .status(HttpStatus.CREATED)
                .succeeded()
                .resultAs(User.class)
                .getId();
    }

    @Test(
            testName = "Verify user cannot register with existing username",
            groups = {"regression"}
    )
    @Severity(SeverityLevel.CRITICAL)
    public void verifyUserCannotRegisterWithExistingUsername() {
        registerData.setUsername(existingUserData.getUsername());

        Response response = authService
                .register(registerData);

        AssertApiResponse.assertThat(response)
                .status(HttpStatus.CONFLICT)
                .failed();
    }

    @Test(
            testName = "Verify user cannot register with existing email",
            groups = {"regression"}
    )
    @Severity(SeverityLevel.CRITICAL)
    public void verifyUserCannotRegisterWithExistingEmail() {
        registerData.setEmail(existingUserData.getEmail());

        Response response = authService
                .register(registerData);

        AssertApiResponse.assertThat(response)
                .status(HttpStatus.CONFLICT)
                .failed();
    }

    @Test(
            testName = "Verify user cannot register with invalid password",
            groups = {"regression"}
    )
    @Severity(SeverityLevel.CRITICAL)
    public void verifyUserCannotRegisterWithInvalidPassword() {
        registerData.setPassword(Randomizer.randomAlphabets(8));

        Response response = authService
                .register(registerData);

        AssertApiResponse.assertThat(response)
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .failed()
                .errorContains(ErrorMessages.INVALID_PASSWORD);
    }

    @AfterMethod(alwaysRun = true)
    public void cleanUp() {
        if (createdUserId != null) {
            Response response = adminUserService
                    .deleteUser(createdUserId);

            AssertApiResponse.assertThat(response)
                    .status(HttpStatus.OK)
                    .succeeded();

            createdUserId = null;
        }
    }
}