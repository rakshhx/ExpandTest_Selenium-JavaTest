package com.krce.tests;
import com.krce.pages.FormInteractionPage;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.Test;

public class FormInteractionTest extends FormInteractionBaseTest {

    @Test

    public void validateInputFields() {
        driver.get("https://practice.expandtesting.com/inputs");
        page.enterNumber("12345");
        Assert.assertEquals(page.getNumber(), "12345");

        page.enterText("Hello Selenium");
        Assert.assertEquals(page.getText(), "Hello Selenium");

        page.enterPassword("Pass@123");
        Assert.assertEquals(page.getPassword(), "Pass@123");

        page.enterDate("2026-05-11");
        Assert.assertEquals(page.getDate(), "2026-05-11");
    }


    // DROPDOWN TEST

    @Test
    public void validateDropdowns() {

        driver.get("https://practice.expandtesting.com/dropdown");

        page.selectSimple("Option 1");
        Assert.assertEquals(page.getSimpleSelected(), "Option 1");

        page.selectPageSize("20");
        Assert.assertEquals(page.getPageSizeSelected(), "20");

        page.selectCountry("India");
        Assert.assertEquals(page.getCountrySelected(), "India");
    }

    @Test
    public void validateCheckboxFlow() {
        driver.get(" https://practice.expandtesting.com/checkboxes");
        FormInteractionPage formPage = new FormInteractionPage(driver);
        Assert.assertFalse(formPage.isCheckbox1Selected(), "Checkbox 1 should be unselected by default");
        Assert.assertTrue(formPage.isCheckbox2Selected(), "Checkbox 2 should be selected by default");

        formPage.toggleCheckbox1();

        formPage.toggleCheckbox2();

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
