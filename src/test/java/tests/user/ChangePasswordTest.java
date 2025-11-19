package tests.user;

import core.api.AssertApiResponse;
import core.constants.ErrorMessages;
import core.constants.PathConstants;
import core.exceptions.AutomationException;
import core.utils.JsonUtils;
import enums.UserRole;
import io.restassured.response.Response;
import listeners.TestListener;
import models.ChangePasswordRequest;
import models.User;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import services.UserService;

@Listeners(TestListener.class)
public class ChangePasswordTest {

    private UserService userService;
    private User userData;
    private Long userId;

    @BeforeClass(alwaysRun = true)
    public void setUp() throws AutomationException {
        userData = JsonUtils.readJson(
                PathConstants.ACCOUNT_JSON,
                User.class,
                UserRole.USER.getRoleName()
        );
        userService = UserService.init(UserRole.USER);
    }

    private Long getUserId() throws AutomationException {
        if (userId == null) {
            Response response = userService
                    .getUserByUsername(userData.getUsername())
                    .getResponse();

            userId = response.jsonPath()
                    .getLong("result.id");
        }
        return userId;
    }

    @Test(
            description = "Verify user can change password successfully",
            groups = {"smoke", "regression"}
    )
    public void verifyUserCanChangePasswordSuccessfully() throws AutomationException {
        Long id = getUserId();

        ChangePasswordRequest payload = ChangePasswordRequest.builder()
                .currentPassword(userData.getCurrentPassword())
                .newPassword(userData.getNewPassword())
                .build();

        Response response = userService.changePassword(id, payload)
                .getResponse();
        AssertApiResponse.success(response);

        rollbackPassword(id);
    }

    @Test(
            description = "Verify user cannot change password if new password is same as current password",
            groups = {"regression"}
    )
    public void verifyUserCannotChangePasswordWhenSamePassword() throws AutomationException {
        Long id = getUserId();

        ChangePasswordRequest payload = ChangePasswordRequest.builder()
                .currentPassword(userData.getCurrentPassword())
                .newPassword(userData.getCurrentPassword())
                .build();

        Response response = userService.changePassword(id, payload)
                .getResponse();

        AssertApiResponse.internalServerError(response, ErrorMessages.NEW_PASSWORD_MUST_BE_DIFFERENT_CURRENT);
    }

    @Test(
            description = "Verify user cannot change password if new password does not meet requirement",
            groups = {"regression"}
    )
    public void verifyUserCannotChangePasswordNotMeetRequirement() throws AutomationException {
        Long id = getUserId();

        ChangePasswordRequest payload = ChangePasswordRequest.builder()
                .currentPassword(userData.getCurrentPassword())
                .newPassword("abc")
                .build();

        Response response = userService.changePassword(id, payload)
                .getResponse();

        AssertApiResponse.internalServerError(response, ErrorMessages.INVALID_PASSWORD);
    }

    private void rollbackPassword(Long id) throws AutomationException {
        ChangePasswordRequest rollback = ChangePasswordRequest.builder()
                .currentPassword(userData.getNewPassword())
                .newPassword(userData.getCurrentPassword())
                .build();
        userService.changePassword(id, rollback);
    }
}