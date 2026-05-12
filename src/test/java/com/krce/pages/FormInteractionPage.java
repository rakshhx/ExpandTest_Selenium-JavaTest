package com.krce.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class FormInteractionPage {

    WebDriver driver;
    WebDriverWait wait;


    public FormInteractionPage(WebDriver driver) {
        this.driver = driver;
    }

    // Locators
    By numberInput = By.id("input-number");
    By textInput = By.id("input-text");
    By passwordInput = By.id("input-password");
    By dateInput = By.id("input-date");

    // Actions
    public void enterNumber(String value) {
        WebElement el = driver.findElement(numberInput);
        el.clear();
        el.sendKeys(value);
    }

    public void enterText(String value) {
        WebElement el = driver.findElement(textInput);
        el.clear();
        el.sendKeys(value);
    }

    public void enterPassword(String value) {
        WebElement el = driver.findElement(passwordInput);
        el.clear();
        el.sendKeys(value);
    }

    public void enterDate(String value) {
        WebElement el = driver.findElement(dateInput);

        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].value='" + value + "'", el);
    }

    // Getters
    public String getNumber() {
        return driver.findElement(numberInput).getAttribute("value");
    }

    public String getText() {
        return driver.findElement(textInput).getAttribute("value");
    }

    public String getPassword() {
        return driver.findElement(passwordInput).getAttribute("value");
    }

    public String getDate() {
        return driver.findElement(dateInput).getAttribute("value");
    }




    By simpleDropdown = By.id("dropdown");
    By pageSizeDropdown = By.id("elementsPerPageSelect");
    By countryDropdown = By.id("country"); // may exist on same page or another section

    // DROPDOWN

    public void selectSimple(String option) {
        Select select = new Select(driver.findElement(simpleDropdown));
        select.selectByVisibleText(option);
    }

    public String getSimpleSelected() {
        return new Select(driver.findElement(simpleDropdown))
                .getFirstSelectedOption()
                .getText();
    }

    public void selectPageSize(String value) {
        Select select = new Select(driver.findElement(pageSizeDropdown));
        select.selectByVisibleText(value);
    }

    public String getPageSizeSelected() {
        return new Select(driver.findElement(pageSizeDropdown))
                .getFirstSelectedOption()
                .getText();
    }

    public void selectCountry(String country) {
        Select select = new Select(driver.findElement(countryDropdown));
        select.selectByVisibleText(country);
    }

    public String getCountrySelected() {
        return new Select(driver.findElement(countryDropdown))
                .getFirstSelectedOption()
                .getText();
    }

    private void scroll(WebElement el) {
        ((JavascriptExecutor) driver)
                .executeScript("arguments[0].scrollIntoView(true);", el);
    }

    // CHECKBOX LOCATORS
    By checkbox1 = By.id("checkbox1");
    By checkbox2 = By.id("checkbox2");

    // SAFE CLICK METHOD
    public void clickSafe(By locator) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        WebElement el = wait.until(
                ExpectedConditions.presenceOfElementLocated(locator)
        );

        ((JavascriptExecutor) driver)
                .executeScript("arguments[0].scrollIntoView(true);", el);

        try {
            wait.until(ExpectedConditions.elementToBeClickable(el)).click();
        } catch (Exception e) {
            ((JavascriptExecutor) driver)
                    .executeScript("arguments[0].click();", el);
        }
    }

    // ACTIONS
    public void toggleCheckbox1() {
        clickSafe(checkbox1);
    }

    public void toggleCheckbox2() {
        clickSafe(checkbox2);
    }

    // VALIDATION
    public boolean isCheckbox1Selected() {
        return driver.findElement(checkbox1).isSelected();
    }

    public boolean isCheckbox2Selected() {
        return driver.findElement(checkbox2).isSelected();
    }

    public void selectColor(String value) {
        List<WebElement> colors = driver.findElements(By.name("color"));

        for (WebElement el : colors) {
            if (el.getAttribute("value").equalsIgnoreCase(value)) {
                scroll(el);
                el.click();
                break;
            }
        }
    }
    public void selectSport(String value) {
        List<WebElement> sports = driver.findElements(By.name("sport"));

        for (WebElement el : sports) {
            if (el.getAttribute("value").equalsIgnoreCase(value)) {
                scroll(el);
                el.click();
                break;
            }
        }
    }
    public boolean isOnlyOneColorSelected() {
        List<WebElement> colors = driver.findElements(By.name("color"));

        int count = 0;
        for (WebElement el : colors) {
            if (el.isSelected()) count++;
        }
        return count == 1;
    }
    public boolean isOnlyOneSportSelected() {
        List<WebElement> sports = driver.findElements(By.name("sport"));

        int count = 0;
        for (WebElement el : sports) {
            if (el.isSelected()) count++;
        }
        return count == 1;
    }
}

