package utils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.interactions.Actions;
import java.time.Duration;
import java.util.List;

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
        wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }
    
    public void waitForElementClickable(By locator) {
        wait.until(ExpectedConditions.elementToBeClickable(locator));
    }
    
    public void click(By locator) {
        waitForElementClickable(locator);
        driver.findElement(locator).click();
    }
    
    public void sendKeys(By locator, String text) {
        waitForElementVisible(locator);
        driver.findElement(locator).sendKeys(text);
    }
    
    public void clearAndSendKeys(By locator, String text) {
        waitForElementVisible(locator);
        WebElement element = driver.findElement(locator);
        element.clear();
        element.sendKeys(text);
    }
    
    public String getText(By locator) {
        waitForElementVisible(locator);
        return driver.findElement(locator).getText();
    }
    
    public boolean isElementDisplayed(By locator) {
        try {
            waitForElementVisible(locator);
            return driver.findElement(locator).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    // Advanced Element Interactions
    public void selectByVisibleText(By locator, String text) {
        waitForElementVisible(locator);
        WebElement element = driver.findElement(locator);
        org.openqa.selenium.support.ui.Select select = new org.openqa.selenium.support.ui.Select(element);
        select.selectByVisibleText(text);
    }

    public void selectByValue(By locator, String value) {
        waitForElementVisible(locator);
        WebElement element = driver.findElement(locator);
        org.openqa.selenium.support.ui.Select select = new org.openqa.selenium.support.ui.Select(element);
        select.selectByValue(value);
    }

    // List Operations
    public List<WebElement> findElements(By locator) {
        waitForElementVisible(locator);
        return driver.findElements(locator);
    }

    public int getElementCount(By locator) {
        return driver.findElements(locator).size();
    }

    // JavaScript Operations
    public void scrollToElement(By locator) {
        WebElement element = driver.findElement(locator);
        js.executeScript("arguments[0].scrollIntoView(true);", element);
    }

    public void clickByJS(By locator) {
        WebElement element = driver.findElement(locator);
        js.executeScript("arguments[0].click();", element);
    }

    // Actions Operations
    public void hover(By locator) {
        waitForElementVisible(locator);
        WebElement element = driver.findElement(locator);
        actions.moveToElement(element).perform();
    }

    public void dragAndDrop(By source, By target) {
        waitForElementVisible(source);
        waitForElementVisible(target);
        WebElement sourceElement = driver.findElement(source);
        WebElement targetElement = driver.findElement(target);
        actions.dragAndDrop(sourceElement, targetElement).perform();
    }

    // Wait Operations
    public void waitForElementToDisappear(By locator) {
        wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    public void waitForTextToBePresent(By locator, String text) {
        wait.until(ExpectedConditions.textToBePresentInElementLocated(locator, text));
    }

    // Navigation
    public void navigateTo(String url) {
        driver.get(url);
    }

    public void refreshPage() {
        driver.navigate().refresh();
    }

    public void goBack() {
        driver.navigate().back();
    }

    public void goForward() {
        driver.navigate().forward();
    }

    // Window Operations
    public void switchToFrame(By locator) {
        waitForElementVisible(locator);
        WebElement frame = driver.findElement(locator);
        driver.switchTo().frame(frame);
    }

    public void switchToDefaultContent() {
        driver.switchTo().defaultContent();
    }

    // Alert Operations
    public void acceptAlert() {
        wait.until(ExpectedConditions.alertIsPresent());
        driver.switchTo().alert().accept();
    }

    public void dismissAlert() {
        wait.until(ExpectedConditions.alertIsPresent());
        driver.switchTo().alert().dismiss();
    }

    public String getAlertText() {
        wait.until(ExpectedConditions.alertIsPresent());
        return driver.switchTo().alert().getText();
    }
} 