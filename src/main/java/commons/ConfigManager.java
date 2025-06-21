package commons;

import java.io.InputStream;
import java.io.FileInputStream;
import java.util.Map;
import java.util.Objects;

import org.yaml.snakeyaml.Yaml;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Manages application configuration using a singleton pattern.
 * This class loads configuration from a YAML file and provides safe access to its properties.
 */
public class ConfigManager {
    private static final Logger logger = LogManager.getLogger(ConfigManager.class);
    private static final String CONFIG_PATH = "src/test/resources/config/lambdatest-config.yaml";
    private static ConfigManager instance;
    private final Map<String, Object> config;
    private BrowserConfig browserConfig;

    /**
     * Private constructor to ensure a single instance.
     * Loads the YAML configuration and performs an initial null check.
     */
    private ConfigManager() {
        this.config = loadYamlConfig();
        Objects.requireNonNull(config, "Failed to load configuration from " + CONFIG_PATH);
        this.browserConfig = loadBrowserConfig();
    }

    /**
     * Provides the global instance of the ConfigManager.
     * @return The singleton instance of ConfigManager.
     */
    public static ConfigManager getInstance() {
        if (instance == null) {
            instance = new ConfigManager();
        }
        return instance;
    }

    /**
     * Loads the YAML configuration file from the specified path.
     * @return A map representing the loaded YAML configuration.
     * @throws RuntimeException if the file cannot be loaded or is empty.
     */
    private Map<String, Object> loadYamlConfig() {
        try (InputStream input = new FileInputStream(CONFIG_PATH)) {
            Yaml yaml = new Yaml();
            Map<String, Object> loadedConfig = yaml.load(input);
            // Ensure the configuration was actually loaded
            Objects.requireNonNull(loadedConfig, "Configuration file is empty or invalid");
            return loadedConfig;
        } catch (Exception e) {
            logger.error("Failed to load configuration from {}: {}", CONFIG_PATH, e.getMessage());
            throw new RuntimeException("Failed to load config: " + e.getMessage(), e);
        }
    }

    /**
     * Loads and validates the browser configuration from the YAML.
     * @return A validated BrowserConfig instance.
     */
    private BrowserConfig loadBrowserConfig() {
        logger.debug("Loading browser configuration...");
        Map<String, Object> browserMap = getMap("browser");
        if (browserMap == null) {
            throw new RuntimeException("Browser configuration not found in YAML");
        }

        BrowserConfig browserConfig = new BrowserConfig();
        browserConfig.setBrowserName((String) browserMap.get("browserName"));
        browserConfig.setBrowserVersion((String) browserMap.get("browserVersion"));
        browserConfig.setRemoteUrl((String) browserMap.get("remoteUrl"));
        browserConfig.setLtOptions(getMapSafely(browserMap, "LT_Options"));

        // Validate the configuration
        browserConfig.validate();
        logger.debug("Browser configuration loaded successfully: {}", browserConfig);
        return browserConfig;
    }

    /**
     * Retrieves the browser configuration.
     * @return The BrowserConfig instance.
     */
    public BrowserConfig getBrowserConfig() {
        return browserConfig;
    }

    /**
     * Retrieves the base URL from the 'env' section of the configuration.
     * @return The base URL as a string.
     */
    public String getBaseUrl() {
        Map<String, Object> envConfig = getEnvConfig();
        String baseUrl = (String) envConfig.get("BASE_URL");
        return Objects.requireNonNull(baseUrl, "BASE_URL not found in configuration");
    }

    /**
     * Retrieves the full login URL.
     * If the login URL in the config is a relative path, it constructs the full URL.
     * @return The absolute login URL.
     */
    public String getLoginUrl() {
        String baseUrl = getBaseUrl();
        Map<String, Object> envConfig = getEnvConfig();
        String loginPath = (String) envConfig.get("LOGIN_URL");
        Objects.requireNonNull(loginPath, "LOGIN_URL not found in configuration");
        
        // Check if loginPath is a relative path (starts with '/') and construct the full URL
        if (loginPath.startsWith("/")) {
            return baseUrl + loginPath;
        }
        
        // Return as is if it's already a full URL
        return loginPath;
    }

    /**
     * Private helper to safely retrieve the 'env' configuration map.
     * @return The 'env' configuration map.
     */
    @SuppressWarnings("unchecked")
    private Map<String, Object> getEnvConfig() {
        Map<String, Object> envConfig = (Map<String, Object>) config.get("env");
        return Objects.requireNonNull(envConfig, "'env' section not found in configuration");
    }

    /**
     * Generic getter for any top-level key in the configuration.
     * @param key The configuration key to retrieve.
     * @return The value associated with the key, or null if not found.
     */
    public Object get(String key) {
        // Ensure the key is not null before attempting to access the map
        Objects.requireNonNull(key, "Configuration key cannot be null");
        Object value = config.get(key);
        if (value == null) {
            logger.warn("Configuration key '{}' not found", key);
        }
        return value;
    }

    /**
     * Type-safe getter for nested map configurations.
     * @param key The configuration key to retrieve.
     * @return The value as a Map, or null if not found or not a map.
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> getMap(String key) {
        Object value = get(key);
        if (value instanceof Map) {
            return (Map<String, Object>) value;
        }
        logger.warn("Configuration key '{}' is not a map", key);
        return null;
    }

    /**
     * Type-safe getter for string values with support for nested paths.
     * @param path The configuration path (e.g., "browser.remoteUrl").
     * @return The value as a String, or null if not found or not a string.
     */
    public String getString(String path) {
        Object value = getNestedValue(path);
        if (value instanceof String) {
            return (String) value;
        }
        logger.warn("Configuration path '{}' is not a string", path);
        return null;
    }

    /**
     * Helper method to get nested values using dot notation.
     * @param path The dot-separated path (e.g., "browser.remoteUrl").
     * @return The value at the specified path, or null if not found.
     */
    private Object getNestedValue(String path) {
        Objects.requireNonNull(path, "Path cannot be null");
        
        String[] keys = path.split("\\.");
        Object current = config;
        
        for (String key : keys) {
            if (current instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<String, Object> map = (Map<String, Object>) current;
                current = map.get(key);
                if (current == null) {
                    logger.warn("Configuration path '{}' not found", path);
                    return null;
                }
            } else {
                logger.warn("Configuration path '{}' is not a map", path);
                return null;
            }
        }
        
        return current;
    }

    /**
     * Helper method to safely extract a map from a configuration object.
     * @param config The configuration map to extract from.
     * @param key The key to extract.
     * @return The map if found and valid, null otherwise.
     */
    private Map<String, Object> getMapSafely(Map<String, Object> config, String key) {
        Object value = config.get(key);
        if (value instanceof Map) {
            return (Map<String, Object>) value;
        }
        logger.warn("Configuration key '{}' is not a map", key);
        return null;
    }
}
