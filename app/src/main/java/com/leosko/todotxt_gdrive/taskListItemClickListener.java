package com.leosko.todotxt_gdrive;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.leosko.todotxt_gdrive.MainActivity;
import com.leosko.todotxt_gdrive.com.leosko.todotxt_gdrive.model.Task;

/**
 * Created by LeoSko on 09.11.2015.
 */
public class TaskListItemClickListener implements ListView.OnItemClickListener, ListView.OnItemLongClickListener
{
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        Task t = (Task) parent.getItemAtPosition(position);
        Toast.makeText(MainActivity.getAppcntxt(), "SHORT" + t.getText(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
    {
        Task t = (Task) parent.getItemAtPosition(position);
        Toast.makeText(MainActivity.getAppcntxt(), "LONG" + t.getText(), Toast.LENGTH_SHORT).show();
        return false;
    }
}
