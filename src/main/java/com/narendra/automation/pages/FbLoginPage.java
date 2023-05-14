package com.narendra.automation.pages;

import org.openqa.selenium.By;
import org.testng.Assert;

public class FbLoginPage extends BasePage {

    By emailOrPhoneTextBox = By.name("email");
    By passwordTextBox = By.name("pass");

    By loginBtn = By.name("login");

    public void enterLoginCredentials(String username, String password) {
        webDriver.findElement(emailOrPhoneTextBox).sendKeys(username);
        webDriver.findElement(passwordTextBox).sendKeys(password);
        webDriver.findElement(loginBtn).click();
        Assert.assertTrue(false);
    }

    public void launchURL(String url) {
        webDriver.get(url);
    }
}
