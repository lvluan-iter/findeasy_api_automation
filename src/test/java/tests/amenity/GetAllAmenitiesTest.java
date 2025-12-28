package tests.amenity;

import api.AssertApiResponse;
import constants.PathConstants;
import enums.HttpStatus;
import enums.UserRole;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.restassured.response.Response;
import models.User;
import org.testng.ITest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import services.AmenityService;
import utils.JsonUtils;

@Epic("Amenity Management")
@Feature("Get All Amenities")
public class GetAllAmenitiesTest implements ITest {

    private AmenityService adminService;
    private AmenityService userService;
    private AmenityService guestService;
    private String testName;

    @BeforeClass(alwaysRun = true)
    public void setUp() {
        User adminData = JsonUtils.fromFileByKey(PathConstants.ACCOUNT_JSON, UserRole.ADMIN.getRoleName(), User.class);
        User useData = JsonUtils.fromFileByKey(PathConstants.ACCOUNT_JSON, UserRole.USER.getRoleName(), User.class);

        adminService = AmenityService.init()
                .auth(adminData.getUsername(), adminData.getPassword());
        userService = AmenityService.init()
                .auth(useData.getUsername(), useData.getPassword());
        guestService = AmenityService.init();
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
                .getAllAmenities();

        AssertApiResponse.assertThat(response)
                .status(HttpStatus.OK)
                .succeeded();
    }

    @Override
    public String getTestName() {
        return testName;
    }
}