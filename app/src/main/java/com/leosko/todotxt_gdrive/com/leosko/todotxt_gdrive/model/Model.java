package com.leosko.todotxt_gdrive.com.leosko.todotxt_gdrive.model;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by LeoSko on 28.10.2015.
 */
public class Model
{
    private ArrayList<Task> tasks;
    public Model()
    {
        tasks = new ArrayList<Task>();
    }

    public void addTask(Task t)
    {
        tasks.add(t);
    }

    public int size()
    {
        return tasks.size();
    }

    public ArrayList<Task> getTasks()
    {
        return tasks;
    }
}
