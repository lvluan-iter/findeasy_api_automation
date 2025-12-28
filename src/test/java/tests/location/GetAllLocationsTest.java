package tests.location;

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
import services.LocationService;
import utils.JsonUtils;

@Epic("Location Management")
@Feature("Get All Locations")
public class GetAllLocationsTest implements ITest {

    private LocationService adminService;
    private LocationService userService;
    private LocationService guestService;
    private String testName;

    @BeforeClass(alwaysRun = true)
    public void setUp() {
        User adminData = JsonUtils.fromFileByKey(PathConstants.ACCOUNT_JSON, UserRole.ADMIN.getRoleName(), User.class);
        User useData = JsonUtils.fromFileByKey(PathConstants.ACCOUNT_JSON, UserRole.USER.getRoleName(), User.class);

        adminService = LocationService.init()
                .auth(adminData.getUsername(), adminData.getPassword());
        userService = LocationService.init()
                .auth(useData.getUsername(), useData.getPassword());
        guestService = LocationService.init();
    }

    @DataProvider(name = "getAllLocationsServiceProvider")
    public Object[][] serviceData() {
        return new Object[][]{
                {"Verify admin can get location list successfully", adminService},
                {"Verify user can get location list successfully", userService},
                {"Verify guest can get location list successfully", guestService}
        };
    }

    @Test(
            dataProvider = "getAllLocationsServiceProvider",
            groups = {"smoke"}
    )
    @Severity(SeverityLevel.CRITICAL)
    public void verifyGetAllLocationsSuccessfully(String description,
                                                  LocationService service) {
        this.testName = description;

        Response response = service
                .getAllLocations();

        AssertApiResponse.assertThat(response)
                .status(HttpStatus.OK)
                .succeeded();
    }

    @Override
    public String getTestName() {
        return testName;
    }
}