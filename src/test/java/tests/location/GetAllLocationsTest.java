package tests.location;

import api.AssertApiResponse;
import enums.HttpStatus;
import enums.UserRole;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.restassured.response.Response;
import org.testng.ITest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import services.LocationService;

@Epic("Location Management")
@Feature("Get All Locations")
public class GetAllLocationsTest implements ITest {

    private LocationService adminService;
    private LocationService userService;
    private LocationService guestService;
    private String testName;

    @BeforeClass(alwaysRun = true)
    public void setUp() {
        adminService = LocationService.init(UserRole.ADMIN);
        userService = LocationService.init(UserRole.USER);
        guestService = LocationService.init(UserRole.GUEST);
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
                .getAllLocations()
                .getResponse();

        AssertApiResponse.assertThat(response)
                .status(HttpStatus.OK)
                .succeeded();
    }

    @Override
    public String getTestName() {
        return testName;
    }
}