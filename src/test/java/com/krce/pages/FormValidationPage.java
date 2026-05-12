package com.krce.pages;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class FormValidationPage {

    private final WebDriver          driver;
    private final WebDriverWait      wait;
    private final JavascriptExecutor js;

    // ── URLs ─────────────────────────────────────────────────────────────────
    private static final String NOTES_LOGIN_URL = "https://practice.expandtesting.com/notes/app/login";
    private static final String REGISTER_URL    = "https://practice.expandtesting.com/register";
    private static final String LOGIN_URL       = "https://practice.expandtesting.com/login";

    // ══════════════════════════════════════════════════════════════════════════
    // SCENARIO 1 – Notes App (React SPA)
    // Confirmed locators from Cypress community examples:
    //   #email, #password, [data-testid="login-submit"]
    // ══════════════════════════════════════════════════════════════════════════

    // Login form
    private final By notesEmailInput    = By.id("email");
    private final By notesPasswordInput = By.id("password");
    private final By notesLoginButton   = By.cssSelector("[data-testid='login-submit']");

    // Dashboard → "Add Note" / "+" button that opens the Create-Note form.
    // We click this instead of navigating directly to /notes/new because
    // direct URL access breaks React Router SPA routing.
    private final By addNoteButton = By.xpath(
            "//*[@data-testid='add-note' or @data-testid='create-note']" +
                    " | //a[contains(@href,'/notes/new')]" +
                    " | //button[contains(translate(normalize-space()," +
                    "      'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'add')]" +
                    " | //a[contains(translate(normalize-space()," +
                    "      'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'note')]"
    );

    // Create-Note form – title input
    private final By notesTitleInput   = By.xpath(
            "//input[@id='title' or @data-testid='note-title' or @name='title']"
    );
    // Create-Note form – submit / "Create" button
    private final By notesCreateButton = By.xpath(
            "//button[@data-testid='note-submit' or @type='submit']" +
                    " | //button[contains(translate(normalize-space()," +
                    "      'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'create')]"
    );
    // Validation error for empty title
    private final By notesTitleError   = By.xpath(
            "//*[@data-testid='note-title-error']" +
                    " | //*[contains(translate(normalize-space()," +
                    "      'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'title')" +
                    "   and contains(translate(normalize-space()," +
                    "      'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'required')]"
    );

    // ══════════════════════════════════════════════════════════════════════════
    // SCENARIO 2 – Register Form
    // ══════════════════════════════════════════════════════════════════════════

    private final By regUsernameInput = By.xpath(
            "//input[@name='username' or @id='username' or @placeholder='Username']"
    );
    // First password field
    private final By regPasswordInput = By.xpath(
            "(//input[@type='password'])[1]"
    );
    // Confirm password field (second password input)
    private final By regConfirmInput  = By.xpath(
            "(//input[@type='password'])[2]"
    );
    private final By regSubmitButton  = By.xpath(
            "//button[@type='submit' or normalize-space()='Register']"
    );
    // "Password must be at least X characters" or similar
    private final By regPasswordError = By.xpath(
            "//*[@data-testid='register-password-error']" +
                    " | //*[contains(translate(normalize-space()," +
                    "      'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'password')" +
                    "   and (contains(translate(normalize-space()," +
                    "        'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'least')" +
                    "    or  contains(translate(normalize-space()," +
                    "        'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'minimum')" +
                    "    or  contains(translate(normalize-space()," +
                    "        'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'short')" +
                    "    or  contains(translate(normalize-space()," +
                    "        'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'character'))]"
    );

    // ══════════════════════════════════════════════════════════════════════════
    // SCENARIO 3 – Login Form
    // IMPORTANT: This page uses type="text" for the username field (not email),
    // so HTML5 email validation does NOT trigger. Submitting "test123" causes
    // the server to respond with "Your username is invalid!" after the redirect.
    // ══════════════════════════════════════════════════════════════════════════

    private final By loginUsernameInput = By.xpath(
            "//input[@name='username' or @id='username' or @type='text']"
    );
    private final By loginPasswordInput = By.xpath(
            "//input[@name='password' or @id='password' or @type='password']"
    );
    private final By loginSubmitButton  = By.xpath(
            "//button[@type='submit' or normalize-space()='Login']"
    );
    // Server returns "Your username is invalid!" or "Invalid username." flash message
    private final By loginErrorMessage  = By.xpath(
            "//*[@data-testid='login-error']" +
                    " | //*[contains(@class,'alert') or contains(@class,'error')" +
                    "       or contains(@class,'flash') or contains(@id,'flash')]"
    );

    // ── Constructor ───────────────────────────────────────────────────────────

    public FormValidationPage(WebDriver driver) {
        this.driver = driver;
        this.wait   = new WebDriverWait(driver, Duration.ofSeconds(15));
        this.js     = (JavascriptExecutor) driver;
    }

    // ══════════════════════════════════════════════════════════════════════════
    // SCENARIO 1 – Notes App actions
    // ══════════════════════════════════════════════════════════════════════════

    /** Opens the Notes App login page. */
    public void openNotesLogin() {
        driver.get(NOTES_LOGIN_URL);
    }

    /**
     * Logs into the Notes React SPA using the confirmed locators
     * (#email, #password, [data-testid="login-submit"]) and waits for the dashboard.
     */
    public void notesLogin(String email, String password) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(notesEmailInput));
        type(notesEmailInput, email);
        type(notesPasswordInput, password);
        jsClick(notesLoginButton);
        // React Router updates the URL; wait until /login disappears
        wait.until(d -> !d.getCurrentUrl().contains("/login"));
    }

    /**
     * Opens the Create Note form by clicking the dashboard's "Add Note" button.
     * Does NOT navigate directly to /notes/new (that breaks SPA routing).
     */
    public void openCreateNote() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(addNoteButton));
        jsClick(addNoteButton);
        wait.until(ExpectedConditions.visibilityOfElementLocated(notesTitleInput));
    }

    /**
     * Clears the Title field, tabs away to fire blur validation, then clicks Create.
     */
    public void submitNoteWithEmptyTitle() {
        WebElement title = driver.findElement(notesTitleInput);
        title.clear();
        title.sendKeys(Keys.TAB);   // triggers React's onBlur validation
        jsClick(notesCreateButton);
    }

    /** Returns the title validation error text, or "" if none appears. */
    public String getNotesTitleErrorText() {
        return getErrorText(notesTitleError);
    }

    // ══════════════════════════════════════════════════════════════════════════
    // SCENARIO 2 – Register Form actions
    // ══════════════════════════════════════════════════════════════════════════

    /** Opens the Register page. */
    public void openRegister() {
        driver.get(REGISTER_URL);
        wait.until(ExpectedConditions.visibilityOfElementLocated(regUsernameInput));
    }

    /**
     * Fills the registration form with a valid username and a below-minimum password,
     * then submits to trigger password-length validation.
     *
     * @param username  any non-empty username string
     * @param shortPwd  a password shorter than the minimum required (e.g. "123")
     */
    public void registerWithShortPassword(String username, String shortPwd) {
        type(regUsernameInput, username);
        type(regPasswordInput, shortPwd);
        type(regConfirmInput, shortPwd);
        jsClick(regSubmitButton);
    }

    /** Returns the password validation error text, or "" if none appears. */
    public String getRegisterPasswordErrorText() {
        return getErrorText(regPasswordError);
    }

    // ══════════════════════════════════════════════════════════════════════════
    // SCENARIO 3 – Login Form actions
    // ══════════════════════════════════════════════════════════════════════════

    /** Opens the standard Login page. */
    public void openLogin() {
        driver.get(LOGIN_URL);
        wait.until(ExpectedConditions.visibilityOfElementLocated(loginUsernameInput));
    }

    /**
     * Enters an invalid username (e.g. "test123") and any password, then submits.
     * The server will respond with an "invalid username" flash/error message.
     */
    public void loginWithInvalidUsername(String invalidUsername, String password) {
        type(loginUsernameInput, invalidUsername);
        type(loginPasswordInput, password);
        jsClick(loginSubmitButton);
    }

    /**
     * Waits for the server-side error/flash message and returns its text,
     * or "" if nothing appears within the timeout.
     */
    public String getLoginErrorText() {
        return getErrorText(loginErrorMessage);
    }

    // ══════════════════════════════════════════════════════════════════════════
    // Private helpers
    // ══════════════════════════════════════════════════════════════════════════

    private void type(By locator, String text) {
        WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        el.clear();
        el.sendKeys(text);
    }

    private void jsClick(By locator) {
        WebElement el = wait.until(ExpectedConditions.elementToBeClickable(locator));
        js.executeScript("arguments[0].scrollIntoView({block:'center'});", el);
        try {
            el.click();
        } catch (ElementClickInterceptedException e) {
            js.executeScript("arguments[0].click();", el);
        }
    }

    private String getErrorText(By locator) {
        try {
            WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            return el.getText().trim();
        } catch (TimeoutException e) {
            return "";
        }
    }
}