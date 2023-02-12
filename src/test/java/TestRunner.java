import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        plugin = {"pretty",
                "json:target/cucumber.json",
                "junit:target/cucumberreport/cucumber.xml",
                "html:target/cucumber-reports/cucumberreport.html",
                "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:"},
        features = "E:\\AutomationRelated\\AutomationCucumber Framework2",
        glue = {"com.narendra.automation"}
)
public class TestRunner {

}
