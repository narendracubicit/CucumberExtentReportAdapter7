package com.narendra.automation.General;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.net.MalformedURLException;
import java.util.concurrent.TimeUnit;

public class Driver {

    private static WebDriver generalDriver;

    public static WebDriver createOnlineDriver() {
        if (generalDriver == null) {
            WebDriverManager.chromedriver().setup();
            generalDriver = new ChromeDriver();
            generalDriver.manage().window().fullscreen();
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--start-fullscreen");
            options.addArguments("--log-level=3");
            options.addArguments("--silent");
        }
        return generalDriver;
    }
}
