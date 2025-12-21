package tests.amenity;

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
import services.AmenityService;

@Epic("Amenity Management")
@Feature("Get All Amenities")
public class GetAllAmenitiesTest implements ITest {

    private AmenityService adminService;
    private AmenityService userService;
    private AmenityService guestService;
    private String testName;

    @BeforeClass(alwaysRun = true)
    public void setUp() {
        adminService = AmenityService.init(UserRole.ADMIN);
        userService = AmenityService.init(UserRole.USER);
        guestService = AmenityService.init(UserRole.GUEST);
    }

    @DataProvider(name = "getAllAmenitiesServiceProvider")
    public Object[][] serviceData() {
        return new Object[][]{
                {"Verify admin can get amenity list successfully", adminService},
                {"Verify user can get amenity list successfully", userService},
                {"Verify guest can get amenity list successfully", guestService}
        };
    }

    @Test(
            dataProvider = "getAllAmenitiesServiceProvider",
            groups = {"smoke"}
    )
    @Severity(SeverityLevel.CRITICAL)
    public void verifyGetAllAmenitiesSuccessfully(String description,
                                                  AmenityService service) {
        this.testName = description;

        Response response = service
                .getAllAmenities()
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