package com.krce.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LoginPage extends BasePage{

    public LoginPage(WebDriver driver, WebDriverWait wait){
        super(driver, wait);
    }
    private final By usernameField = By.id("username");
    private final By passwordField = By.id("password");
    private final By loginButton = By.cssSelector(("button[type='submit']"));
    private final By flashMessage = By.id("flash");

    public void enterUsername(String username){
        driver.findElement(usernameField).sendKeys(username);
    }

    public void enterPassword(String password){
        driver.findElement(passwordField).sendKeys(password);
    }

    public void clickOnSubmitButton(){
            WebElement button = wait.until(
                    ExpectedConditions.elementToBeClickable(loginButton)
            );

            ((JavascriptExecutor) driver)
                    .executeScript("arguments[0].click();", button);
        }

    public String getFlashMessage(){
        WebElement flash = wait.until(ExpectedConditions.visibilityOfElementLocated(flashMessage));
        return flash.getText();
    }

    public String login(String username, String password){
        enterUsername(username);
        enterPassword(password);
        clickOnSubmitButton();
        return getFlashMessage();
    }
}
