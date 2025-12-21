package tests.location;

import api.AssertApiResponse;
import constants.ErrorMessages;
import enums.HttpStatus;
import enums.UserRole;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.restassured.response.Response;
import models.Location;
import org.testng.ITest;
import org.testng.annotations.*;
import services.LocationService;
import utils.DataGenerateUtils;

@Epic("Location Management")
@Feature("Create Location")
public class CreateLocationTest implements ITest {

    private Location locationData;
    private LocationService adminService;
    private LocationService userService;
    private LocationService guestService;
    private Long createdLocationId;
    private String testName;

    @BeforeClass(alwaysRun = true)
    public void setUp() {
        adminService = LocationService.init(UserRole.ADMIN);
        userService = LocationService.init(UserRole.USER);
        guestService = LocationService.init(UserRole.GUEST);
    }

    @BeforeMethod(alwaysRun = true)
    public void setUpData() {
        locationData = Location.builder()
                .name(DataGenerateUtils.city())
                .description(DataGenerateUtils.lorem())
                .url(DataGenerateUtils.imageUrl())
                .build();
    }

    @Test(
            testName = "Verify admin can create a location successfully",
            groups = {"smoke"}
    )
    @Severity(SeverityLevel.CRITICAL)
    public void verifyAdminCanCreateLocationSuccessfully() {
        Response response = adminService
                .createLocation(locationData)
                .getResponse();

        createdLocationId = AssertApiResponse.assertThat(response)
                .status(HttpStatus.OK)
                .succeeded()
                .resultAs(Location.class)
                .getId();
    }

    @DataProvider(name = "unauthorizedCreateLocationProvider")
    public Object[][] unauthorizedData() {
        return new Object[][]{
                {"Verify user cannot create a location", userService},
                {"Verify guest cannot create a location", guestService}
        };
    }

    @Test(
            dataProvider = "unauthorizedCreateLocationProvider",
            groups = {"regression"}
    )
    @Severity(SeverityLevel.CRITICAL)
    public void verifyUnauthorizedUserCannotCreateLocation(String description,
                                                           LocationService service) {
        this.testName = description;

        Response response = service
                .createLocation(locationData)
                .getResponse();

        AssertApiResponse.assertThat(response)
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .failed()
                .errorContains(ErrorMessages.ACCESS_DENIED);
    }

    @AfterMethod(alwaysRun = true)
    public void cleanUp() {
        if (createdLocationId != null) {
            Response response = adminService
                    .deleteLocation(createdLocationId)
                    .getResponse();

            AssertApiResponse.assertThat(response)
                    .status(HttpStatus.NO_CONTENT);

            createdLocationId = null;
        }
    }

    @Override
    public String getTestName() {
        return testName;
    }
}