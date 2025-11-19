package tests.location;

import core.api.AssertApiResponse;
import core.constants.ErrorMessages;
import core.constants.PathConstants;
import core.exceptions.AutomationException;
import core.utils.JsonUtils;
import enums.DataType;
import enums.UserRole;
import io.restassured.response.Response;
import listeners.TestListener;
import models.Location;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import services.LocationService;

@Listeners(TestListener.class)
public class CreateLocationTest {

    private Location locationData;
    private LocationService adminService;
    private LocationService userService;
    private LocationService guestService;
    private Long createdLocationId;

    @BeforeClass(alwaysRun = true)
    public void setUp() {
        locationData = JsonUtils.readJson(
                PathConstants.LOCATION_JSON,
                Location.class,
                DataType.CREATED.getName()
        );

        adminService = LocationService.init(UserRole.ADMIN);
        userService = LocationService.init(UserRole.USER);
        guestService = LocationService.init(UserRole.GUEST);
    }

    @Test(
            description = "Verify that admin can create a location successfully",
            groups = {"smoke", "regression"}
    )
    public void verifyAdminCanCreateLocationSuccessfully() throws AutomationException {
        Response response = adminService.createLocation(locationData)
                .getResponse();

        AssertApiResponse.success(response);

        createdLocationId = response.jsonPath()
                .getLong("result.id");
    }

    @Test(
            description = "Verify that normal user cannot create a location",
            groups = {"regression"}
    )
    public void verifyUserCannotCreateLocation() throws AutomationException {
        Response response = userService.createLocation(locationData)
                .getResponse();

        AssertApiResponse.internalServerError(response, ErrorMessages.ACCESS_DENIED);
    }

    @Test(
            description = "Verify that guest cannot create a location",
            groups = {"regression"}
    )
    public void verifyGuestCannotCreateLocation() throws AutomationException {
        Response response = guestService.createLocation(locationData)
                .getResponse();

        AssertApiResponse.internalServerError(response, ErrorMessages.ACCESS_DENIED);
    }

    @AfterMethod(alwaysRun = true)
    public void cleanUp() throws AutomationException {
        if (createdLocationId != null) {
            Response response = adminService.deleteLocation(createdLocationId)
                    .getResponse();

            AssertApiResponse.successNoContent(response);
            createdLocationId = null;
        }
    }
}