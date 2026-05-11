package com.krce.tests;
import com.krce.pages.BasePage;
import com.krce.pages.LoginPage;
import com.krce.tests.BaseTest;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.FileReader;
import java.io.IOException;

public class LoginTest extends BaseTest {
    @DataProvider(name = "loginData")
    public Object[][] loginData() {
        return new Object[][]{
                {"practice", "SuperSecretPassword!", "You logged into a secure area!"},
                {"wrongUser", "SuperSecretPassword!", "Your password is invalid!"},
                {"wrong", "wrong", "Your password is invalid!"},
                {"","","Your username is invalid!"}
        };
    }

    @Test(dataProvider = "loginData")
    public void testLogin(String username, String password,String ExpectedMessage){
        BasePage BasePage = new BasePage(driver,wait);
        LoginPage LoginPage = new LoginPage(driver,wait);
        BasePage.navigateTo("https://practice.expandtesting.com/login");
        driver.navigate().refresh();
        String message = LoginPage.login(username,password);
        Assert.assertTrue(message.contains(ExpectedMessage));
    }

}