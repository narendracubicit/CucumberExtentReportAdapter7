package com.narendra.automation;

import com.narendra.automation.General.Driver;
import io.cucumber.java.Scenario;
import org.apache.commons.io.FileUtils;
import org.joda.time.LocalDateTime;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Properties;

public class Utilities {

    public static void attachScreenShot(Scenario scenario) throws IOException {
        File src = ((TakesScreenshot) Driver.getDriver()).getScreenshotAs(OutputType.FILE);
        byte[] fileContent = FileUtils.readFileToByteArray(src);
        scenario.attach(fileContent, "image/png", scenario.getName());
    }

    /**
     * this method is used to initialize the properties
     *
     * @return prop
     * @author Narendra Palla
     */
    public static Properties init_properties() {
        Properties prop = new Properties();
        try {
            FileInputStream ip = new FileInputStream("./src/test/resources/application.properties");
            prop.load(ip);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return prop;
    }

    public void setPropertiesFromJenkinsPropertiesFile() {

    }
}
