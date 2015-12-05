package com.leosko.todotxt_gdrive;

import android.app.AlertDialog;
import android.content.Context;
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
    Context cntxt;

    public TaskListItemClickListener(Context cntxt)
    {
        this.cntxt = cntxt;
    }

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
                break;
            case R.id.completion_check_box:
                task.changeCompletion();
                MainActivity.model.getAdapter().notifyDataSetChanged();
                MainActivity.lfs.save();
                break;
            case R.id.delete_task_btn:
                MainActivity.model.getAdapter().remove(task);
                MainActivity.lfs.save();
                break;
            case R.id.edit_task_btn:
                TaskEditDialog ted = new TaskEditDialog(cntxt, null);
                AlertDialog.Builder builder = ted.createTaskEditDialog(task);
                // create an alert dialog
                final AlertDialog alertD = builder.create();
                alertD.show();
                break;
            case R.id.complete_task_btn:
                task.changeCompletion();
                MainActivity.model.getAdapter().notifyDataSetChanged();
                MainActivity.lfs.save();
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
    {
        Task t = (Task) parent.getItemAtPosition(position);
        
        //Toast.makeText(MainActivity.getAppcntxt(), "LONG" + t.getText(), Toast.LENGTH_SHORT).show();
        return false;
    }
}
