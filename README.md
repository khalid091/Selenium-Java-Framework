# Selenium Java BDD Framework

A robust test automation framework built with Selenium WebDriver, Java, and Cucumber BDD.

## Project Structure
```
selenium-java-bdd/
├── src/
│   ├── main/
│   │   └── java/
│   │       ├── actions/          # Page actions and business logic
│   │       ├── commons/          # Common utilities and configurations
│   │       └── page/             # Page Object Model classes
│   └── test/
│       ├── java/
│       │   ├── runners/          # Test runners
│       │   └── stepdefinitions/  # Cucumber step definitions
│       └── resources/
│           ├── features/         # Cucumber feature files
│           ├── testdata/         # Test data (Excel files)
│           └── config/           # Configuration files
└── pom.xml                       # Maven dependencies
```

## Dependencies (POM Model)
Key dependencies in `pom.xml`:
- Selenium WebDriver (4.18.1)
- Cucumber (7.16.1)
- TestNG (7.9.0)
- Apache POI (5.2.4) - For Excel operations
- ExtentReports (5.1.1) - For reporting

## Test Data Management
- Test data is stored in Excel files under `src/test/resources/testdata/`
- Excel structure:
  ```
  Sheet1:
  | Username | Email                |
  |----------|---------------------|
  | user1    | user1@example.com   |
  | user2    | user2@example.com   |
  ```
- Data is read using `ExcelUtils` class for dynamic test execution

## Cloud Testing Integration

### LambdaTest Integration
The framework is integrated with LambdaTest for cross-browser testing:
```java
// Example configuration in Commons.java
DesiredCapabilities capabilities = new DesiredCapabilities();
capabilities.setCapability("browserName", "Chrome");
capabilities.setCapability("version", "latest");
capabilities.setCapability("platform", "Windows 10");
```

### BrowserStack Integration (Optional)
To integrate with BrowserStack, add these configurations:
```java
// Add to pom.xml
<dependency>
    <groupId>com.browserstack</groupId>
    <artifactId>browserstack-java-sdk</artifactId>
    <version>1.11.0</version>
</dependency>

// Configuration in Commons.java
BrowserStackOptions options = new BrowserStackOptions();
options.setBrowserName("Chrome");
options.setBrowserVersion("latest");
options.setOs("Windows");
options.setOsVersion("10");
```

## Jenkins Integration
The project includes a `Jenkinsfile` for CI/CD pipeline:

### Pipeline Stages
1. **Checkout**: Pulls the latest code
2. **Build**: Compiles the project
3. **Test**: Runs the test suite
4. **Report**: Generates and publishes test reports

### Jenkins Configuration
1. Install required plugins:
   - Pipeline
   - Git Integration
   - Cucumber Reports
   - HTML Publisher
   - Maven Integration

2. Configure tools in Jenkins:
   - JDK 11
   - Maven
   - Git

3. Create a new Pipeline job:
   - Use the provided `Jenkinsfile`
   - Configure Git repository
   - Set build triggers as needed

## Running Tests
1. **Local Execution**:
   ```bash
   mvn clean test
   ```

2. **Jenkins Execution**:
   - Trigger the pipeline manually or
   - Set up webhook for automatic triggers

3. **Cloud Execution**:
   - LambdaTest: Configure credentials in environment variables
   - BrowserStack: Add BrowserStack credentials to Jenkins

### Running Specific Features Using Tags
You can run specific feature files or scenarios using tags in your feature files and TestRunner:

1. Add tags to your feature files:
```gherkin
@smoke
Feature: User Registration
  @regression
  Scenario: Successful registration with valid data
    Given user is on registration page
    When user enters valid registration data
    Then user should be registered successfully
```

2. Configure tags in TestRunner.java:
```java
@CucumberOptions(
    tags = "@smoke or @regression"  // Run scenarios with either @smoke or @regression tag
)
```

Common tag patterns:
- `@smoke` - Run only smoke tests
- `@regression` - Run only regression tests
- `@smoke and @regression` - Run tests with both tags
- `@smoke or @regression` - Run tests with either tag
- `not @smoke` - Run all tests except smoke tests

3. Run tests from command line with specific tags:
```bash
# Run smoke tests
mvn test -Dcucumber.filter.tags="@smoke"

# Run regression tests
mvn test -Dcucumber.filter.tags="@regression"

# Run tests with both tags
mvn test -Dcucumber.filter.tags="@smoke and @regression"
```

## Reports
- Cucumber HTML reports: `target/cucumber-reports/cucumber-pretty.html`
- ExtentReports: `target/extent-reports/`
- Jenkins pipeline reports available in Jenkins dashboard

## Best Practices
1. Keep feature files focused and atomic
2. Use meaningful step definitions
3. Maintain test data separately
4. Regular updates of dependencies
5. Proper error handling and logging

## Contributing
1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## Checking Test Reports

### Cucumber Reports
After test execution, you can find Cucumber reports in:
```
target/cucumber-reports/
├── cucumber-pretty.html    # Detailed HTML report
├── CucumberTestReport.json # JSON report for Jenkins
└── cucumber-reports.html   # Masterthought report
```

To view the reports:
1. Navigate to `target/cucumber-reports/` in your project
2. Open `cucumber-pretty.html` in a web browser
3. The report shows:
   - Test execution summary
   - Passed/Failed scenarios
   - Step-by-step execution details
   - Screenshots (if configured)
   - Feature and scenario breakdown
   - Execution time statistics
   - Tags and hooks information

### Jenkins Reports
If running in Jenkins:
1. Go to your Jenkins job
2. Click on the latest build
3. Find reports under:
   - "Cucumber Reports" section
   - "Test Results" section

### Command Line Report Generation
To generate reports without running tests:
```bash
# Generate Cucumber reports
mvn verify
```

### Report Customization
To customize Cucumber reports, modify `@CucumberOptions` in TestRunner.java:
```java
@CucumberOptions(
    features = "src/test/resources/features",
    glue = "stepdefinitions",
    plugin = {
        "pretty",
        "html:target/cucumber-reports/cucumber-pretty.html",
        "json:target/cucumber-reports/CucumberTestReport.json",
        "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:"
    }
)
``` 