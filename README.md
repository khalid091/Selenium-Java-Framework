# Selenium Java BDD Framework

A robust, type-safe test automation framework built with Selenium WebDriver, Java, Cucumber BDD, and TestNG. Features strong typing, comprehensive error handling, and cloud testing integration.

## 🚀 Key Features

- **Strong Typing**: BrowserConfig POJO for compile-time safety
- **Thread-Safe**: ThreadLocal WebDriver management for parallel execution
- **Cloud Integration**: LambdaTest integration for cross-browser testing
- **Data-Driven Testing**: Excel-based test data with flexible row access
- **Clean Architecture**: Separation of concerns with Page Objects, Actions, and Commons
- **Comprehensive Logging**: Structured logging with Log4j2
- **CI/CD Ready**: Jenkins pipeline integration

## 📁 Project Structure

```
Selenium-Java-Framework/
├── src/
│   ├── main/
│   │   └── java/
│   │       ├── actions/              # Business logic and page actions
│   │       │   └── RegisterActions.java
│   │       ├── commons/              # Common utilities and configurations
│   │       │   ├── BrowserConfig.java    # Strongly-typed browser configuration
│   │       │   ├── ConfigManager.java    # YAML configuration management
│   │       │   └── DriverManager.java    # Thread-safe WebDriver management
│   │       ├── page/                 # Page Object Model classes
│   │       │   └── ecomqa/
│   │       │       └── Register_Page/
│   │       │           ├── RegisterPage.java
│   │       │           └── locators/
│   │       │               └── RegisterPageLocators.java
│   │       └── utils/                # Utility classes
│   │           └── SeleniumUtils.java
│   └── test/
│       ├── java/
│       │   ├── data/                 # Test data management
│       │   │   └── TestDataProvider.java
│       │   ├── runners/              # Test runners
│       │   │   └── TestRunner.java
│       │   └── stepdefinitions/      # Cucumber step definitions
│       │       └── RegisterStepDefinitions.java
│       └── resources/
│           ├── config/               # Configuration files
│           │   └── lambdatest-config.yaml
│           ├── features/             # Cucumber feature files
│           │   └── register.feature
│           ├── testdata/             # Test data (Excel files)
│           │   └── TestData.xlsx
│           └── log4j2.xml            # Logging configuration
├── drivers/                          # WebDriver executables
├── logs/                             # Test execution logs
├── Jenkinsfile                       # CI/CD pipeline
├── pom.xml                           # Maven dependencies
└── README.md
```

## 🛠️ Dependencies

### Core Dependencies
- **Selenium WebDriver** (4.18.1) - Web automation
- **Cucumber** (7.16.1) - BDD framework
- **TestNG** (7.9.0) - Test framework
- **Apache POI** (5.2.4) - Excel operations
- **SnakeYAML** (2.2) - YAML configuration parsing
- **Log4j2** - Structured logging

### Test Dependencies
- **Cucumber TestNG** - TestNG integration
- **WebDriverManager** - Driver management
- **JUnit** - Additional testing support

## ⚙️ Configuration

### YAML Configuration (`lambdatest-config.yaml`)
```yaml
env:
  BASE_URL: "https://automationexercise.com"
  LOGIN_URL: "/login"

browser:
  browserName: Chrome
  browserVersion: dev
  LT_Options:
    build: Java TestNG Sample
    name: Selenium Test Suite
    username: your_username
    accessKey: your_access_key
    platformName: Windows 10
    resolution: 1024x768
    project: Untitled
    selenium_version: 3.13.0
    driver_version: 130.0.6683.2
  remoteUrl: https://username:accesskey@hub.lambdatest.com/wd/hub
```

### Strong Typing with BrowserConfig
```java
// Type-safe configuration access
BrowserConfig browserConfig = ConfigManager.getInstance().getBrowserConfig();
String browserName = browserConfig.getBrowserName();
String remoteUrl = browserConfig.getRemoteUrl();
```

## 🧪 Test Data Management

### Excel Structure (`TestData.xlsx`)
```
Sheet1:
| Username | Email                |
|----------|---------------------|
| user1    | user1@example.com   |
| user2    | user2@example.com   |
| user3    | user3@example.com   |
```

### Flexible Data Access
```java
// Get data from specific row
String username = TestDataProvider.getUsername(1);
String email = TestDataProvider.getEmail(1);

// Backward compatibility
String username = TestDataProvider.getUsername(); // Uses row 1
```

## 🏃‍♂️ Running Tests

### Local Execution
```bash
# Run all tests
mvn clean test

# Run with specific tags
mvn test -Dcucumber.filter.tags="@smoke"

# Run specific feature
mvn test -Dcucumber.features="src/test/resources/features/register.feature"
```

### Jenkins Pipeline
The project includes a `Jenkinsfile` for CI/CD:
```groovy
pipeline {
    agent any
    stages {
        stage('Checkout') { ... }
        stage('Build') { ... }
        stage('Test') { ... }
        stage('Report') { ... }
    }
}
```

## 📊 Test Reports

### Cucumber Reports
- **Location**: `target/cucumber-reports/`
- **Files**: 
  - `cucumber-pretty.html` - Detailed HTML report
  - `CucumberTestReport.json` - JSON report for Jenkins

### Logs
- **Location**: `logs/` directory
- **Format**: Structured Log4j2 logging
- **Levels**: INFO, DEBUG, WARN, ERROR

## 🏗️ Architecture Highlights

### 1. Thread-Safe WebDriver Management
```java
// ThreadLocal ensures each thread gets its own WebDriver instance
private static final ThreadLocal<RemoteWebDriver> driverThread = new ThreadLocal<>();

// Automatic cleanup with shutdown hook
Runtime.getRuntime().addShutdownHook(new Thread(DriverManager::shutdown));
```

### 2. Strong Typing with BrowserConfig POJO
```java
public class BrowserConfig {
    private String browserName;
    private String browserVersion;
    private String remoteUrl;
    private Map<String, Object> ltOptions;
    
    // Getters, setters, and validation
    public void validate() {
        Objects.requireNonNull(browserName, "browserName must not be null");
        // ... other validations
    }
}
```

### 3. Clean Step Definitions
```java
@Given("user is on the login page")
public void userOnTheLoginPage() {
    registerActions.navigateToLoginPage();
    registerActions.verifyTheLoginPage();
}
```

### 4. Type-Safe Configuration Access
```java
// No more string-based lookups
BrowserConfig config = ConfigManager.getInstance().getBrowserConfig();
String url = config.getRemoteUrl(); // Compile-time safety
```

## 🎯 Best Practices Implemented

1. **Separation of Concerns**: Page Objects, Actions, and Commons
2. **Type Safety**: POJOs instead of raw maps
3. **Error Handling**: Proper exception propagation
4. **Thread Safety**: ThreadLocal WebDriver management
5. **Configuration Management**: YAML-based configuration
6. **Data-Driven Testing**: Flexible Excel data access
7. **Clean Code**: No try-catch in step definitions
8. **Comprehensive Logging**: Structured logging with Log4j2

## 🔧 Setup Instructions

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd Selenium-Java-Framework
   ```

2. **Configure LambdaTest credentials**
   - Update `lambdatest-config.yaml` with your credentials
   - Or set environment variables

3. **Install dependencies**
   ```bash
   mvn clean install
   ```

4. **Run tests**
   ```bash
   mvn test
   ```

## 🚀 Cloud Testing Integration

### LambdaTest Setup
1. Sign up for LambdaTest account
2. Get your username and access key
3. Update `lambdatest-config.yaml`
4. Run tests on cloud infrastructure

### Supported Browsers
- Chrome, Firefox, Safari, Edge
- Multiple versions and platforms
- Parallel execution support

## 📈 Performance Features

- **Parallel Execution**: Thread-safe WebDriver management
- **Resource Cleanup**: Automatic driver cleanup
- **Memory Management**: Proper resource disposal
- **Fast Startup**: Efficient configuration loading

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📝 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 🆘 Support

For support and questions:
- Create an issue in the repository
- Check the logs in `logs/` directory
- Review the Cucumber reports in `target/cucumber-reports/` 