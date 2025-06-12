package page.ecomqa.Register_Page.locators;

import org.openqa.selenium.By;

public class RegisterPageLocators {
    public static final By USERNAME_INPUT = By.xpath("//input[@data-qa='signup-name']");
    public static final By EMAIL_INPUT = By.xpath("//input[@data-qa='signup-email']");
    public static final By SIGNUP_BUTTON = By.xpath("//button[@data-qa='signup-button']");
    public static final By HEADER_SIGNUP_TEXT = By.xpath("//h2[contains(text(),'New User Signup!')]");


    
}
