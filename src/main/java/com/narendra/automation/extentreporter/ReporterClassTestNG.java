package com.narendra.automation.extentreporter;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.narendra.automation.General.Driver;
import io.cucumber.java.Scenario;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;

public class ReporterClassTestNG {

    //ExtentHtmlReporter htmlReporter;
    ExtentHtmlReporter htmlReporter;

    ExtentReports extent;
    //helps to generate the logs in the test report.
    ExtentTest test;

    @BeforeTest
    public void startReport() {
        // initialize the HtmlReporter
        htmlReporter = new ExtentHtmlReporter(System.getProperty("user.dir") + "/customized/testReport.html");

        //initialize ExtentReports and attach the HtmlReporter
        extent = new ExtentReports();
        extent.attachReporter(htmlReporter);

        //htmlReporter.config().setChartVisibilityOnOpen(true);
        htmlReporter.config().setDocumentTitle("Narendra Palla Cucumber Automation Report");
        htmlReporter.config().setReportName("Cucumber Test Report");
        // htmlReporter.config().setTestViewChartLocation(ChartLocation.TOP);
        htmlReporter.config().setTheme(Theme.STANDARD);
        htmlReporter.config().setTimeStampFormat("EEEE, MMMM dd, yyyy, hh:mm a '('zzz')'");
    }

    @Test
    public void test_1() {
        test = extent.createTest("Test Case 1", "The test case 1 has passed");
        Assert.assertTrue(true);
    }


    @Test
    public void test_2() {
        test = extent.createTest("Test Case 2", "The test case 2 has failed");
        Assert.assertTrue(false);
    }

    @Test
    public void test_3() {
        test = extent.createTest("Test Case 3", "The test case 3 has been skipped");
        throw new SkipException("The test has been skipped");
    }

    @AfterMethod
    public void getResult(ITestResult result) {
        if (result.getStatus() == ITestResult.FAILURE) {
            test.log(Status.FAIL, result.getThrowable());
        } else if (result.getStatus() == ITestResult.SUCCESS) {
            test.log(Status.PASS, result.getTestName());
        } else {
            test.log(Status.SKIP, result.getTestName());
        }
    }

    @AfterTest
    public void tearDown() {
        //to write or update test information to reporter
        extent.flush();
    }

    public static void attachScreenShot(Scenario scenario) throws IOException {
        File src = ((TakesScreenshot) Driver.createOnlineDriver()).getScreenshotAs(OutputType.FILE);
        byte[] fileContent = FileUtils.readFileToByteArray(src);
        scenario.attach(fileContent, "image/png", scenario.getName());
    }
}
