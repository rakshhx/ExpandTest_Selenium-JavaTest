package com.krce.pages;

import com.krce.utils.ConfigReader;
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


    public NotesLoginPage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    public void login() {

        wait.until(ExpectedConditions.visibilityOfElementLocated(email))
                .sendKeys(ConfigReader.get("notesEmail"));

        driver.findElement(password)
                .sendKeys(ConfigReader.get("notesPassword"));

        driver.findElement(loginBtn).click();
    }
}