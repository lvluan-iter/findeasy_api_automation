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
import models.LoginRequest;
import models.User;
import org.testng.ITest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import services.AuthService;
import utils.DataGenerateUtils;
import utils.JsonUtils;
import utils.Randomizer;

@Epic("Authentication")
@Feature("Login")
public class LoginTest implements ITest {

    private User adminData;
    private User userData;
    private AuthService authService;
    private String testName;

    @BeforeClass(alwaysRun = true)
    public void setUp() {
        adminData = JsonUtils.fromFileByKey(
                PathConstants.ACCOUNT_JSON,
                UserRole.ADMIN.getRoleName(),
                User.class
        );

        userData = JsonUtils.fromFileByKey(
                PathConstants.ACCOUNT_JSON,
                UserRole.USER.getRoleName(),
                User.class
        );

        authService = AuthService.init();
    }

    @DataProvider(name = "loginSuccessData")
    public Object[][] loginSuccessData() {
        return new Object[][]{
                {"Verify admin can login successfully", adminData},
                {"Verify user can login successfully", userData}
        };
    }

    @Test(
            dataProvider = "loginSuccessData",
            groups = {"smoke"}
    )
    @Severity(SeverityLevel.BLOCKER)
    public void verifyLoginSuccess(String description, User user) {

        this.testName = description;

        LoginRequest payload = LoginRequest.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .build();

        Response response = authService
                .login(payload);

        AssertApiResponse.assertThat(response)
                .status(HttpStatus.OK)
                .succeeded();
    }

    @DataProvider(name = "loginWrongPasswordData")
    public Object[][] loginWrongPasswordData() {
        return new Object[][]{
                {
                        "Verify admin cannot login with wrong password",
                        LoginRequest.builder()
                                .username(adminData.getUsername())
                                .password(DataGenerateUtils.password())
                                .build()
                },
                {
                        "Verify user cannot login with wrong password",
                        LoginRequest.builder()
                                .username(userData.getUsername())
                                .password(DataGenerateUtils.password())
                                .build()
                }
        };
    }

    @Test(
            dataProvider = "loginWrongPasswordData",
            groups = {"regression"}
    )
    @Severity(SeverityLevel.CRITICAL)
    public void verifyLoginFailWithWrongPassword(String description,
                                                 LoginRequest payload) {
        this.testName = description;

        Response response = authService
                .login(payload);

        AssertApiResponse.assertThat(response)
                .status(HttpStatus.BAD_REQUEST)
                .failed()
                .errorContains(ErrorMessages.BAD_CREDENTIALS);
    }

    @Test(testName = "Verify cannot login with non-exist user", groups = {"regression"})
    @Severity(SeverityLevel.CRITICAL)
    public void verifyLoginFailWithNonExistUser() {
        LoginRequest payload = LoginRequest.builder()
                .username(Randomizer.randomAlphabets(8))
                .password(DataGenerateUtils.password())
                .build();

        Response response = authService
                .login(payload);

        AssertApiResponse.assertThat(response)
                .status(HttpStatus.NOT_FOUND)
                .failed()
                .errorContains(ErrorMessages.BAD_CREDENTIALS);
    }

    @Override
    public String getTestName() {
        return testName;
    }
}