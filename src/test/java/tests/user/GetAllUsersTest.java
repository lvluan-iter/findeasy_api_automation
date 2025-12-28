package tests.user;

import api.AssertApiResponse;
import constants.PathConstants;
import enums.HttpStatus;
import enums.UserRole;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.restassured.response.Response;
import models.User;
import org.testng.ITest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import services.UserService;
import utils.JsonUtils;

@Epic("User Management")
@Feature("Get All Users")
public class GetAllUsersTest implements ITest {

    private UserService adminService;
    private UserService userService;
    private UserService guestService;
    private String testName;

    @BeforeClass(alwaysRun = true)
    public void setUp() {
        User adminData = JsonUtils.fromFileByKey(PathConstants.ACCOUNT_JSON, UserRole.ADMIN.getRoleName(), User.class);
        User useData = JsonUtils.fromFileByKey(PathConstants.ACCOUNT_JSON, UserRole.USER.getRoleName(), User.class);

        adminService = UserService.init()
                .auth(adminData.getUsername(), adminData.getPassword());
        userService = UserService.init()
                .auth(useData.getUsername(), useData.getPassword());
        guestService = UserService.init();
    }

    @DataProvider(name = "getAllUsersServiceProvider")
    public Object[][] serviceData() {
        return new Object[][]{
                {"Verify admin can get user list successfully", adminService},
                {"Verify user can get user list successfully", userService},
                {"Verify guest can get user list successfully", guestService}
        };
    }

    @Test(
            dataProvider = "getAllUsersServiceProvider",
            groups = {"smoke"}
    )
    @Severity(SeverityLevel.CRITICAL)
    public void verifyGetUserListSuccessfully(String description, UserService service) {
        this.testName = description;

        Response response = service
                .getAllUsers();

        AssertApiResponse.assertThat(response)
                .status(HttpStatus.OK)
                .succeeded();
    }

    @Override
    public String getTestName() {
        return testName;
    }
}