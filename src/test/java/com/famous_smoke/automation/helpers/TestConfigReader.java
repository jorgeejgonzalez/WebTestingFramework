package com.famous_smoke.automation.helpers;

import com.famous_smoke.automation.api.SupportedWebDrivers;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by jorge on 16-01-2016.
 */
public class TestConfigReader {

    private static final String     TESTCONFIG_FILE_PATH     = "src/test/resources/properties/TestConfig.properties";
    private static final Properties TESTCONFIG_PROPERTIES    = loadProperties();
    private static final String     MAXIMUM_CRAWLS_PROPERTY  = "MAXIMUM_CRAWLS";
    private static final String     SELENIUM_DRIVER_PROPERTY = "SELENIUM_DRIVER";

    private static Properties loadProperties() {
        Properties properties = new Properties();
        try {
            InputStream inputStream = new FileInputStream(TESTCONFIG_FILE_PATH);
            properties.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return properties;
    }

    public static SupportedWebDrivers getSeleniumDriver() {
        return SupportedWebDrivers.valueOf(TESTCONFIG_PROPERTIES.getProperty(SELENIUM_DRIVER_PROPERTY));
    }

    public static Integer getMaximumCrawls() {
        return Integer.valueOf(TESTCONFIG_PROPERTIES.getProperty(MAXIMUM_CRAWLS_PROPERTY));
    }
}