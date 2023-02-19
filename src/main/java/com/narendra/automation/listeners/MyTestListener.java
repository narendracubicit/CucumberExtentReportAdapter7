package com.narendra.automation.listeners;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.gherkin.model.Asterisk;
import com.aventstack.extentreports.model.Test;
import com.aventstack.extentreports.service.ExtentService;
import io.cucumber.messages.types.Examples;
import io.cucumber.messages.types.Scenario;
import io.cucumber.plugin.ConcurrentEventListener;
import io.cucumber.plugin.event.*;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class MyTestListener implements ConcurrentEventListener {
    @Override
    public void setEventPublisher(EventPublisher eventPublisher) {

    }
    /*
    private static Map<String, ExtentTest> featureMap = new ConcurrentHashMap<>();
    private static ThreadLocal<ExtentTest> featureTestThreadLocal = new InheritableThreadLocal<>();
    private static Map<String, ExtentTest> scenarioOutlineMap = new ConcurrentHashMap<>();
    private static ThreadLocal<ExtentTest> scenarioOutlineThreadLocal = new InheritableThreadLocal<>();
    private static ThreadLocal<ExtentTest> scenarioThreadLocal = new InheritableThreadLocal<>();
    private static ThreadLocal<Boolean> isHookThreadLocal = new InheritableThreadLocal<>();
    private static ThreadLocal<ExtentTest> stepTestThreadLocal = new InheritableThreadLocal<>();
    private static ThreadLocal<Set<String>> featureTagsThreadLocal = new InheritableThreadLocal<>();
    private static ThreadLocal<Set<String>> scenarioOutlineTagsThreadLocal = new InheritableThreadLocal<>();

    @SuppressWarnings("serial")
    private static final Map<String, String> MIME_TYPES_EXTENSIONS = new HashMap<String, String>() {
        {
            put("image/bmp", "bmp");
            put("image/gif", "gif");
            put("image/jpeg", "jpeg");
            put("image/jpg", "jpg");
            put("image/png", "png");
            put("image/svg+xml", "svg");
            // TODO Video, txt, html, pdf etc.
            // put("video/ogg", "ogg");
            // put("video/mp4", "mp4");
        }
    };

    private static final AtomicInteger EMBEDDED_INT = new AtomicInteger(0);


    private ThreadLocal<URI> currentFeatureFile = new ThreadLocal<>();
    private ThreadLocal<Scenario> currentScenarioOutline = new InheritableThreadLocal<>();
    private ThreadLocal<Examples> currentExamples = new InheritableThreadLocal<>();

    private EventHandler<TestSourceRead> testSourceReadHandler = new EventHandler<TestSourceRead>() {
        @Override
        public void receive(TestSourceRead event) {
            handleTestSourceRead(event);
        }
    };
    private EventHandler<TestCaseStarted> caseStartedHandler = new EventHandler<TestCaseStarted>() {
        @Override
        public void receive(TestCaseStarted event) {
            handleTestCaseStarted(event);
        }
    };
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


    @Override
    public void setEventPublisher(EventPublisher publisher) {
        publisher.registerHandlerFor(TestSourceRead.class, testSourceReadHandler);
        publisher.registerHandlerFor(TestCaseStarted.class, caseStartedHandler);
        publisher.registerHandlerFor(TestStepStarted.class, stepStartedHandler);
        publisher.registerHandlerFor(TestStepFinished.class, stepFinishedHandler);
        publisher.registerHandlerFor(TestCaseFinished.class, this::handleTestCaseFinished);
    }

    private synchronized void handleTestCaseStarted(TestCaseStarted event) {
        handleStartOfFeature(event.getTestCase());
        handleScenarioOutline(event.getTestCase());
        createTestCase(event.getTestCase());
    }

    private synchronized void handleTestStepStarted(TestStepStarted event) {
        isHookThreadLocal.set(false);

        if (event.getTestStep() instanceof HookTestStep) {
            ExtentTest t = scenarioThreadLocal.get().createNode(Asterisk.class, event.getTestStep().getCodeLocation(),
                    (((HookTestStep) event.getTestStep()).getHookType()).toString().toUpperCase());
            stepTestThreadLocal.set(t);
            isHookThreadLocal.set(true);
        }

        if (event.getTestStep() instanceof PickleStepTestStep) {
            PickleStepTestStep testStep = (PickleStepTestStep) event.getTestStep();
            createTestStep(testStep);
        }
    }

    private synchronized void handleTestStepFinished(TestStepFinished event) {
        updateResult(event.getResult());
    }

    private synchronized void updateResult(Result result) {
        Test test = stepTestThreadLocal.get().getModel();
        switch (result.getStatus().name().toLowerCase()) {
            case "failed":
                stepTestThreadLocal.get().fail(result.getError());
                break;
            case "undefined":
                stepTestThreadLocal.get().fail("Step undefined");
                break;
            case "pending":
                stepTestThreadLocal.get().fail(result.getError());
                break;
            case "skipped":
                if (isHookThreadLocal.get()) {
                    ExtentService.getInstance().removeTest(stepTestThreadLocal.get());
                    break;
                }
                boolean currentEndingEventSkipped = test.hasLog()
                        ? test.getLogs().get(test.getLogs().size() - 1).getStatus() == com.aventstack.extentreports.Status.SKIP
                        : false;
                if (result.getError() != null) {
                    stepTestThreadLocal.get().skip(result.getError());
                } else if (!currentEndingEventSkipped) {
                    String details = result.getError() == null ? "Step skipped" : result.getError().getMessage();
                    stepTestThreadLocal.get().skip(details);
                }
                break;
            case "passed":
                if (stepTestThreadLocal.get() != null) {
                    if (isHookThreadLocal.get()) {
                        boolean mediaLogs = !test.getLogs().stream().filter(l -> l.getMedia() != null)
                                .collect(Collectors.toList()).isEmpty();
                        if (!test.hasLog() && !mediaLogs)
                            ExtentService.getInstance().removeTest(stepTestThreadLocal.get());
                    }
                    stepTestThreadLocal.get().pass("");
                }
                break;
            default:
                break;
        }
    }

    private void handleTestCaseFinished(TestCaseFinished event) {
        TestCase testCase = event.getTestCase();
        Result result = event.getResult();
        Status status = result.getStatus();
        Throwable error = result.getError();
        String scenarioName = testCase.getName();

        TestStep testStep = testCase.getTestSteps().get(0);
        if (testStep instanceof PickleStepTestStep) {
            PickleStepTestStep pickleStepTestStep  = (PickleStepTestStep) testStep;
            String text = pickleStepTestStep.getStep().getText();
        }

        if (testStep instanceof HookTestStep) {
            HookTestStep hookTestStep = (HookTestStep) testStep;
            HookType hookType = hookTestStep.getHookType();
        }
        String id = "" + testCase.getUri() + testCase.getLine();
        System.out.println("Testcase " + id + " - " + status.name());
    }
     */
}