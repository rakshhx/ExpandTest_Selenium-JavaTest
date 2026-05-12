package com.krce.tests;
import com.krce.pages.AlertsPage;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AlertsTest {

    private static WebDriver driver;
    private static AlertsPage alertsPage;

    // ── Setup / Teardown ─────────────────────────────────────────────────────

    @BeforeAll
    static void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        // options.addArguments("--headless=new"); // uncomment for CI

        driver     = new ChromeDriver(options);
        alertsPage = new AlertsPage(driver);
        alertsPage.open();
    }

    @AfterAll
    static void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    // ── Test 1 – JS Alert: accept → page shows "OK" ──────────────────────────

    /**
     * Accepting a JS Alert causes the page to display "OK" in the result area.
     * The site uses the browser's native return value from window.alert(),
     * which resolves to "OK" when the user clicks OK.
     */
    @Test
    @Order(1)
    @DisplayName("JS Alert – accept and verify result shows 'OK'")
    void testJsAlertAccept() {
        alertsPage.triggerAndAcceptAlert();

        String result = alertsPage.getDialogResponseText();

        assertEquals("OK", result,
                "Expected result to be 'OK' after accepting the JS Alert, but was: " + result);
    }

    // ── Test 2 – JS Confirm: dismiss → page shows "Cancel" / "false" ─────────

    /**
     * Dismissing a JS Confirm causes the page to display either "Cancel" or "false"
     * depending on how the site renders the dismissed state.
     * Both values mean the same thing: the user clicked Cancel.
     */
    @Test
    @Order(2)
    @DisplayName("JS Confirm – dismiss and verify result shows dismissed state")
    void testJsConfirmDismiss() {
        alertsPage.triggerAndDismissConfirm();

        String result = alertsPage.getDialogResponseText();

        assertTrue(
                result.equalsIgnoreCase("Cancel") || result.equalsIgnoreCase("false"),
                "Expected result to be 'Cancel' or 'false' after dismissing JS Confirm, but was: " + result
        );
    }

    // ── Test 3 – JS Prompt: enter text, accept → text appears in result ───────

    /**
     * Entering text in a JS Prompt and accepting it causes the page to display
     * the entered text in the result area (the site echoes back whatever was typed).
     */
    @Test
    @Order(3)
    @DisplayName("JS Prompt – enter custom text, accept, verify it appears in result")
    void testJsPromptEnterTextAndAccept() {
        final String promptInput = "Hello ExpandTesting";

        alertsPage.triggerPromptEnterTextAndAccept(promptInput);

        String result = alertsPage.getDialogResponseText();

        assertTrue(
                result.contains(promptInput),
                "Expected result to contain '" + promptInput + "', but was: " + result
        );
    }
}