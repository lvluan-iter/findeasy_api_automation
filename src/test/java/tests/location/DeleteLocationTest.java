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
public class DeleteLocationTest {

    private Location locationData;
    private LocationService adminService;
    private LocationService userService;
    private LocationService guestService;
    private Long locationId;

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

    @Test(description = "Verify admin can delete a location successfully")
    public void verifyAdminCanDeleteLocationSuccessfully() throws AutomationException {
        Response createRes = adminService.createLocation(locationData)
                .getResponse();
        AssertApiResponse.success(createRes);

        locationId = createRes.jsonPath()
                .getLong("result.id");

        Response deleteRes = adminService.deleteLocation(locationId)
                .getResponse();

        AssertApiResponse.successNoContent(deleteRes);
        locationId = null;
    }

    @Test(description = "Verify normal user cannot delete location")
    public void verifyUserCannotDeleteLocation() throws AutomationException {
        Response createRes = adminService.createLocation(locationData)
                .getResponse();
        locationId = createRes.jsonPath()
                .getLong("result.id");

        Response res = userService.deleteLocation(locationId)
                .getResponse();

        AssertApiResponse.internalServerError(res, ErrorMessages.ACCESS_DENIED);
    }

    @Test(description = "Verify guest cannot delete location")
    public void verifyGuestCannotDeleteLocation() throws AutomationException {

        Response createRes = adminService.createLocation(locationData)
                .getResponse();
        locationId = createRes.jsonPath()
                .getLong("result.id");

        Response res = guestService.deleteLocation(locationId)
                .getResponse();

        AssertApiResponse.internalServerError(res, ErrorMessages.ACCESS_DENIED);
    }

    @Test(description = "Verify admin deleting same location twice returns not found")
    public void verifyDeleteLocationTwice() throws AutomationException {
        Response createRes = adminService.createLocation(locationData)
                .getResponse();
        locationId = createRes.jsonPath()
                .getLong("result.id");

        Response firstDelete = adminService.deleteLocation(locationId)
                .getResponse();
        AssertApiResponse.successNoContent(firstDelete);

        Response secondDelete = adminService.deleteLocation(locationId)
                .getResponse();
        AssertApiResponse.notFound(secondDelete, ErrorMessages.LOCATION_NOT_FOUND + locationId);
        locationId = null;
    }

    @AfterMethod(alwaysRun = true)
    public void cleanUp() throws AutomationException {
        if (locationId != null) {
            Response response = adminService.deleteLocation(locationId)
                    .getResponse();
            AssertApiResponse.successNoContent(response);
            locationId = null;
        }
    }
}