package tests.user;

import core.api.AssertApiResponse;
import core.exceptions.AutomationException;
import enums.UserRole;
import io.restassured.response.Response;
import listeners.TestListener;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import services.UserService;

@Listeners(TestListener.class)
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
    public void verifyUserCanGetUserList() throws AutomationException {
        Response response = userService
                .getAllUsers()
                .getResponse();

        AssertApiResponse.success(response);
    }
}