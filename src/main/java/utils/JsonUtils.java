package utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.util.Map;

public class JsonUtils {

    private static final ObjectMapper MAPPER = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    private static InputStream getResourceStream(String fileName) {
        InputStream is = Thread.currentThread()
                .getContextClassLoader()
                .getResourceAsStream(fileName);

        if (is == null) {
            throw new RuntimeException("JSON resource not found in classpath: " + fileName);
        }
        return is;
    }

    public static <T> T readJson(String fileName, Class<T> clazz) {
        try (InputStream is = getResourceStream(fileName)) {
            return MAPPER.readValue(is, clazz);
        } catch (Exception e) {
            throw new RuntimeException("Cannot read or parse JSON file: " + fileName, e);
        }
    }

    public static <T> T readJson(String fileName, Class<T> clazz, String key) {
        try (InputStream is = getResourceStream(fileName)) {
            Map<String, Object> map = MAPPER.readValue(is, new TypeReference<>() {
            });
            Object raw = map.get(key);

            if (raw == null) {
                throw new IllegalArgumentException("Key '" + key + "' not found in JSON: " + fileName);
            }

            return MAPPER.convertValue(raw, clazz);
        } catch (Exception e) {
            throw new RuntimeException("Cannot read or parse JSON file: " + fileName, e);
        }
    }
}