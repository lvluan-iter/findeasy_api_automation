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
import org.testng.Assert;
import org.testng.ITest;
import org.testng.annotations.*;
import services.LocationService;
import utils.DataGenerateUtils;

@Epic("Location Management")
@Feature("Update Location")
public class UpdateLocationTest implements ITest {

    private LocationService adminService;
    private LocationService userService;
    private LocationService guestService;

    private Location createdLocation;
    private Long createdLocationId;
    private String testName;

    @BeforeClass(alwaysRun = true)
    public void setUp() {
        adminService = LocationService.init(UserRole.ADMIN);
        userService = LocationService.init(UserRole.USER);
        guestService = LocationService.init(UserRole.GUEST);
    }

    @BeforeMethod(alwaysRun = true)
    public void createLocation() {
        Location payload = Location.builder()
                .name(DataGenerateUtils.city())
                .description(DataGenerateUtils.lorem())
                .url(DataGenerateUtils.imageUrl())
                .build();

        Response response = adminService
                .createLocation(payload)
                .getResponse();

        createdLocation = AssertApiResponse.assertThat(response)
                .status(HttpStatus.OK)
                .succeeded()
                .resultAs(Location.class);
        createdLocationId = createdLocation.getId();
    }

    @Test(
            testName = "Verify admin can update a location successfully",
            groups = {"smoke"}
    )
    @Severity(SeverityLevel.BLOCKER)
    public void verifyAdminCanUpdateLocationSuccessfully() {
        Location updatePayload = Location.builder()
                .name(createdLocation.getName() + " Updated")
                .description(createdLocation.getDescription() + " Updated")
                .url(createdLocation.getUrl())
                .build();

        Response response = adminService
                .updateLocation(createdLocationId, updatePayload)
                .getResponse();

        Location updatedLocation = AssertApiResponse.assertThat(response)
                .status(HttpStatus.OK)
                .succeeded()
                .resultAs(Location.class);

        Assert.assertEquals(updatedLocation.getName(), updatePayload.getName());
        Assert.assertEquals(updatedLocation.getDescription(), updatePayload.getDescription());
    }

    @DataProvider(name = "unauthorizedUpdateProvider")
    public Object[][] unauthorizedUpdateProvider() {
        return new Object[][]{
                {"Verify user cannot update location", userService},
                {"Verify guest cannot update location", guestService}
        };
    }

    @Test(
            dataProvider = "unauthorizedUpdateProvider",
            groups = {"regression"}
    )
    @Severity(SeverityLevel.CRITICAL)
    public void verifyUnauthorizedUserCannotUpdateLocation(String description,
                                                           LocationService service) {
        this.testName = description;

        Location payload = Location.builder()
                .name("Unauthorized Update")
                .description("Unauthorized Update")
                .url(createdLocation.getUrl())
                .build();

        Response response = service
                .updateLocation(createdLocationId, payload)
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