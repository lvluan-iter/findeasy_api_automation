package utils;

import java.util.Properties;

public class EnvReader {
    private static final Properties props = new Properties();

    static {
        try {
            props.load(EnvReader.class.getClassLoader()
                    .getResourceAsStream("config.properties"));
        } catch (Exception e) {
            throw new RuntimeException("Cannot load config file", e);
        }
    }

    public static String getBaseUrl() {
        String env = System.getProperty("env", "Qa");
        return props.getProperty(env + ".url");
    }
}
