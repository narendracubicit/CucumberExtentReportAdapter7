package com.narendra.automation.pages;

import com.narendra.automation.General.Driver;
import org.openqa.selenium.WebDriver;

public class BasePage {
    public static WebDriver webDriver;

    protected BasePage() {
        launchDriver();
    }

    private static void launchDriver() {
        if (webDriver==null) {
            webDriver = Driver.createOnlineDriver();
        }
    }
}
