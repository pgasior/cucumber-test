package glue;

import io.cucumber.java.BeforeStep;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import org.example.rest.LoggingFilter;
import org.example.rest.RESTTemplater;

import static org.testng.Assert.assertEquals;


public class StepDefs {

    private String value = "";

    private RESTTemplater templater = new RESTTemplater();

    @Given("value is {}")
    public void value_is(String a) {
        value = a;
    }

    @When("make REST call")
    public void make_REST_call() {

        RestAssured.given().filter(new LoggingFilter(
                query -> currentScenario.attach(templater.getHTMLOutput(query), "text/html", "reponse")))
                .when()
                .get("http://example.com")
                .andReturn();
    }

    @Then("result is expected")
    public void result_is_expected() {
        assertEquals(value, "a");
    }

    private Scenario currentScenario;

    @BeforeStep
    public void beforeStep(Scenario scenario) {
        currentScenario = scenario;
    }

}
