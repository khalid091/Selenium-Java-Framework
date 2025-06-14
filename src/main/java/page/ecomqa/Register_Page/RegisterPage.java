package page.ecomqa.Register_Page;

import page.ecomqa.Register_Page.locators.RegisterPageLocators;
import org.openqa.selenium.remote.RemoteWebDriver;
import utils.SeleniumUtils;
import commons.Commons;

public class RegisterPage {
    private SeleniumUtils seleniumUtils;
    private Commons commons;

    public RegisterPage(RemoteWebDriver driver) {
        this.commons = new Commons();
        this.seleniumUtils = commons.getSeleniumUtils();
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
