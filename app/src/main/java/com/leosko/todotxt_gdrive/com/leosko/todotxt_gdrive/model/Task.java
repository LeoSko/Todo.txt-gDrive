package com.leosko.todotxt_gdrive.com.leosko.todotxt_gdrive.model;

/**
 * Created by LeoSko on 28.10.2015.
 */
public class Task
{
    public static final String TASK_DEFAULT_TEXT = "New task";
    public static final String TASK_COMPLETED_FLAG = "x ";

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

    public void update(String newStr)
    {
        text = newStr;
    }

    public String getText()
    {
        return text;
    }
}
