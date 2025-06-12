package commons;

import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.InputStream;
import java.io.FileInputStream;
import java.net.URL;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;

public class DriverManager {
    private static final ThreadLocal<RemoteWebDriver> driverThread = new ThreadLocal<>();
    private static final String CONFIG_PATH = "src/test/resources/config/lambdatest-config.yaml";

    public static RemoteWebDriver getDriver() {
        if (driverThread.get() == null) {
            driverThread.set(initializeDriver());
        }
        return driverThread.get();
    }

    private static RemoteWebDriver initializeDriver() {
        try (InputStream input = new FileInputStream(CONFIG_PATH)) {
            Yaml yaml = new Yaml();
            Map<String, Object> config = yaml.load(input);

            DesiredCapabilities capabilities = new DesiredCapabilities();
            @SuppressWarnings("unchecked")
            Map<String, Object> browserConfig = (Map<String, Object>) config.get("browser");
            
            capabilities.setCapability("browserName", browserConfig.get("browserName"));
            capabilities.setCapability("browserVersion", browserConfig.get("browserVersion"));
            
            @SuppressWarnings("unchecked")
            Map<String, Object> ltOptions = (Map<String, Object>) browserConfig.get("LT_Options");
            capabilities.setCapability("LT:Options", ltOptions);

            String hubUrl = (String) browserConfig.get("hubUrl");
            
            if (hubUrl == null) {
                throw new RuntimeException("hubUrl is null in the config file. Please check your configuration.");
            }

            return new RemoteWebDriver(new URL(hubUrl), capabilities);

        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize WebDriver", e);
        }
    }

    public static void quitDriver() {
        RemoteWebDriver driver = driverThread.get();
        if (driver != null) {
            driver.quit();
            driverThread.remove();
        }
    }
}
