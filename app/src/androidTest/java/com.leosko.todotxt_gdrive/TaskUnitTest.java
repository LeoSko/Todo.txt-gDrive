package com.leosko.todotxt_gdrive;

import com.leosko.todotxt_gdrive.model.Task;

import junit.framework.TestCase;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class TaskUnitTest extends TestCase
{
    /*
    Rule 1: If priority exists, it ALWAYS appears first.

The priority is an uppercase character from A-Z enclosed in parentheses and followed by a space.

For example, this is a task with an A priority:

(A) Call Mom

These tasks have no priority:

    Really gotta call Mom (A) @phone @someday
    (b) Get back to the boss
    (B)->Submit TPS report

    Rule 2: A task’s creation date may optionally appear directly after priority and a space.

If there is no priority, the creation date appears first. If the creation date exists, it should be in the format YYYY-MM-DD.

These tasks have creation dates:

    2011-03-02 Document +TodoTxt task format
    (A) 2011-03-02 Call Mom
This task doesn’t have a creation date:

(A) Call Mom 2011-03-02

    Rule 3: Contexts and Projects may appear anywhere in the line after priority/prepended date.

A context is preceded by a single space and an @ sign. A project is preceded by a single space and a plus + sign. A project or context contains any non-whitespace character. A task may have zero, one, or more than one projects and contexts included in it.

For example, this task is part of the +Family and +PeaceLoveAndHappiness projects as well as the @iphone and @phone contexts:

(A) Call Mom +Family +PeaceLoveAndHappiness @iphone @phone

This task has no contexts in it:

Email SoAndSo at soandso@example.com

This task has no projects in it:

Learn how to add 2+2
    */
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

    public void testCreatingDateAdding() throws Exception
    {
        Task t1 = new Task("yyyy");
        assertFalse(t1.hasCreationDate());
        assertEquals("", t1.getCreationDate());
        //current date
        String currentDate = new SimpleDateFormat(Task.DATE_FORMAT).format(new Date());
        t1.setCreationDate(currentDate);
        assertTrue(t1.hasCreationDate());
        assertEquals(currentDate, t1.getCreationDate());
    }

    public void testDateRecognition() throws Exception
    {
        Task t1 = new Task("2011-03-02 Document +TodoTxt task format");
        Task t2 = new Task("(A) 2011-03-02 Call Mom");
        Task t3 = new Task("(A) Call Mom 2011-03-02");
        assertTrue(t1.hasCreationDate());
        assertTrue(t2.hasCreationDate());
        assertFalse(t3.hasCreationDate());
    }

    public void testCreatingDateChanging() throws Exception
    {
        Task t1 = new Task("2015-01-01 selebrate");
        assertTrue(t1.hasCreationDate());
        assertEquals("2015-01-01", t1.getCreationDate());
        t1.setCreationDate("2015-01-02");
        assertTrue(t1.hasCreationDate());
        assertEquals("2015-01-02", t1.getCreationDate());
    }

    public void testPriority() throws Exception
    {
        Task t1 = new Task("(A) Do this");
        Task t2 = new Task("(a) Do that");
        Task t3 = new Task("do again (b)");
        assertTrue(t1.hasPriority());
        assertFalse(t2.hasPriority());
        assertFalse(t3.hasPriority());
        // completion shouldn't change the priority
        t1.changeCompletion();
        t2.changeCompletion();
        t3.changeCompletion();
        assertTrue(t1.hasPriority());
        assertFalse(t2.hasPriority());
        assertFalse(t3.hasPriority());
    }
}