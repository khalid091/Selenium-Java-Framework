package stepdefinitions;

import io.cucumber.java.en. *;
import io.cucumber.java.After;
import actions.RegisterActions;
import data.TestDataProvider;
import commons.Commons;

public class RegisterStepDefinitions {
    private RegisterActions registerActions;
    
    public RegisterStepDefinitions() {
        this.registerActions = new RegisterActions();
    }
    
    @Given("user is in the login page")
    public void userOnTheLoginPage() {
        registerActions.initializeDriver();
        registerActions.navigateToLoginPage();
        registerActions.verifyTheLoginPage();
    }

    @When("user fill username and email")
    public void fillUsernameAndEmail() {
        String username = TestDataProvider.getUsername();
        String email = TestDataProvider.getEmail();
        registerActions.inputUsername(username);
        registerActions.inputEmail(email);
    }

    @And("user click signup button")
    public void clickSignupButton(){
        registerActions.clickSignupButton();
    }

    @After
    public void tearDown() {
        Commons.quitDriver();
    }
} 