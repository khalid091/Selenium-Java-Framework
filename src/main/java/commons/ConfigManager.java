package commons;

import java.io.InputStream;
import java.io.FileInputStream;
import java.util.Map;
import java.util.HashMap;

import org.yaml.snakeyaml.Yaml;

public class ConfigManager {
    private static final String CONFIG_PATH = "src/test/resources/config/lambdatest-config.yaml";
    private static ConfigManager instance;
    private final Map<String, Object> config;

    private ConfigManager() {
        this.config = loadYamlConfig();
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
            return yaml.load(input);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load config: " + e.getMessage(), e);
        }
    }

    @SuppressWarnings("unchecked")
    public String getBaseUrl() {
        return (String) ((Map<String, Object>) config.get("env")).get("BASE_URL");
    }

    @SuppressWarnings("unchecked")
    public String getLoginUrl() {
        String baseUrl = getBaseUrl();
        String loginPath = (String) ((Map<String, Object>) config.get("env")).get("LOGIN_URL");
        
        // Check if loginPath is a relative path and construct the full URL
        if (loginPath.startsWith("/")) {
            return baseUrl + loginPath;
        }
        
        return loginPath;
    }

    public Object get(String key) {
        return config.get(key);
    }
}
