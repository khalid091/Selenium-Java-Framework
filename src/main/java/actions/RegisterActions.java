package actions;

import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;
import page.ecomqa.Register_Page.RegisterPage;
import page.ecomqa.Register_Page.locators.RegisterPageLocators;
import utils.SeleniumUtils;
import commons.DriverManager;
import commons.ConfigManager;

public class RegisterActions {
    private final RemoteWebDriver driver;
    private final SeleniumUtils seleniumUtils;
    private final RegisterPage registerPage;
    private final String loginUrl;

    public RegisterActions() {
        this.driver = DriverManager.getDriver();
        this.seleniumUtils = new SeleniumUtils(driver);
        this.loginUrl = ConfigManager.getInstance().getLoginUrl();
        this.registerPage = new RegisterPage(driver);
    }

    public void navigateToLoginPage() {
        seleniumUtils.navigateTo(loginUrl);
    }

    public void verifyTheLoginPage() {
        Assert.assertTrue(registerPage.signupHeaderElement(), "Signup header is not displayed");
        Assert.assertTrue(registerPage.inputUsernameElement(), "Username input is not displayed");
        Assert.assertTrue(registerPage.inputEmailElement(), "Email input is not displayed");
        Assert.assertTrue(registerPage.signupButtonElement(), "Signup button is not displayed");
    }

    public void inputUsername(String username) {
        seleniumUtils.clearAndSendKeys(RegisterPageLocators.USERNAME_INPUT, username);
    }

    public void inputEmail(String email) {
        seleniumUtils.clearAndSendKeys(RegisterPageLocators.EMAIL_INPUT, email);
    }

    public void clickSignupButton() {
        seleniumUtils.click(RegisterPageLocators.SIGNUP_BUTTON);
    }
}
