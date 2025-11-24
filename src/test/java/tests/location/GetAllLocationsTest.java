package tests.location;

import api.AssertApiResponse;
import enums.UserRole;
import exceptions.AutomationException;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import services.LocationService;

@Epic("Location Management")
@Feature("Get All Locations")
public class GetAllLocationsTest {

    private LocationService locationServiceForAdmin;
    private LocationService locationServiceForUser;
    private LocationService locationServiceForGuest;

    @BeforeClass(alwaysRun = true)
    public void setUp() {
        locationServiceForAdmin = LocationService.init(UserRole.ADMIN);
        locationServiceForUser = LocationService.init(UserRole.USER);
        locationServiceForGuest = LocationService.init(UserRole.GUEST);
    }

    @Test(
            description = "Verify admin can get location list successfully",
            groups = {"smoke", "regression"}
    )
    @Severity(SeverityLevel.BLOCKER)
    public void verifyAdminCanGetLocationListSuccessfully() throws AutomationException {
        Response response = locationServiceForAdmin
                .getAllLocations()
                .getResponse();

        AssertApiResponse.success(response);
    }

    @Test(
            description = "Verify user can get location list successfully",
            groups = {"smoke", "regression"}
    )
    @Severity(SeverityLevel.BLOCKER)
    public void verifyUserCanGetLocationListSuccessfully() throws AutomationException {
        Response response = locationServiceForUser
                .getAllLocations()
                .getResponse();

        AssertApiResponse.success(response);
    }

    @Test(
            description = "Verify guest can get location list successfully",
            groups = {"smoke", "regression"}
    )
    @Severity(SeverityLevel.BLOCKER)
    public void verifyGuestCanGetLocationList() throws AutomationException {
        Response response = locationServiceForGuest
                .getAllLocations()
                .getResponse();

        AssertApiResponse.success(response);
    }
}