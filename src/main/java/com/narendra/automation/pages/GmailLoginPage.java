package com.narendra.automation.pages;

import com.narendra.automation.General.Driver;
import org.openqa.selenium.By;

public class GmailLoginPage extends BasePage {

    By emailOrPhoneTextBox = By.xpath("//input[@type='email']");

    public void enterLoginCredentials(String username, String password) {
        webDriver.findElement(emailOrPhoneTextBox).sendKeys(username);
        webDriver.findElement(emailOrPhoneTextBox).sendKeys(username);
    }
}
