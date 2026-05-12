package com.krce.tests;

import com.krce.pages.FormInteractionPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public class FormInteractionBaseTest {
    protected WebDriver driver;
    FormInteractionPage page;

    @BeforeMethod
    public void setup() {
        driver = new ChromeDriver();
        driver.manage().window();


        // ✅ THIS WAS MISSING (MAIN FIX)
        page = new FormInteractionPage(driver);
    }

    @AfterMethod
    public void tearDown() {
        driver.quit();
    }
}
