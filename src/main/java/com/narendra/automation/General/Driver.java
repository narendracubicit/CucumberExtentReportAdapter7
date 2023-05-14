package com.narendra.automation.General;

import com.narendra.automation.Utilities;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class Driver {

    private static WebDriver generalDriver;
    private static Logger logger = Logger.getLogger(Driver.class.getName());

    private static WebDriver createOnlineDriver() {
        String browser = Config.browser;
        String runTestOnCloud = Config.runTestOnCloud;
        logger.info("Run Test on cloud:" + runTestOnCloud + " and browser:" + browser);
        if (runTestOnCloud.equalsIgnoreCase("true")) {
            return createDriverOnSeleniumGrid4(browser);
        } else {
            switch (browser) {
                case "chrome":
                    ChromeOptions options = new ChromeOptions();
                    options.addArguments("--start-fullscreen");
                    options.addArguments("--log-level=3");
                    options.addArguments("--silent");
                    WebDriverManager.chromedriver().setup();
                    generalDriver = new ChromeDriver(options);
                    break;
                case "firefox":
                    WebDriverManager.firefoxdriver().setup();
                    generalDriver = new FirefoxDriver();
                    break;
                case "edge":
                    WebDriverManager.edgedriver().setup();
                    generalDriver = new EdgeDriver();
            }
        }
        return generalDriver;
    }

    private static WebDriver createDriverOnSeleniumGrid4(String browser) {
        DesiredCapabilities caps = new DesiredCapabilities();
        switch (browser) {
            case "chrome":
                caps.setBrowserName("chrome");
                break;
            case "firefox":
                caps.setBrowserName("Firefox");
                break;
            case "edge":
                caps.setBrowserName("MicrosoftEdge");
                break;
        }
        try {
            generalDriver = new RemoteWebDriver(new URL(Config.GRID_URL), caps);
        } catch (Exception e) {
            System.out.println("Error while launching remote web driver");
        }
        return generalDriver;
    }

    public static WebDriver getDriver() {
        if (generalDriver == null) {
            return createOnlineDriver();
        }
        return generalDriver;
    }
}
