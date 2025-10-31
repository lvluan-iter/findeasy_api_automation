package core.api;

import core.constants.PathConstants;
import core.exceptions.AutomationException;
import core.utils.ConfigReader;
import enums.RequestMode;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.HttpClientConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class RequestBuilder {
    public static RequestSpecification getRequestSpec(RequestMode mode, String token) throws AutomationException {
        RequestSpecBuilder builder = new RequestSpecBuilder()
                .setBaseUri(ConfigReader.init().getProperty("baseUrl"))
                .setConfig(RestAssuredConfig.config()
                        .httpClient(HttpClientConfig.httpClientConfig()
                                .setParam("http.socket.timeout", PathConstants.API_TIMEOUT)
                                .setParam("http.connection.timeout", PathConstants.API_TIMEOUT)
                        )
                );

        switch (mode) {
            case JSON_AUTH:
                builder.setContentType(ContentType.JSON)
                        .addHeader("Authorization", "Bearer " + token);
                break;
            case JSON_NO_AUTH:
                builder.setContentType(ContentType.JSON);
                break;
            case MULTIPART_AUTH:
                builder.setContentType(ContentType.MULTIPART)
                        .addHeader("Authorization", "Bearer " + token);
                break;

            case FORM_URLENCODED_AUTH:
                builder.setContentType("application/x-www-form-urlencoded")
                        .addHeader("Authorization", "Bearer " + token);
                break;

        }
        return builder.build();
    }
}
