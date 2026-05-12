package com.krce.tests;
import com.krce.pages.FormInteractionPage;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.Test;

public class FormInteractionTest extends FormInteractionBaseTest {

    // ================= TEST =================
    @Test

    public void validateInputFields() {
        driver.get("https://practice.expandtesting.com/inputs");
        // NUMBER
        page.enterNumber("12345");
        Assert.assertEquals(page.getNumber(), "12345");

        // TEXT
        page.enterText("Hello Selenium");
        Assert.assertEquals(page.getText(), "Hello Selenium");

        // PASSWORD
        page.enterPassword("Pass@123");
        Assert.assertEquals(page.getPassword(), "Pass@123");

        // DATE (IMPORTANT FIX)
        page.enterDate("2026-05-11");
        Assert.assertEquals(page.getDate(), "2026-05-11");
    }


    // ================= DROPDOWN TEST =================

    @Test
    public void validateDropdowns() {

        driver.get("https://practice.expandtesting.com/dropdown");

        // 1. Simple dropdown
        page.selectSimple("Option 1");
        Assert.assertEquals(page.getSimpleSelected(), "Option 1");

        // 2. Elements per page dropdown
        page.selectPageSize("20");
        Assert.assertEquals(page.getPageSizeSelected(), "20");

        // 3. Country dropdown (ONLY if visible on page)
        // Example values depend on site (e.g., "India", "United States")
        page.selectCountry("India");
        Assert.assertEquals(page.getCountrySelected(), "India");
    }

    @Test
    public void validateCheckboxFlow() {
        driver.get(" https://practice.expandtesting.com/checkboxes");
        FormInteractionPage formPage = new FormInteractionPage(driver);
        // STEP 1: VERIFY DEFAULT STATE
        Assert.assertFalse(formPage.isCheckbox1Selected(), "Checkbox 1 should be unselected by default");
        Assert.assertTrue(formPage.isCheckbox2Selected(), "Checkbox 2 should be selected by default");

        // STEP 2: CLICK checkbox 1 (turn ON)
        formPage.toggleCheckbox1();

        // STEP 3: CLICK checkbox 2 (turn OFF)
        formPage.toggleCheckbox2();

        // STEP 4: VERIFY AFTER ACTION
        Assert.assertTrue(formPage.isCheckbox1Selected(), "Checkbox 1 should be selected after click");
        Assert.assertFalse(formPage.isCheckbox2Selected(), "Checkbox 2 should be unselected after click");
    }

    @Test
    public void validateRadioButtons() {
        driver.get(" https://practice.expandtesting.com/radio-buttons");


        page.selectColor("blue");
        page.selectColor("red");

        Assert.assertTrue(page.isOnlyOneColorSelected());

        page.selectSport("football");
        Assert.assertTrue(page.isOnlyOneSportSelected());
    }
}
