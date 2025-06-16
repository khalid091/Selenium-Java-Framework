package commons;

import java.io.InputStream;
import java.io.FileInputStream;
import java.util.Map;
import java.util.Objects;

import org.yaml.snakeyaml.Yaml;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ConfigManager {
    private static final Logger logger = LogManager.getLogger(ConfigManager.class);
    private static final String CONFIG_PATH = "src/test/resources/config/lambdatest-config.yaml";
    private static ConfigManager instance;
    private final Map<String, Object> config;

    private ConfigManager() {
        this.config = loadYamlConfig();
        Objects.requireNonNull(config, "Failed to load configuration from " + CONFIG_PATH);
    }

    public static ConfigManager getInstance() {
        if (instance == null) {
            instance = new ConfigManager();
        }
        return instance;
    }

    private Map<String, Object> loadYamlConfig() {
        try (InputStream input = new FileInputStream(CONFIG_PATH)) {
            Yaml yaml = new Yaml();
            Map<String, Object> loadedConfig = yaml.load(input);
            Objects.requireNonNull(loadedConfig, "Configuration file is empty or invalid");
            return loadedConfig;
        } catch (Exception e) {
            logger.error("Failed to load configuration from {}: {}", CONFIG_PATH, e.getMessage());
            throw new RuntimeException("Failed to load config: " + e.getMessage(), e);
        }
    }

    public String getBaseUrl() {
        Map<String, Object> envConfig = getEnvConfig();
        String baseUrl = (String) envConfig.get("BASE_URL");
        return Objects.requireNonNull(baseUrl, "BASE_URL not found in configuration");
    }

    public String getLoginUrl() {
        String baseUrl = getBaseUrl();
        Map<String, Object> envConfig = getEnvConfig();
        String loginPath = (String) envConfig.get("LOGIN_URL");
        Objects.requireNonNull(loginPath, "LOGIN_URL not found in configuration");
        
        // Check if loginPath is a relative path and construct the full URL
        if (loginPath.startsWith("/")) {
            return baseUrl + loginPath;
        }
        
        return loginPath;
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> getEnvConfig() {
        Map<String, Object> envConfig = (Map<String, Object>) config.get("env");
        return Objects.requireNonNull(envConfig, "'env' section not found in configuration");
    }

    public Object get(String key) {
        Objects.requireNonNull(key, "Configuration key cannot be null");
        Object value = config.get(key);
        if (value == null) {
            logger.warn("Configuration key '{}' not found", key);
        }
        return value;
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> getBrowserConfig() {
        Map<String, Object> browserConfig = (Map<String, Object>) config.get("browser");
        return Objects.requireNonNull(browserConfig, "'browser' section not found in configuration");
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> getLTOptions() {
        Map<String, Object> browserConfig = getBrowserConfig();
        Map<String, Object> ltOptions = (Map<String, Object>) browserConfig.get("LT_Options");
        return Objects.requireNonNull(ltOptions, "LT_Options not found in configuration");
    }

    public String getBrowserName() {
        Map<String, Object> browserConfig = getBrowserConfig();
        String browserName = (String) browserConfig.get("browserName");
        return Objects.requireNonNull(browserName, "browserName not found in configuration");
    }

    public String getBrowserVersion() {
        Map<String, Object> browserConfig = getBrowserConfig();
        String browserVersion = (String) browserConfig.get("browserVersion");
        return Objects.requireNonNull(browserVersion, "browserVersion not found in configuration");
    }

    public String getHubUrl() {
        Map<String, Object> browserConfig = getBrowserConfig();
        String hubUrl = (String) browserConfig.get("hubUrl");
        return Objects.requireNonNull(hubUrl, "hubUrl not found in configuration");
    }
}
