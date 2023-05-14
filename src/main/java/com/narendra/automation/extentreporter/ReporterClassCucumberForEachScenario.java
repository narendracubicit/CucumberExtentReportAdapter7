package com.narendra.automation.extentreporter;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.narendra.automation.General.Driver;
import io.cucumber.java.*;
import io.cucumber.plugin.EventListener;
import io.cucumber.plugin.event.*;
import org.apache.commons.io.FileUtils;
import org.joda.time.LocalDateTime;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.io.File;
import java.io.IOException;

public class ReporterClassCucumberForEachScenario implements EventListener {


    public static ExtentReports extent;
    //helps to generate the logs in the test report.
    public static ExtentTest test;


    private static int counter = 0;
    private static PickleStepTestStep pickleStepTestStep;

    /*
    step details are coming from teststepstarted event, not from before step
     */
    @Override
    public void setEventPublisher(EventPublisher eventPublisher) {
        eventPublisher.registerHandlerFor(TestStepStarted.class, stepStartedHandler);
        eventPublisher.registerHandlerFor(TestStepFinished.class, stepFinishedHandler);
    }

    private EventHandler<TestStepStarted> stepStartedHandler = new EventHandler<TestStepStarted>() {
        @Override
        public void receive(TestStepStarted event) {
            handleTestStepStarted(event);
        }
    };
    private EventHandler<TestStepFinished> stepFinishedHandler = new EventHandler<TestStepFinished>() {
        @Override
        public void receive(TestStepFinished event) {
            handleTestStepFinished(event);
        }
    };

    private synchronized void handleTestStepStarted(TestStepStarted event) {
        if (event.getTestStep() instanceof PickleStepTestStep) {
            pickleStepTestStep = (PickleStepTestStep) event.getTestStep();
            test.log(com.aventstack.extentreports.Status.INFO, MarkupHelper.createLabel("Executing step::" + pickleStepTestStep.getStep().getText(), ExtentColor.YELLOW));
        } else if (event.getTestStep() instanceof HookTestStep) {
            HookTestStep hookTestStep = (HookTestStep) event.getTestStep();
        }
    }

    private synchronized void handleTestStepFinished(TestStepFinished event) {
        if (event.getTestStep() instanceof PickleStepTestStep) {
            pickleStepTestStep = (PickleStepTestStep) event.getTestStep();
        } else if (event.getTestStep() instanceof HookTestStep) {
            HookTestStep hookTestStep = (HookTestStep) event.getTestStep();
        }
    }

    @Before
    public void beforeEachScenario(Scenario scenario) {
        ExtentHtmlReporter htmlReporter;
        String time = LocalDateTime.now().toString("MMMdd-yyyy-hhmmssa");
        htmlReporter = new ExtentHtmlReporter(System.getProperty("user.dir") + "/target/testReport-"+time+".html");
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
        htmlReporter.config().setTimeStampFormat("MMM dd, yyyy, hh:mm a '('zzz')'");


        test = extent.createTest(scenario.getName(), "Testing the scenario:" + scenario.getName());
        test.assignAuthor("Narendra Palla");
    }

    @AfterStep
    public void afterEachStep(Scenario scenario) throws Exception {
        String imgPathBase64 = attachScreenShot(scenario);
        test.addScreenCaptureFromBase64String(imgPathBase64); // adding at the end of scenario
        test.log(com.aventstack.extentreports.Status.INFO, MarkupHelper.createLabel("Executed Step::" + pickleStepTestStep.getStep().getText(), ExtentColor.YELLOW));
        String imgTag = "<br></br><img src=\"data:image/gif;base64," + imgPathBase64 + "\" height=\"330\">";
        test.log(com.aventstack.extentreports.Status.INFO, imgTag); // embedded after each step
    }

    @After
    public void afterEachScenario(Scenario scenario) throws Exception {
        String imgPathBase64 = attachScreenShot(scenario);
        String imgTag = "<br></br><img src=\"data:image/gif;base64," + imgPathBase64 + "\" height=\"330\">";
        if (scenario.getStatus().name().equals("PASSED")) {
            test.log(com.aventstack.extentreports.Status.PASS, MarkupHelper.createLabel(scenario.getName(), ExtentColor.GREEN));
            test.log(com.aventstack.extentreports.Status.PASS, imgTag);
            //testSpark.log(Status.PASS, MarkupHelper.createLabel(scenario.getName(), ExtentColor.GREEN));
        } else if (scenario.getStatus().name().equals("FAILED")) {
            test.log(com.aventstack.extentreports.Status.FAIL, MarkupHelper.createLabel(scenario.getName(), ExtentColor.RED));
            test.log(com.aventstack.extentreports.Status.FAIL, imgTag);
            //testSpark.log(Status.FAIL, MarkupHelper.createLabel(scenario.getName(), ExtentColor.RED));
        } else if (scenario.getStatus().name().equals("PENDING")) {
            test.log(com.aventstack.extentreports.Status.SKIP, MarkupHelper.createLabel(scenario.getName(), ExtentColor.ORANGE));
            test.log(com.aventstack.extentreports.Status.SKIP, imgTag);
            //testSpark.log(Status.SKIP, MarkupHelper.createLabel(scenario.getName(), ExtentColor.ORANGE));
        } else if (scenario.getStatus().name().equals("SKIPPED")) {
            test.log(com.aventstack.extentreports.Status.SKIP, MarkupHelper.createLabel(scenario.getName(), ExtentColor.PURPLE));
            test.log(Status.SKIP, imgTag);
            //testSpark.log(Status.SKIP, MarkupHelper.createLabel(scenario.getName(), ExtentColor.PURPLE));
        }
        extent.flush();
    }


    public static String attachScreenShot(Scenario scenario) throws IOException {
        File src = ((TakesScreenshot) Driver.getDriver()).getScreenshotAs(OutputType.FILE);
        byte[] fileContent = FileUtils.readFileToByteArray(src);
        scenario.attach(fileContent, "image/png", scenario.getName());
        return ((TakesScreenshot) Driver.getDriver()).getScreenshotAs(OutputType.BASE64);
    }

}
