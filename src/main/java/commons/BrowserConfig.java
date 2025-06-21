package commons;

import java.util.Map;
import java.util.Objects;

/**
 * Strongly-typed configuration class for browser settings.
 * Provides compile-time safety and better IDE support compared to raw maps.
 */
public class BrowserConfig {
    private String browserName;
    private String browserVersion;
    private String remoteUrl;
    private Map<String, Object> ltOptions;

    // Default constructor for YAML deserialization
    public BrowserConfig() {}

    public BrowserConfig(String browserName, String browserVersion, String remoteUrl, Map<String, Object> ltOptions) {
        this.browserName = browserName;
        this.browserVersion = browserVersion;
        this.remoteUrl = remoteUrl;
        this.ltOptions = ltOptions;
    }

    // Getters
    public String getBrowserName() {
        return browserName;
    }

    public String getBrowserVersion() {
        return browserVersion;
    }

    public String getRemoteUrl() {
        return remoteUrl;
    }

    public Map<String, Object> getLtOptions() {
        return ltOptions;
    }

    // Setters
    public void setBrowserName(String browserName) {
        this.browserName = browserName;
    }

    public void setBrowserVersion(String browserVersion) {
        this.browserVersion = browserVersion;
    }

    public void setRemoteUrl(String remoteUrl) {
        this.remoteUrl = remoteUrl;
    }

    public void setLtOptions(Map<String, Object> ltOptions) {
        this.ltOptions = ltOptions;
    }

    /**
     * Validates that all required fields are present and not null.
     * @throws IllegalStateException if any required field is missing.
     */
    public void validate() {
        Objects.requireNonNull(browserName, "browserName must not be null");
        Objects.requireNonNull(browserVersion, "browserVersion must not be null");
        Objects.requireNonNull(remoteUrl, "remoteUrl must not be null");
    }

    @Override
    public String toString() {
        return "BrowserConfig{" +
                "browserName='" + browserName + '\'' +
                ", browserVersion='" + browserVersion + '\'' +
                ", remoteUrl='" + remoteUrl + '\'' +
                ", ltOptions=" + ltOptions +
                '}';
    }
} 