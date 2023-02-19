package com.narendra.automation.extentreporter;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.gherkin.model.Asterisk;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.narendra.automation.General.Driver;
import gherkin.formatter.model.Step;
import io.cucumber.java.*;
import io.cucumber.plugin.ConcurrentEventListener;
import io.cucumber.plugin.EventListener;
import io.cucumber.plugin.event.*;
import org.apache.commons.io.FileUtils;
import org.joda.time.LocalDateTime;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;

public class ReporterClassCucumber implements EventListener {
    public static ExtentHtmlReporter htmlReporter;
    public static ExtentSparkReporter sparkReporter;

    public static ExtentReports extent;
    public static ExtentReports extentSpark;
    //helps to generate the logs in the test report.
    public static ExtentTest test;
    public static ExtentTest testSpark;

    private static int counter = 0;
    private static PickleStepTestStep pickleStepTestStep;

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
        }
    }

    private synchronized void handleTestStepFinished(TestStepFinished event) {
       if (event.getTestStep() instanceof PickleStepTestStep) {
            pickleStepTestStep = (PickleStepTestStep) event.getTestStep();
        }
    }

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
        test = extent.createTest(scenario.getName(), "Testing the scenario:" + scenario.getName());
        test.assignAuthor("Narendra Palla");
        //testSpark = extentSpark.createTest(scenario.getName());
    }

    @BeforeStep
    public void beforeEachStep(Scenario scenario) throws Exception {
        //PickleStepTestStep currentStep = getCurrentStep(scenario);
        test.log(Status.INFO, MarkupHelper.createLabel("Executing step::" + pickleStepTestStep.getStep().getText(), ExtentColor.YELLOW));
    }

    @AfterStep
    public void afterEachStep(Scenario scenario) throws Exception {
        String imgPathBase64 = attachScreenShot(scenario);
        test.addScreenCaptureFromBase64String(imgPathBase64); // adding at the end of scenario
        test.log(Status.INFO, MarkupHelper.createLabel("Executed Step::"+pickleStepTestStep.getStep().getText(), ExtentColor.ORANGE));
        String imgTag = "<br></br><img src=\"data:image/gif;base64," + imgPathBase64 + "\" height=\"400\">";
        test.log(Status.INFO, imgTag); // embedded after each step
    }

    @After
    public void afterEachScenario(Scenario scenario) throws Exception {
        String imgPathBase64 = attachScreenShot(scenario);
        String imgTag = "<br></br><img src=\"data:image/gif;base64," + imgPathBase64 + "\" height=\"400\">";
        if (scenario.getStatus().name().equals("PASSED")) {
            test.log(Status.PASS, MarkupHelper.createLabel(scenario.getName(), ExtentColor.GREEN));
            test.log(Status.PASS, imgTag);
            //testSpark.log(Status.PASS, MarkupHelper.createLabel(scenario.getName(), ExtentColor.GREEN));
        } else if (scenario.getStatus().name().equals("FAILED")) {
            test.log(Status.FAIL, MarkupHelper.createLabel(scenario.getName(), ExtentColor.RED));
            test.log(Status.FAIL, imgTag);
            //testSpark.log(Status.FAIL, MarkupHelper.createLabel(scenario.getName(), ExtentColor.RED));
        } else if (scenario.getStatus().name().equals("PENDING")) {
            test.log(Status.SKIP, MarkupHelper.createLabel(scenario.getName(), ExtentColor.ORANGE));
            test.log(Status.SKIP, imgTag);
            //testSpark.log(Status.SKIP, MarkupHelper.createLabel(scenario.getName(), ExtentColor.ORANGE));
        } else if (scenario.getStatus().name().equals("SKIPPED")) {
            test.log(Status.SKIP, MarkupHelper.createLabel(scenario.getName(), ExtentColor.PURPLE));
            test.log(Status.SKIP, imgTag);
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

    private static PickleStepTestStep getCurrentStep(Scenario scenario) throws Exception {
        Field f = scenario.getClass().getDeclaredField("testCase");
        f.setAccessible(true);
        TestCase r = (TestCase) f.get(scenario);
        List<PickleStepTestStep> stepDefs = r.getTestSteps()
                .stream()
                .filter(x -> x instanceof PickleStepTestStep)
                .map(x -> (PickleStepTestStep) x)
                .collect(Collectors.toList());

        PickleStepTestStep currentStep = stepDefs.get(counter);
        return currentStep;
    }
}
