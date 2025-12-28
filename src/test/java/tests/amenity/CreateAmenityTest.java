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
import models.Amenity;
import models.User;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import services.AmenityService;
import utils.DataGenerateUtils;
import utils.JsonUtils;

@Epic("Amenity Management")
@Feature("Create Amenity")
public class CreateAmenityTest {
    private Amenity amenityData;
    private AmenityService adminService;

    private Long createdAmenityId;

    @BeforeClass(alwaysRun = true)
    public void setUp() {
        User adminData = JsonUtils.fromFileByKey(PathConstants.ACCOUNT_JSON, UserRole.ADMIN.getRoleName(), User.class);
        adminService = AmenityService.init()
                .auth(adminData.getUsername(), adminData.getPassword());
    }

    @BeforeMethod(alwaysRun = true)
    public void setUpData() {
        amenityData = Amenity.builder()
                .name(DataGenerateUtils.word())
                .icon(DataGenerateUtils.imageUrl())
                .build();
    }

    @Test(
            testName = "Verify admin can create an amenity successfully",
            groups = {"smoke"}
    )
    @Severity(SeverityLevel.CRITICAL)
    public void verifyAdminCanCreateAmenitySuccessfully() {
        Response response = adminService
                .createAmenity(amenityData);

        createdAmenityId = AssertApiResponse.assertThat(response)
                .status(HttpStatus.OK)
                .succeeded()
                .resultAs(Amenity.class)
                .getId();
    }

    @AfterMethod(alwaysRun = true)
    public void cleanUp() {
        if (createdAmenityId != null) {
            Response response = adminService
                    .deleteAmenity(createdAmenityId);

            AssertApiResponse.assertThat(response)
                    .status(HttpStatus.NO_CONTENT);

            createdAmenityId = null;
        }
    }
}