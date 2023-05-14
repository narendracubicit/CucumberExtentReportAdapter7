package com.narendra.automation.pages;

import com.narendra.automation.General.Driver;
import org.openqa.selenium.WebDriver;

public abstract class BasePage {
    protected WebDriver webDriver;

    protected BasePage() {
        launchDriver();
    }

    private void launchDriver() {
        webDriver = Driver.getDriver();
    }
}
