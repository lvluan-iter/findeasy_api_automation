package core.utils;

import core.constants.PathConstants;
import core.exceptions.AutomationException;

import java.io.FileInputStream;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Properties;

public final class ConfigReader {

    private static final ConfigReader INSTANCE = new ConfigReader();
    private static Properties properties;

    public static ConfigReader init() {
        ConfigReader testProp = INSTANCE;
        testProp.getInstance();
        return testProp;
    }

    private void getInstance() {
        if (properties == null) {
            properties = new Properties();
            try {
                FileInputStream inputStream = new FileInputStream(PathConstants.CONFIG_PROPERTIES_PATH);
                properties.load(inputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getProperty(String propertyName) throws AutomationException {

        Object value = properties.get(propertyName);
        if (value != null) {
            return properties.get(propertyName)
                    .toString();
        } else {
            String errorLog = MessageFormat.format(
                    "Error occurred while getting {0} Property from config.properties. This could be due to no such property available in this file.",
                    propertyName);
            throw new AutomationException(errorLog);
        }
    }

    public boolean getBooleanProperty(String propertyName) {
        return Boolean.parseBoolean(properties.getProperty(propertyName));
    }

    public Integer getIntegerProperty(String propertyName) {
        return Integer.parseInt(properties.getProperty(propertyName));
    }

}