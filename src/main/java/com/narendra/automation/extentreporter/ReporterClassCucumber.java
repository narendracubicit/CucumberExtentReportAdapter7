package com.narendra.automation.extentreporter;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.narendra.automation.General.Driver;
import io.cucumber.java.*;
import org.apache.commons.io.FileUtils;
import org.joda.time.LocalDateTime;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import java.io.File;
import java.io.IOException;

public class ReporterClassCucumber {
    public static ExtentHtmlReporter htmlReporter;
    public static ExtentSparkReporter sparkReporter;

    public static ExtentReports extent;
    public static ExtentReports extentSpark;
    //helps to generate the logs in the test report.
    public static ExtentTest test;
    public static ExtentTest testSpark;

    @BeforeAll
    public static void startReport() {
        String time = LocalDateTime.now().toString("MMMdd-yyyy-hhmmssa");
        // htmlReporter = new ExtentHtmlReporter(System.getProperty("user.dir") + "/target/extent-reports/testReport-" + time + ".html");
        htmlReporter = new ExtentHtmlReporter(System.getProperty("user.dir") + "/target/testReport.html");
        extent = new ExtentReports();
        extent.attachReporter(htmlReporter);

        extent.setSystemInfo("Author", "Narendra Palla");
        extent.setSystemInfo("Environment", "Staging1");
        extent.setSystemInfo("Region", "EC2-Hyd");
        extent.setSystemInfo("Platform", "Windows");
        extent.setSystemInfo("Browser Type", "Desktop Browser");
        extent.setSystemInfo("Browser Name", "Chrome");

        htmlReporter.config().setDocumentTitle("Narendra Palla Cucumber Automation Report");
        htmlReporter.config().setReportName("Cucumber Test Report");
        htmlReporter.config().setTheme(Theme.DARK);
        // htmlReporter.config().setTimeStampFormat("EEEE, MMMM dd, yyyy, hh:mm a '('zzz')'");
        htmlReporter.config().setTimeStampFormat("MMM dd, yyyy, hh:mm a '('zzz')'");

        /*
        sparkReporter = new ExtentSparkReporter(System.getProperty("user.dir") + "/target/sparkReport.html");

        extentSpark.attachReporter(sparkReporter);
        extentSpark.setSystemInfo("Author", "Narendra Palla");
        extentSpark.setSystemInfo("Environment", "Staging1");
        extentSpark.setSystemInfo("Region", "EC2-Hyd");
        extentSpark.setSystemInfo("Platform", "Windows");
        extentSpark.setSystemInfo("Browser Type", "Desktop Browser");
        extentSpark.setSystemInfo("Browser Name", "Chrome");

        sparkReporter.config().setDocumentTitle("Narendra Palla Cucumber Automation Report");
        sparkReporter.config().setReportName("Cucumber Test Report");
        sparkReporter.config().setTheme(Theme.DARK);
        sparkReporter.config().setTimeStampFormat("MMM dd, yyyy, hh:mm a '('zzz')'");
        */
    }

    @Before
    public void beforeEachScenario(Scenario scenario) {
        test = extent.createTest(scenario.getName());
        //testSpark = extentSpark.createTest(scenario.getName());
    }

    @BeforeStep
    public void beforeEachStep(Scenario scenario) {
        test.log(Status.INFO, MarkupHelper.createLabel("Executing step::", ExtentColor.YELLOW));
        //testSpark.log(Status.INFO, MarkupHelper.createLabel("Executing step::", ExtentColor.YELLOW));
    }

    @AfterStep
    public void afterEachStep(Scenario scenario) throws Exception {
        String imgPathBase64 = attachScreenShot(scenario);
        test.addScreenCaptureFromBase64String(imgPathBase64); // adding at the end of scenario
        test.log(Status.INFO, MarkupHelper.createLabel("Executed Step::", ExtentColor.ORANGE));
        String imgTag = "<br></br><img src=\"data:image/gif;base64," + imgPathBase64 + "\" height=\"400\">";
        test.log(Status.INFO, imgTag); // embedded after each step

        //testSpark.log(Status.INFO,imgTag);
    }

    @After
    public void afterEachScenario(Scenario scenario) {
        if (scenario.getStatus().name().equals("PASSED")) {
            test.log(Status.PASS, MarkupHelper.createLabel(scenario.getName(), ExtentColor.GREEN));
            //testSpark.log(Status.PASS, MarkupHelper.createLabel(scenario.getName(), ExtentColor.GREEN));
        } else if (scenario.getStatus().name().equals("FAILED")) {
            test.log(Status.FAIL, MarkupHelper.createLabel(scenario.getName(), ExtentColor.RED));
            //testSpark.log(Status.FAIL, MarkupHelper.createLabel(scenario.getName(), ExtentColor.RED));
        } else if (scenario.getStatus().name().equals("PENDING")) {
            test.log(Status.SKIP, MarkupHelper.createLabel(scenario.getName(), ExtentColor.ORANGE));
            //testSpark.log(Status.SKIP, MarkupHelper.createLabel(scenario.getName(), ExtentColor.ORANGE));
        } else if (scenario.getStatus().name().equals("SKIPPED")) {
            test.log(Status.SKIP, MarkupHelper.createLabel(scenario.getName(), ExtentColor.PURPLE));
            //testSpark.log(Status.SKIP, MarkupHelper.createLabel(scenario.getName(), ExtentColor.PURPLE));
        }
    }


    @AfterAll
    public static void tearDown() {
        extent.flush();
        //extentSpark.flush();
    }

    public static String attachScreenShot(Scenario scenario) throws IOException {
        File src = ((TakesScreenshot) Driver.createOnlineDriver()).getScreenshotAs(OutputType.FILE);
        byte[] fileContent = FileUtils.readFileToByteArray(src);
        scenario.attach(fileContent, "image/png", scenario.getName());
        return ((TakesScreenshot) Driver.createOnlineDriver()).getScreenshotAs(OutputType.BASE64);
    }
}
