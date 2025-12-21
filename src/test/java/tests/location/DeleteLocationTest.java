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
@Feature("Delete Location")
public class DeleteLocationTest implements ITest {

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
    public void createLocation() {
        Location payload = Location.builder()
                .name(DataGenerateUtils.city())
                .description(DataGenerateUtils.lorem())
                .url(DataGenerateUtils.imageUrl())
                .build();

        Response response = adminService
                .createLocation(payload)
                .getResponse();

        createdLocationId = AssertApiResponse.assertThat(response)
                .status(HttpStatus.OK)
                .succeeded()
                .resultAs(Location.class)
                .getId();
    }

    @Test(
            testName = "Verify admin can delete a location successfully",
            groups = {"smoke"}
    )
    @Severity(SeverityLevel.BLOCKER)
    public void verifyAdminCanDeleteLocationSuccessfully() {
        Response response = adminService
                .deleteLocation(createdLocationId)
                .getResponse();

        AssertApiResponse.assertThat(response)
                .status(HttpStatus.NO_CONTENT);

        createdLocationId = null;
    }

    @DataProvider(name = "unauthorizedDeleteProvider")
    public Object[][] unauthorizedDeleteProvider() {
        return new Object[][]{
                {"Verify user cannot delete location", userService},
                {"Verify guest cannot delete location", guestService}
        };
    }

    @Test(
            dataProvider = "unauthorizedDeleteProvider",
            groups = {"regression"}
    )
    @Severity(SeverityLevel.CRITICAL)
    public void verifyUnauthorizedUserCannotDeleteLocation(String description,
                                                           LocationService service) {
        this.testName = description;

        Response response = service
                .deleteLocation(createdLocationId)
                .getResponse();

        AssertApiResponse.assertThat(response)
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .failed()
                .errorContains(ErrorMessages.ACCESS_DENIED);
    }

    @Test(
            testName = "Verify deleting same location twice returns not found",
            groups = {"regression"}
    )
    @Severity(SeverityLevel.CRITICAL)
    public void verifyDeleteLocationTwice() {
        Response firstDelete = adminService
                .deleteLocation(createdLocationId)
                .getResponse();

        AssertApiResponse.assertThat(firstDelete)
                .status(HttpStatus.NO_CONTENT);

        Response secondDelete = adminService
                .deleteLocation(createdLocationId)
                .getResponse();

        AssertApiResponse.assertThat(secondDelete)
                .status(HttpStatus.NOT_FOUND)
                .failed()
                .errorContains(ErrorMessages.LOCATION_NOT_FOUND);

        createdLocationId = null;
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