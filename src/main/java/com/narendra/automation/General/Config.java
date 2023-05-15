package com.narendra.automation.General;

import com.narendra.automation.Utilities;
import org.apache.commons.lang3.StringUtils;

import java.util.Properties;
import java.util.logging.Logger;

public class Config {

    // mvn commandline args use System.getProperty()
    // Jenkins parameters and config file properties - System.getenv()
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
        String browser = getCommandLinePropertyValue("browser");
        String runTestOnCloud = getCommandLinePropertyValue("runtestoncloud");
        String dbUserName = getCommandLinePropertyValue("DB_USER");
        String dbPassword = getCommandLinePropertyValue("DB_PASSWORD");
        String gridURL = getCommandLinePropertyValue("GRID_URL");
        String jiraUser = getCommandLinePropertyValue("jira_user");
        String jiraPassword = getCommandLinePropertyValue("jira_pwd");
        // OVERRIDE if the value from jenkins is not empty or null
        if (StringUtils.isNotEmpty(browser))
            Config.browser = browser;
        if (StringUtils.isNotEmpty(runTestOnCloud))
            Config.runTestOnCloud = runTestOnCloud;
        if (StringUtils.isNotEmpty(dbUserName))
            Config.DB_USER = dbUserName;
        if (StringUtils.isNotEmpty(dbPassword))
            Config.DB_PASSWORD = dbPassword;
        if (StringUtils.isNotEmpty(gridURL))
            Config.GRID_URL = gridURL;
        if (StringUtils.isNotEmpty(jiraUser))
            Config.JIRA_USER = jiraUser;
        if (StringUtils.isNotEmpty(jiraPassword))
            Config.JIRA_PASSWORD = jiraPassword;
    }

    public static void setConfigFileProperties() {
        // overriding values set by config.xlsx or application.properties and commandline if the value is present in jenkins params
        String browser = getJenkinsParameterPropertyValue("browser");
        String runTestOnCloud = getJenkinsParameterPropertyValue("runtestoncloud");
        String dbUserName = getJenkinsParameterPropertyValue("DB_USER");
        String dbPassword = getJenkinsParameterPropertyValue("DB_PASSWORD");
        String gridURL = getJenkinsParameterPropertyValue("GRID_URL");
        String jiraUser = getJenkinsParameterPropertyValue("jira_user");
        String jiraPassword = getJenkinsParameterPropertyValue("jira_pwd");
        // OVERRIDE if the value from jenkins is not empty or null
        if (StringUtils.isNotEmpty(browser))
            Config.browser = browser;
        if (StringUtils.isNotEmpty(runTestOnCloud))
            Config.runTestOnCloud = runTestOnCloud;
        if (StringUtils.isNotEmpty(dbUserName))
            Config.DB_USER = dbUserName;
        if (StringUtils.isNotEmpty(dbPassword))
            Config.DB_PASSWORD = dbPassword;
        if (StringUtils.isNotEmpty(gridURL))
            Config.GRID_URL = gridURL;
        if (StringUtils.isNotEmpty(jiraUser))
            Config.JIRA_USER = jiraUser;
        if (StringUtils.isNotEmpty(jiraPassword))
            Config.JIRA_PASSWORD = jiraPassword;
    }

    private static String getCommandLinePropertyValue(String property) {
        String value = System.getProperty(property);
        logger.info("Commandline property - " + property + " value:" + value);
        return StringUtils.isNotEmpty(value) ? value : "";
    }

    private static String getJenkinsParameterPropertyValue(String property) {
        String value = System.getenv(property);
        logger.info("Jenkins Params/Config file property - " + property + " value:" + value);
        return StringUtils.isNotEmpty(value) ? value : "";
    }
}
