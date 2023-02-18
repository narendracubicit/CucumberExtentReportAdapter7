package com.narendra.automation;

import com.narendra.automation.General.Driver;
import io.cucumber.java.*;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.Reporter;

import java.io.IOException;

public class CucumberHooks implements Log4j {
    public CucumberHooks() {
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
