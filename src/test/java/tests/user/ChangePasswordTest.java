package tests.user;

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
import models.ChangePasswordRequest;
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

@Epic("User Management")
@Feature("Change Password")
public class ChangePasswordTest {

    private AuthService authService;
    private UserService userService;
    private UserService adminUserService;

    private RegisterRequest registerData;
    private Long createdUserId;

    @BeforeClass(alwaysRun = true)
    public void setUp() {
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

        Response registerResponse = authService
                .register(registerData);

        createdUserId = AssertApiResponse.assertThat(registerResponse)
                .status(HttpStatus.CREATED)
                .succeeded()
                .resultAs(User.class)
                .getId();

        userService = UserService.init()
                .auth(registerData.getUsername(), registerData.getPassword());
    }

    @Test(
            testName = "Verify user can change password successfully",
            groups = {"smoke"}
    )
    @Severity(SeverityLevel.BLOCKER)
    public void verifyUserCanChangePasswordSuccessfully() {
        ChangePasswordRequest payload = ChangePasswordRequest.builder()
                .currentPassword(registerData.getPassword())
                .newPassword(DataGenerateUtils.password())
                .build();

        Response response = userService
                .changePassword(createdUserId, payload);

        AssertApiResponse.assertThat(response)
                .status(HttpStatus.OK)
                .succeeded();
    }

    @Test(
            testName = "Verify user cannot change password when new password is same as current password",
            groups = {"regression"}
    )
    @Severity(SeverityLevel.CRITICAL)
    public void verifyUserCannotChangePasswordWhenSamePassword() {
        ChangePasswordRequest payload = ChangePasswordRequest.builder()
                .currentPassword(registerData.getPassword())
                .newPassword(registerData.getPassword())
                .build();

        Response response = userService
                .changePassword(createdUserId, payload);

        AssertApiResponse.assertThat(response)
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .failed()
                .errorContains(ErrorMessages.NEW_PASSWORD_MUST_BE_DIFFERENT_CURRENT);
    }

    @Test(
            testName = "Verify user cannot change password when new password does not meet requirement",
            groups = {"regression"}
    )
    @Severity(SeverityLevel.CRITICAL)
    public void verifyUserCannotChangePasswordNotMeetRequirement() {
        ChangePasswordRequest payload = ChangePasswordRequest.builder()
                .currentPassword(registerData.getPassword())
                .newPassword(Randomizer.randomAlphabets(8))
                .build();

        Response response = userService
                .changePassword(createdUserId, payload);

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