package org.example;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;

/**
 * Unit test for simple App.
 */
@CucumberOptions( plugin = {"json:target/cucumber-report/cucumber.json",
        "html:target/cucumber-html-report/report.html",
"progress"},
        glue = {"glue"},
        features = {"src/test/resources/"})
public class AppTest extends AbstractTestNGCucumberTests {

    @DataProvider(parallel = true)
    @Override
    public Object[][] scenarios() {
        Object[][] scenarios = super.scenarios();
        return scenarios;
    }
}
