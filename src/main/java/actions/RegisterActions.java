package actions;

import commons.Commons;
import page.ecomqa.Register_Page.RegisterPage;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;
import page.ecomqa.Register_Page.locators.RegisterPageLocators;

public class RegisterActions {
    private static final String BASE_URL = "https://automationexercise.com";
    private static final String LOGIN_URL = BASE_URL + "/login";
    private final RemoteWebDriver driver;
    private final Commons commons;
    private final RegisterPage registerPage;

    public RegisterActions() {
        this.commons = Commons.getInstance();
        this.driver = Commons.getDriver();
        this.registerPage = new RegisterPage(driver);
    }

    public void initializeDriver() {
        Commons.getDriver();
    }

    public void navigateToLoginPage() {
        commons.getSeleniumUtils().navigateTo(LOGIN_URL);
    }

    public void verifyTheLoginPage() {
        Assert.assertTrue(registerPage.signupHeaderElement(), "Signup header is not displayed");
        Assert.assertTrue(registerPage.inputUsernameElement("testuser01"), "Username input is not displayed");
        Assert.assertTrue(registerPage.inputEmailElement("testuser01@mailinator.com"), "Email input is not displayed");
        Assert.assertTrue(registerPage.signupButtonElement(), "Signup button is not displayed");
    }

    public void inputUsername(String username) {
        commons.getSeleniumUtils().clearAndSendKeys(RegisterPageLocators.USERNAME_INPUT, username);
    }

    public void inputEmail(String email){
        commons.getSeleniumUtils().clearAndSendKeys(RegisterPageLocators.EMAIL_INPUT, email);
    }

    public void clickSignupButton(){
        commons.getSeleniumUtils().click(RegisterPageLocators.SIGNUP_BUTTON);
    }

}
