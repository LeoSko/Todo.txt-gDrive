package com.leosko.todotxt_gdrive.com.leosko.todotxt_gdrive.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.regex.*;

/**
 * Created by LeoSko on 28.10.2015.
 */
public class Task
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

    public static final String TASK_DEFAULT_TEXT = "New task";
    public static final String TASK_COMPLETED_FLAG = "x ";
    public static Matcher TASK_HAS_PRIOR_REGEX;

    private String text;

    public Task(String _text)
    {
        text = _text;
    }

    public boolean isComplete()
    {
        if (text.length() < 2)
            return false;
        return (text.charAt(0) == 'x' && text.charAt(1) == ' ');
    }

    public boolean hasPriority()
    {
        if (isComplete())
        {
            if (text.length() < 7)
            {
                return false;
            }
            char pr = text.charAt(3);
            boolean canBePrior = (pr >= 'A' && pr <='Z');
            return (text.charAt(2) == '(' &&  canBePrior && text.charAt(4) == ')' && text.charAt(5) == ' ');
        }
        else
        {
            if (text.length() < 5)
            {
                return false;
            }
            char pr = text.charAt(1);
            boolean canBePrior = (pr >= 'A' && pr <='Z');
            return (text.charAt(0) == '(' &&  canBePrior && text.charAt(2) == ')' && text.charAt(3) == ' ');
        }
    }

    public boolean hasDate()
    {
        // TODO (LeoSko) maybe check date for validity somehow?
        /*
        examples:
        2011-03-02 Document +TodoTxt task format
        (A) 2011-03-02 Call Mom
         */
        int start = 0;
        int end = 9;
        if (hasPriority())
        {
            start += 4;
            end += 4;
        }
        // not a date for sure
        if (end > text.length())
        {
            return false;
        }
        String potentialDate = text.substring(start, end);
        return (potentialDate.matches("[\\^\\n]\\d{4}-\\d{2}-\\d{2}"));
    }

    public void update(String newStr)
    {
        text = newStr;
    }

    public String getText()
    {
        return text;
    }

    public String getDate()
    {
        return "N/A, hardcoded";
    }
}
