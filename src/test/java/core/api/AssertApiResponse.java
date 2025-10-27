package core.api;

import io.restassured.response.Response;
import org.testng.Assert;

public class AssertApiResponse {

    private static void checkStatus(Response response, int expected) {
        Assert.assertEquals(
                expected,
                response.getStatusCode(),
                "Expected status " + expected + " but got " + response.getStatusCode()
        );
    }

    private static void checkSucceeded(Response response, boolean expected) {
        boolean actual = response.jsonPath().getBoolean("succeeded");
        Assert.assertEquals(
                expected,
                actual,
                "Expected succeeded to be " + expected + " but got " + actual
        );
    }

    private static void checkErrors(Response response,
                                    String expectedErrorMessage) {

        if (expectedErrorMessage == null) {
            return;
        }
        String actualMsg = response.jsonPath().getString("errors[0]");
        Assert.assertEquals(expectedErrorMessage, actualMsg,
                "Expected error message " + expectedErrorMessage +
                        " but got " + actualMsg);
    }

    private static void checkResponse(Response response,
                                      int expectedStatus,
                                      boolean expectedSucceeded,
                                      String expectedErrorMessage) {

        checkStatus(response, expectedStatus);
        checkSucceeded(response, expectedSucceeded);
        checkErrors(response, expectedErrorMessage);
    }

    public static void success(Response response) {
        checkResponse(response, 200, true, null);
    }

    public static void createSuccess(Response response) {
        checkResponse(response, 201, true, null);
    }

    public static void successNoContent(Response response) {
        checkResponse(response, 204, true, null);
    }

    public static void badRequest(Response response, String msg) {
        checkResponse(response, 400, false, msg);
    }

    public static void unauthorized(Response response, String msg) {
        checkResponse(response, 401, false, msg);
    }

    public static void forbidden(Response response, String msg) {
        checkResponse(response, 403, false, msg);
    }

    public static void notFound(Response response, String msg) {
        checkResponse(response, 404, false, msg);
    }
}
