package com.krce.tests;

import com.krce.tests.NotesBaseTest;
import com.krce.pages.NotesLoginPage;
import com.krce.pages.NotesLoginPage;
import com.krce.pages.NotesPage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class NotesTest extends NotesBaseTest {

    @Test
    public void testCreateAndEditNote() {

        NotesLoginPage login = new NotesLoginPage(driver, wait);
        login.login();

        NotesPage notes = new NotesPage(driver, wait);

        notes.createNote("Work", true, "Old Note", "Old desc");

        Assert.assertTrue(notes.isNoteDisplayed("Old Note"));

        notes.editNote("Old Note", "Updated Note", "Updated desc");

        Assert.assertTrue(notes.isNoteDisplayed("Updated Note"));
    }
}