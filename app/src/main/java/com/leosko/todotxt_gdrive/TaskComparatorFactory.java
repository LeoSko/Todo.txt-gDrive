package com.leosko.todotxt_gdrive;

import com.leosko.todotxt_gdrive.model.Task;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

/**
 * Created by LeoSko on 12.12.2015.
 */
public class TaskComparatorFactory
{
    public enum SORT_TYPE {
        UNCOMPLETE_PRIORITY,
        PRIORITY,
        DATE,
        ALPHABETICAL
    };
    public static Comparator<Task> getTaskComparator(SORT_TYPE sort_type)
    {
        switch (sort_type)
        {
            case ALPHABETICAL:
                return new Comparator<Task>()
                {
                    @Override
                    public int compare(Task lhs, Task rhs)
                    {
                        return lhs.getText().compareTo(rhs.getText());
                    }
                };
            case DATE:
                return new Comparator<Task>()
                {
                    Comparator<Task> alpha = getTaskComparator(SORT_TYPE.ALPHABETICAL);
                    @Override
                    public int compare(Task lhs, Task rhs)
                    {
                        if (lhs.hasCreationDate())
                        {
                            if (rhs.hasCreationDate())
                            {
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Task.DATE_FORMAT);
                                try
                                {
                                    Date ld = simpleDateFormat.parse(lhs.getCreationDate());
                                    Date rd = simpleDateFormat.parse(rhs.getCreationDate());
                                    if (ld.equals(rd))
                                    {
                                        return alpha.compare(lhs, rhs);
                                    }
                                    else
                                    {
                                        return ld.compareTo(rd);
                                    }
                                } catch (ParseException parseException)
                                {
                                    parseException.printStackTrace();
                                }
                                return alpha.compare(lhs, rhs);
                            }
                            else
                            {
                                return -1;
                            }
                        }
                        else
                        {
                            if (rhs.hasCreationDate())
                            {
                                return 1;
                            }
                            else
                            {
                                return alpha.compare(lhs, rhs);
                            }
                        }
                    }
                };
            case PRIORITY:
                return new Comparator<Task>()
                {
                    Comparator<Task> date = getTaskComparator(SORT_TYPE.DATE);

                    @Override
                    public int compare(Task lhs, Task rhs)
                    {
                        if (lhs.hasPriority())
                        {
                            if (rhs.hasPriority())
                            {
                                if (lhs.getPriority().equals(rhs.getPriority()))
                                {
                                    return date.compare(lhs, rhs);
                                }
                                else
                                {
                                    return lhs.getPriority().compareTo(rhs.getPriority());
                                }
                            }
                            else
                            {
                                return -1;
                            }
                        }
                        else
                        {
                            if (rhs.hasPriority())
                            {
                                return 1;
                            }
                            else
                            {
                                return date.compare(lhs, rhs);
                            }
                        }
                    }
                };
            case UNCOMPLETE_PRIORITY:
            default:
                return new Comparator<Task>()
                {
                    private Comparator<Task> prior = getTaskComparator(SORT_TYPE.PRIORITY);
                    private Comparator<Task> date = getTaskComparator(SORT_TYPE.DATE);

                    @Override
                    public int compare(Task lhs, Task rhs)
                    {
                        if (!lhs.isComplete())
                        {
                            if (!rhs.isComplete())
                            {
                                return prior.compare(lhs, rhs);
                            }
                            else
                            {
                                return -1;
                            }
                        }
                        else
                        {
                            if (!rhs.isComplete())
                            {
                                return 1;
                            }
                            else
                            {
                                return date.compare(lhs, rhs);
                            }
                        }
                    }
                };
        }
    }
}
