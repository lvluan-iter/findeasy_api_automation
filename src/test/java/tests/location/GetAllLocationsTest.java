package tests.location;

import core.api.AssertApiResponse;
import core.base.TestListener;
import core.exceptions.AutomationException;
import enums.UserRole;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import services.LocationService;

@Listeners(TestListener.class)
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

    @Test(description = "Verify admin can get location list successfully")
    public void verifyAdminCanGetLocationListSuccessfully() throws AutomationException {
        Response response = locationServiceForAdmin
                .getAllLocations()
                .getResponse();

        AssertApiResponse.success(response);
    }

    @Test(description = "Verify user can get location list successfully")
    public void verifyUserCanGetLocationListSuccessfully() throws AutomationException {
        Response response = locationServiceForUser
                .getAllLocations()
                .getResponse();

        AssertApiResponse.success(response);
    }

    @Test(description = "Verify guest can get location list successfully")
    public void verifyGuestCanGetLocationList() throws AutomationException {
        Response response = locationServiceForGuest
                .getAllLocations()
                .getResponse();

        AssertApiResponse.success(response);
    }
}