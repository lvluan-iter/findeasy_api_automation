package utils;

import exceptions.AutomationException;

import java.io.InputStream;
import java.util.Properties;

public final class ConfigReader {
    private static final Properties props = new Properties();
    private static final String ENV = System.getProperty("env", "qa");

    static {
        try (InputStream is = ConfigReader.class
                .getClassLoader()
                .getResourceAsStream("config.properties")) {

            if (is == null) {
                throw new RuntimeException("config.properties not found");
            }
            props.load(is);
        } catch (Exception e) {
            throw new RuntimeException("Cannot load config file", e);
        }
    }

    private static String key(String name) {
        return ENV + "." + name;
    }

    public static String get(String name) {
        String value = props.getProperty(key(name));
        if (value == null) {
            throw new AutomationException("Missing config: " + key(name));
        }
        return value;
    }

    public static String getOrDefault(String name, String defaultValue) {
        return props.getProperty(key(name), defaultValue);
    }

    public static boolean getBoolean(String name) {
        return Boolean.parseBoolean(get(name));
    }

    public static int getInt(String name) {
        return Integer.parseInt(get(name));
    }
}