package com.leosko.todotxt_gdrive.model;

/**
 * Created by LeoSko on 28.10.2015.
 */
public class Task
{
    public static final String TASK_DEFAULT_TEXT = "New task";
    public static final String TASK_COMPLETED_FLAG = "x ";
    public static final String TASK_HAS_PRIOR_REGEX = "\\d{4}-\\d{2}-\\d{2}";
    public static final int DATE_LENGTH = 10;

    private String text;

    public Task(String _text)
    {
        text = _text;
    }

    public Task(String _text, String _context, String _project)
    {
        String rescontext = "";
        String resproject = "";
        if (!_context.isEmpty())
        {
            String[] contexts = _context.split(" ");
            for (String s : contexts)
            {
                rescontext += "@" + s + " ";
            }
            rescontext = rescontext.substring(0, rescontext.length() - 1);
        }
        if (!_project.isEmpty())
        {
            String[] projects = _project.split(" ");
            for (String s : projects)
            {
                resproject += "+" + s + " ";
            }
            resproject = resproject.substring(0, resproject.length() - 1);
        }
        text = _text + " " + rescontext + " " + resproject;
    }

    public String getProjects()
    {
        String res = "";
        for (String s : text.split(" "))
        {
            if (s.startsWith("+"))
            {
                res += s + " ";
            }
        }
        return res.substring(0, Math.max(0, res.length() - 1));
    }

    public String getContexts()
    {
        String res = "";
        for (String s : text.split(" "))
        {
            if (s.startsWith("@"))
            {
                res += s + " ";
            }
        }
        return res.substring(0, Math.max(0, res.length() - 1));
    }

    public void setCreationDate(String date)
    {
        if (hasCreationDate())
        {
            text.replace(getCreationDate(), date);
        }
        else
        {
            int start = 0;
            if (hasPriority())
            {
                start += 4;
            }
            if (isComplete())
            {
                start += 2;
            }
            text = text.substring(0, start) + " " + date + " " + text.substring(start);
            if (start == 0)
            {
                text = text.substring(1);
            }
        }
    }

    /*
    returns new state of completion
     */
    public boolean changeCompletion()
    {
        if (text.startsWith("x "))
        {
            text = text.substring(2);
            return false;
        }
        else
        {
            text = "x " + text;
            return true;
        }
    }

    public boolean isComplete()
    {
        return (text.startsWith(TASK_COMPLETED_FLAG) && text.length() >= 2);
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

    public boolean hasCreationDate()
    {
        // TODO (LeoSko) maybe check date for validity somehow?
        /*
        examples:
        2011-03-02 Document +TodoTxt task format
        (A) 2011-03-02 Call Mom
         */
        int start = 0;
        if (hasPriority())
        {
            start += 4;
        }
        if (isComplete())
        {
            start += 2;
        }
        // not a date for sure
        if (start + DATE_LENGTH > text.length())
        {
            return false;
        }
        String potentialDate = text.substring(start, start + DATE_LENGTH);
        return (potentialDate.matches(TASK_HAS_PRIOR_REGEX));
    }

    public void update(String newStr)
    {
        text = newStr;
    }

    public String getRawText()
    {
        return text;
    }

    public String getText()
    {
        int start = 0;
        if (hasPriority())
        {
            start += 4;
        }
        if (isComplete())
        {
            start += 2;
        }
        if (hasCreationDate())
        {
            start += DATE_LENGTH;
        }
        String res = "";
        for (String s : text.substring(start).split(" "))
        {
            if (!(s.startsWith("@") || s.startsWith("+")))
            {
                res += s + " ";
            }
        }
        return res.substring(0, res.length() - 1);
    }

    public String getCreationDate()
    {
        if (hasCreationDate())
        {
            int start = 0;
            if (hasPriority())
            {
                start += 4;
            }
            if (isComplete())
            {
                start += 2;
            }
            return text.substring(start, start + DATE_LENGTH);
        }
        else
        {
            return "";
        }
    }
}
