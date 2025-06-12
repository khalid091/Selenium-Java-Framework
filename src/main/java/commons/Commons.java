package commons;

import java.net.URL;
import java.io.InputStream;
import java.util.Map;
import org.yaml.snakeyaml.Yaml;
import java.io.IOException;
import java.io.FileInputStream;

import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import utils.SeleniumUtils;

public class Commons {
    private static RemoteWebDriver driver;
    private static Commons instance;
    protected SeleniumUtils seleniumUtils;
    
    private Commons() {
        // Private constructor to prevent instantiation
    }
    
    public static Commons getInstance() {
        if (instance == null) {
            instance = new Commons();
        }
        return instance;
    }
    
    public static RemoteWebDriver getDriver() {
        if (driver == null) {
            initializeDriver();
        }
        return driver;
    }
    
    private static void initializeDriver() {
        Yaml yaml = new Yaml();
        try (InputStream in = new FileInputStream("src/test/resources/config/lambdatest-config.yaml")) {
            Map<String, Object> config = yaml.load(in);

            DesiredCapabilities capabilities = new DesiredCapabilities();
            capabilities.setCapability("browserName", config.get("browserName"));
            capabilities.setCapability("browserVersion", config.get("browserVersion"));

            @SuppressWarnings("unchecked")
            Map<String, Object> ltOptions = ((Map<String, Object>) config.get("LT_Options"));
            capabilities.setCapability("LT:Options", ltOptions);

            String hubUrl = (String) config.get("hubUrl");
            driver = new RemoteWebDriver(new URL(hubUrl), capabilities);
            
            // Initialize SeleniumUtils for the instance
            if (instance != null) {
                instance.seleniumUtils = new SeleniumUtils(driver);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void quitDriver() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }
    
    public SeleniumUtils getSeleniumUtils() {
        if (seleniumUtils == null && driver != null) {
            seleniumUtils = new SeleniumUtils(driver);
        }
        return seleniumUtils;
    }
}
