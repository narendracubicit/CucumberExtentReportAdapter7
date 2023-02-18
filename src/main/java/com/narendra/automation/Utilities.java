package com.narendra.automation;

import com.narendra.automation.General.Driver;
import io.cucumber.java.Scenario;
import org.apache.commons.io.FileUtils;
import org.joda.time.LocalDateTime;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;

public class Utilities {

    public static void attachScreenShot(Scenario scenario) throws IOException {
        File src = ((TakesScreenshot) Driver.createOnlineDriver()).getScreenshotAs(OutputType.FILE);
        byte[] fileContent = FileUtils.readFileToByteArray(src);
        scenario.attach(fileContent, "image/png", scenario.getName());
    }
}
