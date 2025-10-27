package core.utils;

import core.constants.FrameworkConstants;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {
    private static final Properties properties;

    static {
        try {
            FileInputStream fis = new FileInputStream(FrameworkConstants.CONFIG_PROPERTIES_PATH);
            properties = new Properties();
            properties.load(fis);

        } catch (IOException e) {
            throw new RuntimeException("Could not load config.properties file.", e);
        }
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }
}
