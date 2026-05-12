package com.krce.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class NotesLoginPage {

    WebDriver driver;
    WebDriverWait wait;

    private final By email = By.id("email");
    private final By password = By.id("password");
    private final By loginBtn = By.xpath("//button[@type='submit']");

    // 🔥 PUT YOUR CREDENTIALS HERE
    private final String USER_EMAIL = "rakshi14102004@gmail.com";
    private final String USER_PASSWORD = "123456789";

    public NotesLoginPage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    public void login() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(email)).sendKeys(USER_EMAIL);
        driver.findElement(password).sendKeys(USER_PASSWORD);
        driver.findElement(loginBtn).click();
    }
}