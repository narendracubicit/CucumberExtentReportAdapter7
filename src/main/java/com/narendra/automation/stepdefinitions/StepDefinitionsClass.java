package com.narendra.automation.stepdefinitions;

import com.aventstack.extentreports.Status;
import com.narendra.automation.General.Driver;
import com.narendra.automation.extentreporter.ReporterClassCucumber;
import com.narendra.automation.extentreporter.ReporterClassCucumberForEachScenario;
import com.narendra.automation.pages.FbLoginPage;
import com.narendra.automation.pages.GmailLoginPage;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class StepDefinitionsClass {

    @Given("I print something")
    public void printSomething() {
        ReporterClassCucumberForEachScenario.test.log(Status.INFO,"Printed something..");
    }

    @Given("I open {string}")
    public void openSomething(String url) throws Throwable{
        new FbLoginPage().launchURL(url);
        ReporterClassCucumberForEachScenario.test.log(Status.INFO,"Opening url "+url);
    }

    @Given("user log into gmail using {string} and {string}")
    public void userLogsIntoApp(String username, String password) throws Throwable{
        ReporterClassCucumberForEachScenario.test.log(Status.INFO,"Entering username: "+username+" and password:"+password);
        GmailLoginPage gmailLoginPage = new GmailLoginPage();
        gmailLoginPage.enterLoginCredentials(username,password);
    }

    @Given("user log into fb using {string} and {string}")
    public void userLogsIntoFB(String username, String password) throws Throwable{
        ReporterClassCucumberForEachScenario.test.log(Status.INFO,"Entering username: "+username+" and password:"+password);
        new FbLoginPage().enterLoginCredentials(username,password);
    }
}
