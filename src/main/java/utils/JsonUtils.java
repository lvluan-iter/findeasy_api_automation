package utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import exceptions.AutomationException;
import io.restassured.response.Response;

import java.io.InputStream;
import java.util.Map;

public final class JsonUtils {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private JsonUtils() {
    }

    public static <T> T fromFile(String path, Class<T> clazz) {
        try (InputStream is = resource(path)) {
            return MAPPER.readValue(is, clazz);
        } catch (Exception e) {
            throw new AutomationException("Failed to read JSON file: " + path, e);
        }
    }

    public static <T> T fromFileByKey(String path, String key, Class<T> clazz) {
        try (InputStream is = resource(path)) {
            Map<String, Object> map = MAPPER.readValue(is, new TypeReference<>() {
            });
            Object raw = map.get(key);

            if (raw == null) {
                throw new AutomationException(
                        "Key '" + key + "' not found in JSON file: " + path
                );
            }
            return convert(raw, clazz);
        } catch (Exception e) {
            throw new AutomationException("Failed to read JSON file: " + path, e);
        }
    }

    public static <T> T fromResponse(Response response, Class<T> clazz) {
        return fromString(body(response), clazz);
    }

    public static <T> T fromResponse(Response response, TypeReference<T> typeRef) {
        return fromString(body(response), typeRef);
    }

    public static <T> T fromString(String json, Class<T> clazz) {
        try {
            return MAPPER.readValue(json, clazz);
        } catch (Exception e) {
            throw new AutomationException("Failed to parse JSON string", e);
        }
    }

    public static <T> T fromString(String json, TypeReference<T> typeRef) {
        try {
            return MAPPER.readValue(json, typeRef);
        } catch (Exception e) {
            throw new AutomationException("Failed to parse JSON string", e);
        }
    }

    public static <T> T convert(Object raw, Class<T> clazz) {
        try {
            return MAPPER.convertValue(raw, clazz);
        } catch (Exception e) {
            throw new AutomationException(
                    "Failed to convert value to " + clazz.getSimpleName(), e
            );
        }
    }

    public static <T> T convert(Object raw, TypeReference<T> typeRef) {
        try {
            return MAPPER.convertValue(raw, typeRef);
        } catch (Exception e) {
            throw new AutomationException("Failed to convert value", e);
        }
    }

    private static InputStream resource(String path) {
        InputStream is = Thread.currentThread()
                .getContextClassLoader()
                .getResourceAsStream(path);

        if (is == null) {
            throw new AutomationException("JSON resource not found: " + path);
        }
        return is;
    }

    private static String body(Response response) {
        if (response == null || response.asString()
                .isBlank()) {
            throw new AutomationException("Response body is empty");
        }
        return response.asString();
    }
}