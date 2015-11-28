package com.leosko.todotxt_gdrive;

import android.test.TouchUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.leosko.todotxt_gdrive.MainActivity;
import com.leosko.todotxt_gdrive.model.Task;

/**
 * Created by LeoSko on 09.11.2015.
 */
public class TaskListItemClickListener implements ListView.OnItemClickListener, ListView.OnItemLongClickListener
{
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        Integer viewId = (Integer) view.getTag();
        Task task = MainActivity.model.getAdapter().getItem(position);
        switch(viewId)
        {
            case R.id.description_view:
            case R.id.date_view:
            case R.layout.list_item:
                //Toast.makeText(MainActivity.getAppcntxt(), "Row elements", Toast.LENGTH_SHORT).show();
                break;
            case R.id.completion_check_box:
                MainActivity.model.getAdapter().getItem(position).changeCompletion();
                //Toast.makeText(MainActivity.getAppcntxt(), "Checkbox view", Toast.LENGTH_SHORT).show();
                break;
            case R.id.delete_task_btn:
                MainActivity.model.getAdapter().remove(task);
                break;
            case R.id.edit_task_btn:
                break;
            case R.id.complete_task_btn:
                task.changeCompletion();
                break;
            default:
                //Toast.makeText(MainActivity.getAppcntxt(), "Unknown view", Toast.LENGTH_SHORT).show();
                break;
        }
        //Task t = (Task) parent.getItemAtPosition(position);
        //Toast.makeText(MainActivity.getAppcntxt(), "SHORT" + t.getText(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
    {
        Task t = (Task) parent.getItemAtPosition(position);
        
        //Toast.makeText(MainActivity.getAppcntxt(), "LONG" + t.getText(), Toast.LENGTH_SHORT).show();
        return false;
    }
}
