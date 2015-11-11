package com.leosko.todotxt_gdrive.com.leosko.todotxt_gdrive.model;

import android.view.View;

import com.leosko.todotxt_gdrive.MainActivity;
import com.leosko.todotxt_gdrive.R;
import com.leosko.todotxt_gdrive.TaskListAdapter;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by LeoSko on 28.10.2015.
 */
public class Model
{
    private ArrayList<Task> tasks;
    private TaskListAdapter adapter;
    public Model()
    {
        tasks = new ArrayList<Task>();
        adapter = new TaskListAdapter(MainActivity.getAppcntxt(), R.id.TaskListView, tasks);
    }

    public void addTask(Task t)
    {
        //tasks.add(t);
        adapter.add(t);
        MainActivity.lfs.save();
        //adapter.getView(tasks.size() - 1, View.inflate(MainActivity.getAppcntxt(), R.layout.list_item, null), null).setEnabled(true);
    }

    public void removeTask(int id)
    {
        //remove task here
    }

    public int size()
    {
        return tasks.size();
    }

    public ArrayList<Task> getTasks()
    {
        return tasks;
    }

    public TaskListAdapter getAdapter()
    {
        return adapter;
    }

    public void setAdapter(TaskListAdapter adapter)
    {
        this.adapter = adapter;
    }
}
