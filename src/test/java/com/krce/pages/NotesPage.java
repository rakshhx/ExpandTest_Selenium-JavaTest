package com.krce.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class NotesPage {

    WebDriver driver;
    WebDriverWait wait;

    public NotesPage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    // Locators (IMPORTANT: DO NOT CACHE ELEMENTS)
    private final By createBtn = By.cssSelector("[data-testid='note-create']");
    private final By titleInput = By.id("note-title");
    private final By descInput = By.id("note-description");
    private final By saveBtn = By.cssSelector("[data-testid='note-submit']");

    // ---------- CREATE NOTE ----------
    public void createNote(String category, boolean flag, String title, String desc) {

        wait.until(ExpectedConditions.elementToBeClickable(createBtn)).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(titleInput)).sendKeys(title);
        driver.findElement(descInput).sendKeys(desc);

        driver.findElement(saveBtn).click();

        // wait for UI refresh
        wait.until(ExpectedConditions.invisibilityOfElementLocated(saveBtn));
    }

    // ---------- EDIT NOTE (FIXED STALE ELEMENT ISSUE) ----------
    public void editNote(String oldTitle, String newTitle, String newDesc) {

        By noteCard = By.xpath("//div[contains(@class,'card-header') and normalize-space()='" + oldTitle + "']");
        By editBtn = By.xpath("//div[contains(@class,'card-header') and normalize-space()='" + oldTitle + "']/following::button[contains(.,'Edit')]");

        wait.until(ExpectedConditions.visibilityOfElementLocated(noteCard));

        wait.until(ExpectedConditions.refreshed(
                ExpectedConditions.elementToBeClickable(editBtn)
        )).click();

        WebElement title = wait.until(ExpectedConditions.visibilityOfElementLocated(titleInput));
        title.clear();
        title.sendKeys(newTitle);

        WebElement desc = driver.findElement(descInput);
        desc.clear();
        desc.sendKeys(newDesc);

        driver.findElement(saveBtn).click();

        // wait for update
        wait.until(ExpectedConditions.textToBePresentInElementLocated(noteCard, newTitle));
    }

    // ---------- VERIFY ----------
    public boolean isNoteDisplayed(String title) {

        By note = By.xpath("//div[contains(@class,'card-header') and normalize-space()='" + title + "']");

        return !driver.findElements(note).isEmpty();
    }
}