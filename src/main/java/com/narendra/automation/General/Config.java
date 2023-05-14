package com.narendra.automation.General;

import com.narendra.automation.Utilities;
import org.apache.commons.lang3.StringUtils;

import java.util.Properties;
import java.util.logging.Logger;

public class Config {

    // Jenkins parameters and mvn commandline args use System.getProperty()
    // Jenkins config file properties - System.getenv()
    public static String browser, runTestOnCloud, JIRA_USER, JIRA_PASSWORD, DB_USER, DB_PASSWORD, GRID_URL;
    private static Logger logger = Logger.getLogger(Config.class.getName());


    public static void init_properties() {
        Properties properties = Utilities.init_properties();
        for (String property : properties.stringPropertyNames()) {
            logger.info("Setting property from application.properties -key:" + property + " and value:" + properties.getProperty(property));
        }
        browser = properties.getProperty("browser");
        runTestOnCloud = properties.getProperty("runtestoncloud");
        JIRA_USER = properties.getProperty("jira_user");
        JIRA_PASSWORD = properties.getProperty("jira_pwd");
        DB_USER = properties.getProperty("DB_USER");
        DB_PASSWORD = properties.getProperty("DB_PASSWORD");
        GRID_URL = properties.getProperty("GRID_URL");
    }

    public static void loadConfigurations(String fileName) {
        try {
            switch (fileName) {
                case "application.properties":
                    logger.info("Reading app config from application.properties file");
                    init_properties();
                    loadConfigurations("config.xlsx");
                    break;
                case "config.xlsx":
                    logger.info("Reading app config from config.xlsx file..");
                    // TO-DO
                    loadConfigurations("commandlineProperties");
                    break;
                case "commandlineProperties":
                    logger.info("Setting environment variable from jenkins parameters and config file");
                    setJenkinsProps();
                    break;
            }

        } catch (Exception e) {
            logger.info("Something went wrong while trying to load properties.");
        }
    }

    public static void setJenkinsProps() {
        setCommandLineProperties();
        setConfigFileProperties();
    }

    public static void setCommandLineProperties() {
        if (!getPropertyValue("DB_USER").equals("")) {
            Config.DB_USER = System.getProperty("DB_USER");
        }
        if (!getPropertyValue("DB_PASSWORD").equals("")) {
            Config.DB_PASSWORD = System.getProperty("DB_PASSWORD");
        }
        if (!getPropertyValue("GRID_URL").equals("")) {
            Config.GRID_URL = System.getProperty("GRID_URL");
        }
        if (!getPropertyValue("DB_USER").equals("")) {
            Config.DB_USER = System.getProperty("DB_USER");
        }
        if (!getPropertyValue("DB_PASSWORD").equals("")) {
            Config.DB_PASSWORD = System.getProperty("DB_PASSWORD");
        }
        if (!getPropertyValue("GRID_URL").equals("")) {
            Config.GRID_URL = System.getProperty("GRID_URL");
        }
    }

    public static void setConfigFileProperties() {
        if (!getJenkinsConfigPropertyValue("DB_USER").equals("")) {
            Config.DB_USER = System.getProperty("DB_USER");
        }
        if (!getJenkinsConfigPropertyValue("DB_PASSWORD").equals("")) {
            Config.DB_PASSWORD = System.getProperty("DB_PASSWORD");
        }
        if (!getJenkinsConfigPropertyValue("GRID_URL").equals("")) {
            Config.GRID_URL = System.getProperty("GRID_URL");
        }
        if (!getJenkinsConfigPropertyValue("DB_USER").equals("")) {
            Config.DB_USER = System.getProperty("DB_USER");
        }
        if (!getJenkinsConfigPropertyValue("DB_PASSWORD").equals("")) {
            Config.DB_PASSWORD = System.getProperty("DB_PASSWORD");
        }
        if (!getJenkinsConfigPropertyValue("GRID_URL").equals("")) {
            Config.GRID_URL = System.getProperty("GRID_URL");
        }
    }

    private static String getPropertyValue(String property) {
        String value = System.getProperty(property);
        logger.info("Commandline property - " + property + " value:" + value);
        return StringUtils.isNotEmpty(value) ? value : "";
    }

    private static String getJenkinsConfigPropertyValue(String property) {
        String value = System.getenv(property);
        logger.info("Jenkins Config file property - " + property + " value:" + value);
        return StringUtils.isNotEmpty(value) ? value : "";
    }
}
