package stepdefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import actions.RegisterActions;
import data.TestDataProvider;
import commons.DriverManager;

/**
 * Step definitions for user registration functionality.
 * Handles the complete registration flow.
 */
public class RegisterStepDefinitions {
    private RegisterActions registerActions;
    private String username;
    private String email;
    
    public RegisterStepDefinitions() {
        this.registerActions = new RegisterActions();
    }

    @Before
    public void setUp() {
        DriverManager.getDriver();
    }
    
    @Given("user is on the login page")
    public void userOnTheLoginPage() {
        registerActions.navigateToLoginPage();
        registerActions.verifyTheLoginPage();
    }

    @When("user fills username and email from row {int}")
    public void fillUsernameAndEmailFromRow(int row) {
        // Get test data from the specified row
        this.username = TestDataProvider.getUsername(row);
        this.email = TestDataProvider.getEmail(row);
        
        registerActions.inputUsername(username);
        registerActions.inputEmail(email);
    }

    @And("user clicks signup button")
    public void clickSignupButton() {
        registerActions.clickSignupButton();
    }

    @After
    public void tearDown() {
        shutdown();
    }

    /**
     * Shutdown method for cleanup. Ensures all drivers are properly closed.
     */
    public void shutdown() {
        DriverManager.shutdown();
    }
} 