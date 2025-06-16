package commons;

import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.InputStream;
import java.io.FileInputStream;
import java.net.URL;
import java.util.Map;
import java.util.Objects;

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
            Objects.requireNonNull(config, "Failed to load configuration from " + CONFIG_PATH);

            DesiredCapabilities capabilities = new DesiredCapabilities();
            @SuppressWarnings("unchecked")
            Map<String, Object> browserConfig = (Map<String, Object>) config.get("browser");
            Objects.requireNonNull(browserConfig, "'browser' section not found in config");

            String browserName = Objects.requireNonNull(
                (String) browserConfig.get("browserName"),
                "browserName not found in configuration"
            );
            String browserVersion = Objects.requireNonNull(
                (String) browserConfig.get("browserVersion"),
                "browserVersion not found in configuration"
            );

            logger.info("Setting browser capabilities: browserName={}, browserVersion={}",
                    browserName, browserVersion);
            capabilities.setCapability("browserName", browserName);
            capabilities.setCapability("browserVersion", browserVersion);

            @SuppressWarnings("unchecked")
            Map<String, Object> ltOptions = (Map<String, Object>) browserConfig.get("LT_Options");
            Objects.requireNonNull(ltOptions, "LT_Options not found in configuration");
            capabilities.setCapability("LT:Options", ltOptions);

            String hubUrl = Objects.requireNonNull(
                (String) browserConfig.get("hubUrl"),
                "hubUrl not found in configuration"
            );

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