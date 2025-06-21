package commons;

import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.URL;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Manages the lifecycle of RemoteWebDriver instances using a thread-safe approach.
 * This ensures that each thread gets its own isolated WebDriver instance.
 */
public class DriverManager {
    private static final Logger logger = LogManager.getLogger(DriverManager.class);
    // ThreadLocal provides thread-safe variables. Each thread will have its own RemoteWebDriver instance.
    private static final ThreadLocal<RemoteWebDriver> driverThread = new ThreadLocal<>();

    static {
        // Register shutdown hook for cleanup
        Runtime.getRuntime().addShutdownHook(new Thread(DriverManager::shutdown));
    }

    /**
     * Retrieves the WebDriver instance for the current thread.
     * If an instance does not exist, it initializes a new one.
     * @return The RemoteWebDriver instance for the current thread.
     */
    public static RemoteWebDriver getDriver() {
        if (driverThread.get() == null) {
            logger.info("Initializing new WebDriver instance for thread: {}", threadName());
            driverThread.set(initializeDriver());
        } else {
            logger.debug("Returning existing WebDriver instance for thread: {}", threadName());
        }
        return driverThread.get();
    }

    /**
     * Initializes a new RemoteWebDriver instance based on the configuration.
     * @return A new instance of RemoteWebDriver.
     * @throws RuntimeException if the WebDriver cannot be initialized.
     */
    private static RemoteWebDriver initializeDriver() {
        try {
            logger.debug("Fetching browser configuration...");
            ConfigManager configManager = ConfigManager.getInstance();
            BrowserConfig browserConfig = configManager.getBrowserConfig();
            
            logger.debug("Creating DesiredCapabilities from browser config...");
            DesiredCapabilities capabilities = getCapabilities(browserConfig);
            
            logger.debug("Initializing RemoteWebDriver with URL: {}", browserConfig.getRemoteUrl());
            RemoteWebDriver driver = new RemoteWebDriver(new URL(browserConfig.getRemoteUrl()), capabilities);
            logger.info("WebDriver initialized successfully for thread: {}", threadName());
            return driver;
        } catch (Exception e) {
            logger.error("Failed to initialize WebDriver", e);
            throw new RuntimeException("Failed to initialize WebDriver: " + e.getMessage(), e);
        }
    }

    /**
     * Creates DesiredCapabilities from the strongly-typed BrowserConfig.
     * @param browserConfig The browser configuration object.
     * @return Configured DesiredCapabilities instance.
     */
    private static DesiredCapabilities getCapabilities(BrowserConfig browserConfig) {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        
        // Set basic browser capabilities using type-safe access
        capabilities.setCapability("browserName", browserConfig.getBrowserName());
        capabilities.setCapability("browserVersion", browserConfig.getBrowserVersion());
        
        // Set LambdaTest specific options
        Map<String, Object> ltOptions = browserConfig.getLtOptions();
        if (ltOptions != null) {
            capabilities.setCapability("LT:Options", ltOptions);
            logger.debug("LT_Options configured: {}", ltOptions);
        } else {
            logger.warn("Missing LT_Options, continuing without them.");
        }
        
        return capabilities;
    }

    /**
     * Quits the WebDriver instance for the current thread and removes it from the ThreadLocal map.
     */
    public static void quitDriver() {
        RemoteWebDriver driver = driverThread.get();
        if (driver != null) {
            logger.info("Quitting WebDriver instance for thread: {}", threadName());
            driver.quit();
            driverThread.remove();
        } else {
            logger.debug("No WebDriver instance found for thread: {}", threadName());
        }
    }

    /**
     * Shutdown method for cleanup. Can be called explicitly or via shutdown hook.
     * Ensures all drivers are properly closed.
     */
    public static void shutdown() {
        logger.info("Shutting down DriverManager...");
        quitDriver();
        logger.info("DriverManager shutdown complete");
    }

    /**
     * Helper method to get the current thread name for consistent logging.
     * @return The name of the current thread.
     */
    private static String threadName() {
        return Thread.currentThread().getName();
    }
}