package commons;

import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.InputStream;
import java.io.FileInputStream;
import java.net.URL;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DriverManager {
    private static final Logger logger = LogManager.getLogger(DriverManager.class);
    private static final ThreadLocal<RemoteWebDriver> driverThread = new ThreadLocal<>();
    private static final String CONFIG_PATH = "src/test/resources/config/lambdatest-config.yaml";

    public static RemoteWebDriver getDriver() {
        if (driverThread.get() == null) {
            logger.info("Initializing new WebDriver instance for thread: {}", Thread.currentThread().getName());
            driverThread.set(initializeDriver());
        } else {
            logger.debug("Returning existing WebDriver instance for thread: {}", Thread.currentThread().getName());
        }
        return driverThread.get();
    }

    private static RemoteWebDriver initializeDriver() {
        try (InputStream input = new FileInputStream(CONFIG_PATH)) {
            logger.info("Loading configuration from {}", CONFIG_PATH);
            Yaml yaml = new Yaml();
            Map<String, Object> config = yaml.load(input);

            DesiredCapabilities capabilities = new DesiredCapabilities();
            @SuppressWarnings("unchecked")
            Map<String, Object> browserConfig = (Map<String, Object>) config.get("browser");

            logger.info("Setting browser capabilities: browserName={}, browserVersion={}",
                    browserConfig.get("browserName"), browserConfig.get("browserVersion"));
            capabilities.setCapability("browserName", browserConfig.get("browserName"));
            capabilities.setCapability("browserVersion", browserConfig.get("browserVersion"));

            @SuppressWarnings("unchecked")
            Map<String, Object> ltOptions = (Map<String, Object>) browserConfig.get("LT_Options");
            capabilities.setCapability("LT:Options", ltOptions);

            String hubUrl = (String) browserConfig.get("hubUrl");

            if (hubUrl == null) {
                logger.error("hubUrl is null in the config file. Please check your configuration.");
                throw new RuntimeException("hubUrl is null in the config file. Please check your configuration.");
            }

            logger.info("Connecting to Selenium hub at {}", hubUrl);
            return new RemoteWebDriver(new URL(hubUrl), capabilities);

        } catch (Exception e) {
            logger.error("Failed to initialize WebDriver", e);
            throw new RuntimeException("Failed to initialize WebDriver", e);
        }
    }

    public static void quitDriver() {
        RemoteWebDriver driver = driverThread.get();
        if (driver != null) {
            logger.info("Quitting WebDriver instance for thread: {}", Thread.currentThread().getName());
            driver.quit();
            driverThread.remove();
        } else {
            logger.debug("No WebDriver instance found for thread: {}", Thread.currentThread().getName());
        }
    }
}