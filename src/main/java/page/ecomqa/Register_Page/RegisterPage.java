package page.ecomqa.Register_Page;

import page.ecomqa.Register_Page.locators.RegisterPageLocators;
import org.openqa.selenium.remote.RemoteWebDriver;
import utils.SeleniumUtils;

public class RegisterPage {
    private SeleniumUtils seleniumUtils;

    public RegisterPage(RemoteWebDriver driver) {
        this.seleniumUtils = new SeleniumUtils(driver);
    }

    public boolean signupHeaderElement() {
        return seleniumUtils.isElementDisplayed(RegisterPageLocators.HEADER_SIGNUP_TEXT);
    }

    public boolean inputUsernameElement() {
       return seleniumUtils.isElementDisplayed(RegisterPageLocators.USERNAME_INPUT);
    }

    public boolean inputEmailElement() {
        return seleniumUtils.isElementDisplayed(RegisterPageLocators.EMAIL_INPUT);
    }

    public boolean signupButtonElement() {
        return seleniumUtils.isElementDisplayed(RegisterPageLocators.SIGNUP_BUTTON);
    }
}
