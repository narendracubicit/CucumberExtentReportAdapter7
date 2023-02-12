package com.narendra.automation;

import io.cucumber.java.*;
import java.io.IOException;

public class CucumberHooks implements Log4j {
    public static String caseIdGlobal;
    public static String scenarioName;


    public CucumberHooks() {
    }

    @AfterAll
    public static void afterSuite() {

    }

    @AfterStep
    public static void afterEachStep(Scenario scenario) throws IOException {
        scenario.attach(Utilities.getByteScreenshot(), "image/png", scenario.getName());
    }
}
