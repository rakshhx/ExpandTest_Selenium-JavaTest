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

    /**
     * Ordered list of XPath strategies for the result element.
     * The page shows "Dialog Response: <result>" and the response value sits in a
     * sibling/child element whose exact ID can vary across deployments.
     * We try the most specific selectors first and fall back to broader ones.
     */
    private final By[] responseLocators = {
            // 1. Any element whose id contains "result" (covers "result", "dialog-result", etc.)
            By.xpath("//*[contains(@id,'result')]"),
            // 2. Any element whose id contains "response" (covers "response", "dialog-response", etc.)
            By.xpath("//*[contains(@id,'response')]"),
            // 3. Sibling/child element that follows the "Dialog Response:" label text
            By.xpath("//*[contains(text(),'Dialog Response')]/following-sibling::*[1]"),
            // 4. The paragraph/div/span that directly contains "Waiting" on load
            By.xpath("//*[normalize-space(text())='Waiting']"),
            // 5. Broadest fallback: any element that once held "Waiting" text (matched by position)
            By.xpath("//p[contains(@class,'result') or contains(@class,'response')]")
    };

    public AlertsPage(WebDriver driver) {
        this.driver = driver;
        this.wait   = new WebDriverWait(driver, Duration.ofSeconds(10));
        this.js     = (JavascriptExecutor) driver;
    }

    // ── Navigation ──────────────────────────────────────────────────────────

    public void open() {
        driver.get(URL);
    }

    // ── JS Alert ────────────────────────────────────────────────────────────

    /** Clicks the JS Alert button and accepts (OK) the alert. */
    public void triggerAndAcceptAlert() {
        clickButton(jsAlertButton);
        Alert alert = waitForAlert();
        alert.accept();
    }

    // ── JS Confirm ──────────────────────────────────────────────────────────

    /** Clicks the JS Confirm button and dismisses (Cancel) the dialog. */
    public void triggerAndDismissConfirm() {
        clickButton(jsConfirmButton);
        Alert alert = waitForAlert();
        alert.dismiss();
    }

    // ── JS Prompt ───────────────────────────────────────────────────────────

    /**
     * Clicks the JS Prompt button, types the supplied text into the prompt,
     * then accepts (OK) it.
     *
     * @param text the string to enter in the prompt input field
     */
    public void triggerPromptEnterTextAndAccept(String text) {
        clickButton(jsPromptButton);
        Alert alert = waitForAlert();
        alert.sendKeys(text);
        alert.accept();
    }

    // ── Result reading ───────────────────────────────────────────────────────

    /**
     * Finds the "Dialog Response" result element using multiple XPath fallbacks
     * (the page's element ID is not stable across deployments).
     * Waits up to 10 s for the text to change from the initial "Waiting" state,
     * then returns the final value using JavaScript innerText to avoid stale reads.
     */
    public String getDialogResponseText() {
        WebElement resultEl = resolveResultElement();

        // Wait until the text stops being "Waiting" / empty
        wait.until(driver -> {
            String t = ((JavascriptExecutor) driver)
                    .executeScript("return arguments[0].innerText;", resultEl)
                    .toString().trim();
            return !t.equalsIgnoreCase("Waiting") && !t.isEmpty();
        });

        return js.executeScript("return arguments[0].innerText;", resultEl)
                .toString().trim();
    }

    /**
     * Tries each locator in {@code responseLocators} in order and returns the
     * first one that resolves to a visible element within 3 seconds.
     * Also accepts an element that is present but not yet visible (e.g. hidden
     * until a dialog is handled).
     *
     * @throws org.openqa.selenium.TimeoutException if none of the locators match
     */
    private WebElement resolveResultElement() {
        WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(3));

        for (By locator : responseLocators) {
            try {
                return shortWait.until(ExpectedConditions.presenceOfElementLocated(locator));
            } catch (org.openqa.selenium.TimeoutException ignored) {
                // try the next locator
            }
        }

        // Last-resort: use JavaScript to find any element with "Waiting" text
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

    // ── Private helpers ──────────────────────────────────────────────────────

    /**
     * Scrolls the target button into the centre of the viewport, then tries a
     * normal Selenium click.  If that is intercepted by an overlapping element
     * (e.g. a cookie banner or sticky ad), we fall back to a JavaScript click
     * which bypasses the overlay entirely.
     */
    private void clickButton(By locator) {
        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(locator));

        // Scroll so the button is centred in the viewport (avoids sticky headers/footers)
        js.executeScript("arguments[0].scrollIntoView({block:'center', inline:'center'});", button);

        try {
            button.click();
        } catch (org.openqa.selenium.ElementClickInterceptedException e) {
            // Overlay is blocking the normal click — use JS as a reliable fallback
            js.executeScript("arguments[0].click();", button);
        }
    }

    private Alert waitForAlert() {
        return wait.until(ExpectedConditions.alertIsPresent());
    }
}