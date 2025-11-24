package tests.user;

import api.AssertApiResponse;
import enums.UserRole;
import exceptions.AutomationException;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import services.UserService;

@Epic("User Management")
@Feature("Get All Users")
public class GetAllUsersTest {

    private UserService userServiceForAdmin;
    private UserService userService;

    @BeforeClass(alwaysRun = true)
    public void setUp() {
        userServiceForAdmin = UserService.init(UserRole.ADMIN);
        userService = UserService.init(UserRole.USER);
    }

    @Test(
            description = "Verify admin can get user list successfully",
            groups = {"smoke", "regression"}
    )
    @Severity(SeverityLevel.BLOCKER)
    public void verifyAdminCanGetUserList() throws AutomationException {
        Response response = userServiceForAdmin
                .getAllUsers()
                .getResponse();

        AssertApiResponse.success(response);
    }

    @Test(
            description = "Verify user can also get user list successfully",
            groups = {"smoke", "regression"}
    )
    @Severity(SeverityLevel.CRITICAL)
    public void verifyUserCanGetUserList() throws AutomationException {
        Response response = userService
                .getAllUsers()
                .getResponse();

        AssertApiResponse.success(response);
    }
}