package com.krce.pages;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class FormValidationPage {

    private final WebDriver          driver;
    private final WebDriverWait      wait;
    private final JavascriptExecutor js;

    private static final String NOTES_LOGIN_URL = "https://practice.expandtesting.com/notes/app/login";
    private static final String REGISTER_URL    = "https://practice.expandtesting.com/register";
    private static final String LOGIN_URL       = "https://practice.expandtesting.com/login";

    // Login form
    private final By notesEmailInput    = By.id("email");
    private final By notesPasswordInput = By.id("password");
    private final By notesLoginButton   = By.cssSelector("[data-testid='login-submit']");


    private final By addNoteButton = By.xpath(
            "//*[@data-testid='add-note' or @data-testid='create-note']" +
                    " | //a[contains(@href,'/notes/new')]" +
                    " | //button[contains(translate(normalize-space()," +
                    "      'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'add')]" +
                    " | //a[contains(translate(normalize-space()," +
                    "      'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'note')]"
    );

    private final By notesTitleInput   = By.xpath(
            "//input[@id='title' or @data-testid='note-title' or @name='title']"
    );
    private final By notesCreateButton = By.xpath(
            "//button[@data-testid='note-submit' or @type='submit']" +
                    " | //button[contains(translate(normalize-space()," +
                    "      'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'create')]"
    );
    private final By notesTitleError   = By.xpath(
            "//*[@data-testid='note-title-error']" +
                    " | //*[contains(translate(normalize-space()," +
                    "      'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'title')" +
                    "   and contains(translate(normalize-space()," +
                    "      'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'required')]"
    );


    // Register Form


    private final By regUsernameInput = By.xpath(
            "//input[@name='username' or @id='username' or @placeholder='Username']"
    );
    private final By regPasswordInput = By.xpath(
            "(//input[@type='password'])[1]"
    );
    private final By regConfirmInput  = By.xpath(
            "(//input[@type='password'])[2]"
    );
    private final By regSubmitButton  = By.xpath(
            "//button[@type='submit' or normalize-space()='Register']"
    );
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

    //  Login Form

    private final By loginUsernameInput = By.xpath(
            "//input[@name='username' or @id='username' or @type='text']"
    );
    private final By loginPasswordInput = By.xpath(
            "//input[@name='password' or @id='password' or @type='password']"
    );
    private final By loginSubmitButton  = By.xpath(
            "//button[@type='submit' or normalize-space()='Login']"
    );
    private final By loginErrorMessage  = By.xpath(
            "//*[@data-testid='login-error']" +
                    " | //*[contains(@class,'alert') or contains(@class,'error')" +
                    "       or contains(@class,'flash') or contains(@id,'flash')]"
    );


    public FormValidationPage(WebDriver driver) {
        this.driver = driver;
        this.wait   = new WebDriverWait(driver, Duration.ofSeconds(15));
        this.js     = (JavascriptExecutor) driver;
    }

    // Notes App actions

    public void openNotesLogin() {
        driver.get(NOTES_LOGIN_URL);
    }


    public void notesLogin(String email, String password) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(notesEmailInput));
        type(notesEmailInput, email);
        type(notesPasswordInput, password);
        jsClick(notesLoginButton);
        // React Router updates the URL; wait until /login disappears
        wait.until(d -> !d.getCurrentUrl().contains("/login"));
    }


    public void openCreateNote() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(addNoteButton));
        jsClick(addNoteButton);
        wait.until(ExpectedConditions.visibilityOfElementLocated(notesTitleInput));
    }


    public void submitNoteWithEmptyTitle() {
        WebElement title = driver.findElement(notesTitleInput);
        title.clear();
        title.sendKeys(Keys.TAB);   // triggers React's onBlur validation
        jsClick(notesCreateButton);
    }

    public String getNotesTitleErrorText() {
        return getErrorText(notesTitleError);
    }

    //  Register Form actions

    public void openRegister() {
        driver.get(REGISTER_URL);
        wait.until(ExpectedConditions.visibilityOfElementLocated(regUsernameInput));
    }


    public void registerWithShortPassword(String username, String shortPwd) {
        type(regUsernameInput, username);
        type(regPasswordInput, shortPwd);
        type(regConfirmInput, shortPwd);
        jsClick(regSubmitButton);
    }

    public String getRegisterPasswordErrorText() {
        return getErrorText(regPasswordError);
    }

    // Login Form actions

    public void openLogin() {
        driver.get(LOGIN_URL);
        wait.until(ExpectedConditions.visibilityOfElementLocated(loginUsernameInput));
    }


    public void loginWithInvalidUsername(String invalidUsername, String password) {
        type(loginUsernameInput, invalidUsername);
        type(loginPasswordInput, password);
        jsClick(loginSubmitButton);
    }


    public String getLoginErrorText() {
        return getErrorText(loginErrorMessage);
    }

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