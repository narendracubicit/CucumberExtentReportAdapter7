package com.narendra.automation;

import com.narendra.automation.General.Config;
import com.narendra.automation.General.Driver;
import io.cucumber.java.*;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.Reporter;

import java.io.IOException;
import java.util.logging.Logger;

public class CucumberHooks implements Log4j {

    private static Logger logger = Logger.getLogger(CucumberHooks.class.getName());
    public CucumberHooks() {
    }

    @BeforeAll
    public static void setUp() {
        logger.info("Before all the scenarios");
        Config.loadConfigurations("application.properties");
    }

    @AfterAll
    public static void tearDown() {
        Driver.getDriver().quit();
    }
    /*   Moved to ReporterClassCucumber.java
    @AfterStep
    public static void afterEachStep(Scenario scenario) throws IOException {
//        scenario.attach(Utilities.getByteScreenshot(), "image/png", scenario.getName());
        final byte[] screenshot = ((TakesScreenshot) Driver.createOnlineDriver()).getScreenshotAs(OutputType.BYTES);
        scenario.attach(screenshot, "image/png",scenario.getName());

    }

    @After
    public static void afterEachScenario(Scenario scenario) throws IOException {
            if ((scenario.isFailed())) {
                final byte[] screenshot = ((TakesScreenshot) Driver.createOnlineDriver()).getScreenshotAs(OutputType.BYTES);
                //scenario.attach(screenshot, "image/png", scenario.getName());
                scenario.attach(screenshot, "image/png",scenario.getName());
            }
    }

     */
}
