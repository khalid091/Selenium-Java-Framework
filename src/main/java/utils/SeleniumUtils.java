package utils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.NoSuchElementException;
import java.time.Duration;
import java.util.List;

/**
 * Utility class for Selenium WebDriver operations with centralized exception handling.
 * Provides clear error messages and context for debugging test failures.
 */
public class SeleniumUtils {
    
    private final WebDriver driver;
    private final WebDriverWait wait;
    private final Actions actions;
    private final JavascriptExecutor js;
    
    public SeleniumUtils(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        this.actions = new Actions(driver);
        this.js = (JavascriptExecutor) driver;
    }
    
    // Basic Element Interactions
    public void waitForElementVisible(By locator) {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        } catch (Exception e) {
            throw new SeleniumActionException("Element not visible after waiting: " + locator, e);
        }
    }
    
    public void waitForElementClickable(By locator) {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(locator));
        } catch (Exception e) {
            throw new SeleniumActionException("Element not clickable after waiting: " + locator, e);
        }
    }
    
    public void click(By locator) {
        try {
            waitForElementClickable(locator);
            driver.findElement(locator).click();
        } catch (Exception e) {
            throw new SeleniumActionException("Failed to click element: " + locator, e);
        }
    }
    
    public void sendKeys(By locator, String text) {
        try {
            waitForElementVisible(locator);
            driver.findElement(locator).sendKeys(text);
        } catch (Exception e) {
            throw new SeleniumActionException("Failed to send keys '" + text + "' to element: " + locator, e);
        }
    }
    
    public void clearAndSendKeys(By locator, String text) {
        try {
            waitForElementVisible(locator);
            WebElement element = driver.findElement(locator);
            element.clear();
            element.sendKeys(text);
        } catch (Exception e) {
            throw new SeleniumActionException("Failed to clear and send keys '" + text + "' to element: " + locator, e);
        }
    }
    
    public String getText(By locator) {
        try {
            waitForElementVisible(locator);
            return driver.findElement(locator).getText();
        } catch (Exception e) {
            throw new SeleniumActionException("Failed to get text from element: " + locator, e);
        }
    }
    
    /**
     * Checks if an element is displayed with fail-fast behavior.
     * Returns false only for NoSuchElementException, rethrows other exceptions.
     * @param locator the element locator
     * @return true if element is displayed, false if element not found
     * @throws SeleniumActionException for unexpected errors
     */
    public boolean isElementDisplayed(By locator) {
        try {
            waitForElementVisible(locator);
            return driver.findElement(locator).isDisplayed();
        } catch (NoSuchElementException e) {
            // Element not found - this is expected behavior, return false
            return false;
        } catch (Exception e) {
            // Unexpected error - fail fast with context
            throw new SeleniumActionException("Error checking visibility of: " + locator, e);
        }
    }

    // Advanced Element Interactions
    public void selectByVisibleText(By locator, String text) {
        try {
            waitForElementVisible(locator);
            WebElement element = driver.findElement(locator);
            org.openqa.selenium.support.ui.Select select = new org.openqa.selenium.support.ui.Select(element);
            select.selectByVisibleText(text);
        } catch (Exception e) {
            throw new SeleniumActionException("Failed to select option '" + text + "' from dropdown: " + locator, e);
        }
    }

    public void selectByValue(By locator, String value) {
        try {
            waitForElementVisible(locator);
            WebElement element = driver.findElement(locator);
            org.openqa.selenium.support.ui.Select select = new org.openqa.selenium.support.ui.Select(element);
            select.selectByValue(value);
        } catch (Exception e) {
            throw new SeleniumActionException("Failed to select option with value '" + value + "' from dropdown: " + locator, e);
        }
    }

    // List Operations
    public List<WebElement> findElements(By locator) {
        try {
            waitForElementVisible(locator);
            return driver.findElements(locator);
        } catch (Exception e) {
            throw new SeleniumActionException("Failed to find elements: " + locator, e);
        }
    }

    public int getElementCount(By locator) {
        try {
            return driver.findElements(locator).size();
        } catch (Exception e) {
            throw new SeleniumActionException("Failed to get element count for: " + locator, e);
        }
    }

    // JavaScript Operations
    public void scrollToElement(By locator) {
        try {
            WebElement element = driver.findElement(locator);
            js.executeScript("arguments[0].scrollIntoView(true);", element);
        } catch (Exception e) {
            throw new SeleniumActionException("Failed to scroll to element: " + locator, e);
        }
    }

    public void clickByJS(By locator) {
        try {
            WebElement element = driver.findElement(locator);
            js.executeScript("arguments[0].click();", element);
        } catch (Exception e) {
            throw new SeleniumActionException("Failed to click element using JavaScript: " + locator, e);
        }
    }

    // Actions Operations
    public void hover(By locator) {
        try {
            waitForElementVisible(locator);
            WebElement element = driver.findElement(locator);
            actions.moveToElement(element).perform();
        } catch (Exception e) {
            throw new SeleniumActionException("Failed to hover over element: " + locator, e);
        }
    }

    public void dragAndDrop(By source, By target) {
        try {
            waitForElementVisible(source);
            waitForElementVisible(target);
            WebElement sourceElement = driver.findElement(source);
            WebElement targetElement = driver.findElement(target);
            actions.dragAndDrop(sourceElement, targetElement).perform();
        } catch (Exception e) {
            throw new SeleniumActionException("Failed to drag element " + source + " to " + target, e);
        }
    }

    // Wait Operations
    public void waitForElementToDisappear(By locator) {
        try {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
        } catch (Exception e) {
            throw new SeleniumActionException("Element did not disappear after waiting: " + locator, e);
        }
    }

    public void waitForTextToBePresent(By locator, String text) {
        try {
            wait.until(ExpectedConditions.textToBePresentInElementLocated(locator, text));
        } catch (Exception e) {
            throw new SeleniumActionException("Text '" + text + "' not found in element after waiting: " + locator, e);
        }
    }

    // Navigation
    public void navigateTo(String url) {
        try {
            driver.get(url);
        } catch (Exception e) {
            throw new SeleniumActionException("Failed to navigate to URL: " + url, e);
        }
    }

    public void refreshPage() {
        try {
            driver.navigate().refresh();
        } catch (Exception e) {
            throw new SeleniumActionException("Failed to refresh page", e);
        }
    }

    public void goBack() {
        try {
            driver.navigate().back();
        } catch (Exception e) {
            throw new SeleniumActionException("Failed to go back", e);
        }
    }

    public void goForward() {
        try {
            driver.navigate().forward();
        } catch (Exception e) {
            throw new SeleniumActionException("Failed to go forward", e);
        }
    }

    // Window Operations
    public void switchToFrame(By locator) {
        try {
            waitForElementVisible(locator);
            WebElement frame = driver.findElement(locator);
            driver.switchTo().frame(frame);
        } catch (Exception e) {
            throw new SeleniumActionException("Failed to switch to frame: " + locator, e);
        }
    }

    public void switchToDefaultContent() {
        try {
            driver.switchTo().defaultContent();
        } catch (Exception e) {
            throw new SeleniumActionException("Failed to switch to default content", e);
        }
    }

    // Alert Operations
    public void acceptAlert() {
        try {
            wait.until(ExpectedConditions.alertIsPresent());
            driver.switchTo().alert().accept();
        } catch (Exception e) {
            throw new SeleniumActionException("Failed to accept alert", e);
        }
    }

    public void dismissAlert() {
        try {
            wait.until(ExpectedConditions.alertIsPresent());
            driver.switchTo().alert().dismiss();
        } catch (Exception e) {
            throw new SeleniumActionException("Failed to dismiss alert", e);
        }
    }

    public String getAlertText() {
        try {
            wait.until(ExpectedConditions.alertIsPresent());
            return driver.switchTo().alert().getText();
        } catch (Exception e) {
            throw new SeleniumActionException("Failed to get alert text", e);
        }
    }
} 