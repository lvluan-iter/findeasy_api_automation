package tests.location;

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
import models.Location;
import models.User;
import org.testng.Assert;
import org.testng.ITest;
import org.testng.annotations.*;
import services.LocationService;
import utils.DataGenerateUtils;
import utils.JsonUtils;

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
        User adminData = JsonUtils.fromFileByKey(PathConstants.ACCOUNT_JSON, UserRole.ADMIN.getRoleName(), User.class);
        User useData = JsonUtils.fromFileByKey(PathConstants.ACCOUNT_JSON, UserRole.USER.getRoleName(), User.class);

        adminService = LocationService.init()
                .auth(adminData.getUsername(), adminData.getPassword());
        userService = LocationService.init()
                .auth(useData.getUsername(), useData.getPassword());
        guestService = LocationService.init();
    }

    @BeforeMethod(alwaysRun = true)
    public void createLocation() {
        Location payload = Location.builder()
                .name(DataGenerateUtils.city())
                .description(DataGenerateUtils.lorem())
                .url(DataGenerateUtils.imageUrl())
                .build();

        Response response = adminService
                .createLocation(payload);

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
                .updateLocation(createdLocationId, updatePayload);

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
                .updateLocation(createdLocationId, payload);

        AssertApiResponse.assertThat(response)
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .failed()
                .errorContains(ErrorMessages.ACCESS_DENIED);
    }

    @AfterMethod(alwaysRun = true)
    public void cleanUp() {
        if (createdLocationId != null) {
            Response response = adminService
                    .deleteLocation(createdLocationId);

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