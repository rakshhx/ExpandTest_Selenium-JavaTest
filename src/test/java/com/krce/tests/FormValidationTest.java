package com.krce.tests;
import com.krce.pages.FormValidationPage;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FormValidationTest {

    private static WebDriver           driver;
    private static FormValidationPage page;

    // Notes App credentials
    private static final String NOTES_EMAIL    = "rakshi14102004@gmail.com";
    private static final String NOTES_PASSWORD = "123456789";

    // ── Setup / Teardown ──────────────────────────────────────────────────────

    @BeforeAll
    static void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        // options.addArguments("--headless=new"); // uncomment for CI

        driver = new ChromeDriver(options);
        page   = new FormValidationPage(driver);
    }

    @AfterAll
    static void tearDown() {
        if (driver != null) driver.quit();
    }

    // ══════════════════════════════════════════════════════════════════════════
    // TEST 1 – Notes App : empty title shows "Title is required" (or similar)
    // ══════════════════════════════════════════════════════════════════════════

    @Test
    @Order(1)
    @DisplayName("Notes App – submitting Create Note with empty title shows title-required error")
    void testCreateNoteEmptyTitleValidation() {
        // Step 1: Log in
        page.openNotesLogin();
        page.notesLogin(NOTES_EMAIL, NOTES_PASSWORD);

        // Step 2: Open Create Note form via the dashboard button (not direct URL)
        page.openCreateNote();

        // Step 3: Submit with empty title
        page.submitNoteWithEmptyTitle();

        // Step 4: Verify validation error
        String errorText = page.getNotesTitleErrorText();

        assertFalse(
                errorText.isEmpty(),
                "Expected a title-required validation error, but no error text was found."
        );
        assertTrue(
                errorText.toLowerCase().contains("title") || errorText.toLowerCase().contains("required"),
                "Expected error to mention 'title' or 'required', but was: " + errorText
        );
    }

    // ══════════════════════════════════════════════════════════════════════════
    // TEST 2 – Register : short password shows password-length error
    // ══════════════════════════════════════════════════════════════════════════

    @Test
    @Order(2)
    @DisplayName("Register – submitting with a short password shows password-length error")
    void testRegisterShortPasswordValidation() {
        // Step 1: Open Register page and submit with a 3-char password
        page.openRegister();
        page.registerWithShortPassword("testuser_automation", "123");

        // Step 2: Verify the password validation error
        String errorText = page.getRegisterPasswordErrorText();

        assertFalse(
                errorText.isEmpty(),
                "Expected a password-length validation error, but no error text was found."
        );
        assertTrue(
                errorText.toLowerCase().contains("password")
                        && (errorText.toLowerCase().contains("least")
                        || errorText.toLowerCase().contains("minimum")
                        || errorText.toLowerCase().contains("short")
                        || errorText.toLowerCase().contains("character")),
                "Expected error to mention password minimum length, but was: " + errorText
        );
    }

    // ══════════════════════════════════════════════════════════════════════════
    // TEST 3 – Login Form : invalid username → server returns error message
    //
    // NOTE: practice.expandtesting.com/login uses a plain type="text" username
    // field (not type="email"), so browser-level email validation does NOT fire.
    // Entering "test123" is simply an invalid/non-existent username, and the
    // server responds with "Your username is invalid!" after submission.
    // ══════════════════════════════════════════════════════════════════════════

    @Test
    @Order(3)
    @DisplayName("Login – invalid username triggers server-side error message")
    void testLoginInvalidUsernameValidation() {
        // Step 1: Open Login page and submit with a non-existent username
        page.openLogin();
        page.loginWithInvalidUsername("test123", "anypassword");

        // Step 2: Verify the server returns an error message
        String errorText = page.getLoginErrorText();

        assertFalse(
                errorText.isEmpty(),
                "Expected a server-side error message after invalid username, but none was found."
        );
        assertTrue(
                errorText.toLowerCase().contains("invalid")
                        || errorText.toLowerCase().contains("username")
                        || errorText.toLowerCase().contains("incorrect")
                        || errorText.toLowerCase().contains("credential"),
                "Expected error to mention 'invalid', 'username', or similar, but was: " + errorText
        );
    }
}