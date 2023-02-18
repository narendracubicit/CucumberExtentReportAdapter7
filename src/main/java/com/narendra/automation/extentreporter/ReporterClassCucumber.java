package com.narendra.automation.extentreporter;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.narendra.automation.General.Driver;
import com.narendra.automation.Utilities;
import io.cucumber.core.gherkin.Step;
import io.cucumber.java.*;
import org.apache.commons.io.FileUtils;
import org.joda.time.LocalDateTime;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.Assert;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;

public class ReporterClassCucumber {

    //ExtentHtmlReporter htmlReporter;
    static ExtentHtmlReporter htmlReporter;

    static ExtentReports extent;
    //helps to generate the logs in the test report.
    static ExtentTest test;

    @BeforeAll
    public static void startReport() {
        System.out.println("Before all hook is called.");
        // initialize the HtmlReporter
        String time = LocalDateTime.now().toString("MMMdd-yyyy-hhmmssa");
        htmlReporter = new ExtentHtmlReporter(System.getProperty("user.dir") + "/target/extent-reports/testReport-" + time + ".html");

        //initialize ExtentReports and attach the HtmlReporter
        extent = new ExtentReports();
        extent.attachReporter(htmlReporter);

        //htmlReporter.config().setChartVisibilityOnOpen(true);
        htmlReporter.config().setDocumentTitle("Narendra Palla Cucumber Automation Report");
        htmlReporter.config().setReportName("Cucumber Test Report");
        // htmlReporter.config().setTestViewChartLocation(ChartLocation.TOP);
        htmlReporter.config().setTheme(Theme.STANDARD);
        // htmlReporter.config().setTimeStampFormat("EEEE, MMMM dd, yyyy, hh:mm a '('zzz')'");
        htmlReporter.config().setTimeStampFormat("MMM dd, yyyy, hh:mm a '('zzz')'");
    }

    @Before
    public void beforeEachScenario(Scenario scenario) {
        test = extent.createTest(scenario.getName(), scenario.getStatus().name());
    }

    @BeforeStep
    public void beforeEachStep(Scenario scenario) {
        test.log(Status.INFO, "Executing step:: ");
    }

    @AfterStep
    public void afterEachStep(Scenario scenario) throws Exception {
        String imgPathBase64 = attachScreenShot(scenario);
        test.addScreenCaptureFromBase64String(imgPathBase64); // adding at the end of scenario
        test.log(Status.INFO, "Executed Step::");
        String imgTag = "<br></br><img src=\"data:image/gif;base64," + imgPathBase64 + "\" height=\"400\">";
        test.log(Status.INFO, imgTag); // embedded after each step
    }

    @After
    public void afterEachScenario(Scenario scenario) {
        if (scenario.getStatus().name().equals("PASSED")) {
            test.log(Status.PASS, scenario.getName());
        } else if (scenario.getStatus().name().equals("FAILED")) {
            test.log(Status.FAIL, scenario.getName());
        } else if (scenario.getStatus().name().equals("PENDING")) {
            test.log(Status.SKIP, scenario.getName());
        } else if (scenario.getStatus().name().equals("SKIPPED")) {
            test.log(Status.SKIP, scenario.getName());
        }
    }


    @AfterAll
    public static void tearDown() {
        extent.flush();
    }

    public static String attachScreenShot(Scenario scenario) throws IOException {
        File src = ((TakesScreenshot) Driver.createOnlineDriver()).getScreenshotAs(OutputType.FILE);
        byte[] fileContent = FileUtils.readFileToByteArray(src);
        scenario.attach(fileContent, "image/png", scenario.getName());
        return ((TakesScreenshot) Driver.createOnlineDriver()).getScreenshotAs(OutputType.BASE64);
    }
}
