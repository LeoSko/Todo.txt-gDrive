package com.leosko.todotxt_gdrive;

import com.leosko.todotxt_gdrive.model.Task;

import junit.framework.TestCase;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class TaskUnitTest extends TestCase
{
    @Override
    public void tearDown() throws Exception
    {
        super.tearDown();
    }

    @Override
    public void setUp() throws Exception
    {
        super.setUp();
    }

    public void testCompletion() throws Exception
    {
        Task t1 = new Task("something");
        Task t2 = new Task("x something");
        assertFalse(t1.isComplete());
        assertTrue(t2.isComplete());
        t1.changeCompletion();
        assertEquals(t1.getRawText(), t2.getRawText());
    }

    public void testDate() throws Exception
    {
        Task t1 = new Task("yyyy");
        assertFalse(t1.hasDate());
        assertEquals("", t1.getDate());
        //current date
        String currentDate = new SimpleDateFormat(com.leosko.todotxt_gdrive.TaskEditDialog.DATE_FORMAT).format(new Date());
        t1.setCreationDate(currentDate);
        assertTrue(t1.hasDate());
        assertEquals(currentDate, t1.getDate());
    }
}