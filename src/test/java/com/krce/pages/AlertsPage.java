package com.krce.pages;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class AlertsPage {

    private final WebDriver       driver;
    private final WebDriverWait   wait;
    private final JavascriptExecutor js;

    private static final String URL = "https://practice.expandtesting.com/js-dialogs";

    // Locators – buttons
    private final By jsAlertButton   = By.xpath("//button[normalize-space()='Js Alert']");
    private final By jsConfirmButton = By.xpath("//button[normalize-space()='Js Confirm']");
    private final By jsPromptButton  = By.xpath("//button[normalize-space()='Js Prompt']");


    private final By[] responseLocators = {
            By.xpath("//*[contains(@id,'result')]"),
            By.xpath("//*[contains(@id,'response')]"),
            By.xpath("//*[contains(text(),'Dialog Response')]/following-sibling::*[1]"),
            By.xpath("//*[normalize-space(text())='Waiting']"),
            By.xpath("//p[contains(@class,'result') or contains(@class,'response')]")
    };

    public AlertsPage(WebDriver driver) {
        this.driver = driver;
        this.wait   = new WebDriverWait(driver, Duration.ofSeconds(10));
        this.js     = (JavascriptExecutor) driver;
    }

    public void open() {
        driver.get(URL);
    }

    public void triggerAndAcceptAlert() {
        clickButton(jsAlertButton);
        Alert alert = waitForAlert();
        alert.accept();
    }

    public void triggerAndDismissConfirm() {
        clickButton(jsConfirmButton);
        Alert alert = waitForAlert();
        alert.dismiss();
    }

    public void triggerPromptEnterTextAndAccept(String text) {
        clickButton(jsPromptButton);
        Alert alert = waitForAlert();
        alert.sendKeys(text);
        alert.accept();
    }

    public String getDialogResponseText() {
        WebElement resultEl = resolveResultElement();

        wait.until(driver -> {
            String t = ((JavascriptExecutor) driver).executeScript("return arguments[0].innerText;", resultEl).toString().trim();
            return !t.equalsIgnoreCase("Waiting") && !t.isEmpty();
        });

        return js.executeScript("return arguments[0].innerText;", resultEl)
                .toString().trim();
    }

    private WebElement resolveResultElement() {
        WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(3));

        for (By locator : responseLocators) {
            try {
                return shortWait.until(ExpectedConditions.presenceOfElementLocated(locator));
            } catch (org.openqa.selenium.TimeoutException ignored) {

            }
        }

        Object el = js.executeScript(
                "var all = document.querySelectorAll('*');" +
                        "for (var i = 0; i < all.length; i++) {" +
                        "  if (all[i].children.length === 0 && " +
                        "      all[i].innerText && all[i].innerText.trim() === 'Waiting') {" +
                        "    return all[i];" +
                        "  }" +
                        "} return null;");

        if (el instanceof WebElement) {
            return (WebElement) el;
        }

        throw new org.openqa.selenium.TimeoutException(
                "Could not locate the dialog-response result element with any known strategy.");
    }

    private void clickButton(By locator) {
        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(locator));

        js.executeScript("arguments[0].scrollIntoView({block:'center', inline:'center'});", button);

        try {
            button.click();
        } catch (org.openqa.selenium.ElementClickInterceptedException e) {
            js.executeScript("arguments[0].click();", button);
        }
    }

    private Alert waitForAlert() {
        return wait.until(ExpectedConditions.alertIsPresent());
    }
}