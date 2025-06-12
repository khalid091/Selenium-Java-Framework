package commons;

import org.openqa.selenium.remote.RemoteWebDriver;
import utils.SeleniumUtils;

public class Commons {

    private final RemoteWebDriver driver;
    private final SeleniumUtils seleniumUtils;

    public Commons() {
        this.driver = DriverManager.getDriver();
        this.seleniumUtils = new SeleniumUtils(driver);
    }

    public RemoteWebDriver getDriver() {
        return driver;
    }

    public SeleniumUtils getSeleniumUtils() {
        return seleniumUtils;
    }

    public String getBaseUrl() {
        return ConfigManager.getInstance().getBaseUrl();
    }

    public String getLoginUrl() {
        return ConfigManager.getInstance().getLoginUrl();
    }
}
