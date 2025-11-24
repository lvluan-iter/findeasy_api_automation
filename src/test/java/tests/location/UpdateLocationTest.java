package tests.location;

import api.AssertApiResponse;
import constants.ErrorMessages;
import constants.PathConstants;
import enums.DataType;
import enums.UserRole;
import exceptions.AutomationException;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.restassured.response.Response;
import models.Location;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import services.LocationService;
import utils.JsonUtils;

@Epic("Location Management")
@Feature("Update Location")
public class UpdateLocationTest {

    private Location locationData;
    private LocationService adminService;
    private LocationService userService;
    private LocationService guestService;
    private Long updatedLocationId;

    @BeforeClass(alwaysRun = true)
    public void setUp() {
        locationData = JsonUtils.readJson(
                PathConstants.LOCATION_JSON,
                Location.class,
                DataType.TEST.getName()
        );

        adminService = LocationService.init(UserRole.ADMIN);
        userService = LocationService.init(UserRole.USER);
        guestService = LocationService.init(UserRole.GUEST);
    }

    @Test(
            description = "Verify admin can update a location successfully",
            groups = {"smoke", "regression"}
    )
    @Severity(SeverityLevel.BLOCKER)
    public void verifyAdminCanUpdateLocationSuccessfully() throws AutomationException {
        Location payload = Location.builder()
                .name(locationData.getName() + "Test")
                .description(locationData.getDescription())
                .url(locationData.getUrl())
                .build();

        Response response = adminService.updateLocation(locationData.getId(), payload)
                .getResponse();

        AssertApiResponse.success(response);
        Assert.assertEquals(
                response.jsonPath()
                        .getString("result.name"),
                payload.getName()
        );

        updatedLocationId = response.jsonPath()
                .getLong("result.id");
    }

    @Test(
            description = "Verify normal user cannot update location",
            groups = {"regression"}
    )
    @Severity(SeverityLevel.CRITICAL)
    public void verifyUserCannotUpdateLocation() throws AutomationException {
        Location payload = Location.builder()
                .name(locationData.getName() + "Test")
                .description(locationData.getDescription())
                .url(locationData.getUrl())
                .build();

        Response response = userService.updateLocation(locationData.getId(), payload)
                .getResponse();

        AssertApiResponse.internalServerError(response, ErrorMessages.ACCESS_DENIED);
    }

    @Test(
            description = "Verify guest cannot update location",
            groups = {"regression"}
    )
    @Severity(SeverityLevel.CRITICAL)
    public void verifyGuestCannotUpdateLocation() throws AutomationException {
        Location payload = Location.builder()
                .name(locationData.getName() + "Test")
                .description(locationData.getDescription())
                .url(locationData.getUrl())
                .build();

        Response response = guestService.updateLocation(locationData.getId(), payload)
                .getResponse();

        AssertApiResponse.internalServerError(response, ErrorMessages.ACCESS_DENIED);
    }

    @AfterMethod(alwaysRun = true)
    public void revertChanges() throws AutomationException {
        if (updatedLocationId != null) {
            Location revertPayload = Location.builder()
                    .name(locationData.getName())
                    .description(locationData.getDescription())
                    .url(locationData.getUrl())
                    .build();

            Response response = adminService.updateLocation(locationData.getId(), revertPayload)
                    .getResponse();

            AssertApiResponse.success(response);

            updatedLocationId = null;
        }
    }
}