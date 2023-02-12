package com.narendra.automation.stepdefinitions;

import com.narendra.automation.General.Driver;
import com.narendra.automation.pages.FbLoginPage;
import com.narendra.automation.pages.GmailLoginPage;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class StepDefinitionsClass {

    @Given("I print something")
    public void printSomething() {
        System.out.println("Test step passed...");
    }
    @Given("I open {string}")
    public void openSomething(String url) throws Throwable{
        Driver.createOnlineDriver().get(url);
        Thread.sleep(5000);
    }
    @Given("user log into gmail using {string} and {string}")
    public void userLogsIntoApp(String username, String password) throws Throwable{
        GmailLoginPage gmailLoginPage = new GmailLoginPage();
        gmailLoginPage.enterLoginCredentials(username,password);
        Thread.sleep(5000);
    }

    @Given("user log into fb using {string} and {string}")
    public void userLogsIntoFB(String username, String password) throws Throwable{
        new FbLoginPage().enterLoginCredentials(username,password);
        Thread.sleep(5000);
    }
}
