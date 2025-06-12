# Selenium Java BDD Project

This is a sample Selenium WebDriver project with TestNG integration.

## Prerequisites

- Java JDK 11 or higher
- Maven
- Chrome Browser installed

## Project Structure

```
selenium-java-bdd/
├── src/
│   └── test/
│       └── java/
│           └── com/
│               └── selenium/
│                   └── demo/
│                       └── GoogleSearchTest.java
├── pom.xml
├── testng.xml
└── README.md
```

## Setup

1. Clone the repository
2. Make sure you have Java and Maven installed
3. Install Chrome browser if not already installed

## Running Tests

You can run the tests using Maven:

```bash
mvn clean test
```

Or run the TestNG XML file directly from your IDE.

## Test Description

The sample test performs the following:
1. Opens Google homepage
2. Searches for "Selenium WebDriver"
3. Verifies that the page title contains the search term

## Dependencies

- Selenium WebDriver 4.18.1
- TestNG 7.9.0 