package com.leosko.todotxt_gdrive;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.leosko.todotxt_gdrive.model.Task;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * Created by user on 03.11.2015.
 */
public class TaskListAdapter extends ArrayAdapter<Task> {
    Comparator<Task> comparator;

    public TaskListAdapter(Context context, int textViewResourceId, ArrayList<Task> objects) {
        super(context, textViewResourceId, objects);
    }

    public Comparator<Task> getComparator()
    {
        return comparator;
    }

    public void setComparator(Comparator<Task> comparator)
    {
        this.comparator = comparator;
    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Task task = getItem(position);

        SwipeLayout rowView = (SwipeLayout) inflater.inflate(R.layout.swipe_list_item, parent, false);

        //set show mode.
        rowView.setShowMode(SwipeLayout.ShowMode.PullOut);

        TextView descriptionView = (TextView) rowView.findViewById(R.id.description_view);
        TextView dateView = (TextView) rowView.findViewById(R.id.date_view);
        TextView projectView = (TextView) rowView.findViewById(R.id.project_view);
        TextView contextView = (TextView) rowView.findViewById(R.id.context_view);
        TextView priorityView = (TextView) rowView.findViewById(R.id.priority_view);
        CheckBox cb = (CheckBox) rowView.findViewById(R.id.completion_check_box);

        rowView.setTag(R.layout.list_item);
        descriptionView.setTag(R.id.description_view);
        dateView.setTag(R.id.date_view);
        cb.setTag(R.id.completion_check_box);
        projectView.setTag(R.id.project_view);
        contextView.setTag(R.id.context_view);
        priorityView.setTag(R.id.priority_view);

        if (task.isComplete())
        {
            rowView.findViewById(R.id.list_item).setBackgroundColor(ContextCompat.getColor(MainActivity.getAppcntxt(), R.color.taskCompleteForegroundBackground));
        }
        cb.setChecked(task.isComplete());
        dateView.setText(task.getCreationDate());
        descriptionView.setText(task.getText());
        projectView.setText(task.getProjects());
        contextView.setText(task.getContexts());
        priorityView.setText(task.getPriority());

        cb.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ((ListView) v.getParent().getParent().getParent()).performItemClick(v, position, 0); // Let the event be handled in onItemClick()
            }
        });
        rowView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ((ListView) v.getParent()).performItemClick(v, position, 0); // Let the event be handled in onItemClick()
            }
        });
        dateView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ((ListView) v.getParent().getParent().getParent().getParent()).performItemClick(v, position, 0); // Let the event be handled in onItemClick()
            }
        });
        descriptionView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ((ListView) v.getParent().getParent().getParent().getParent()).performItemClick(v, position, 0); // Let the event be handled in onItemClick()
            }
        });

        //now set same hack for back items

        Button deletebtn = (Button) rowView.findViewById(R.id.delete_task_btn);
        Button editbtn = (Button) rowView.findViewById(R.id.edit_task_btn);
        Button completebtn = (Button) rowView.findViewById(R.id.complete_task_btn);

        deletebtn.setTag(R.id.delete_task_btn);
        editbtn.setTag(R.id.edit_task_btn);
        completebtn.setTag(R.id.complete_task_btn);

        deletebtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ((ListView) v.getParent().getParent().getParent()).performItemClick(v, position, 0); // Let the event be handled in onItemClick()
            }
        });
        editbtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ((ListView) v.getParent().getParent().getParent()).performItemClick(v, position, 0); // Let the event be handled in onItemClick()
            }
        });
        completebtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ((ListView) v.getParent().getParent().getParent()).performItemClick(v, position, 0); // Let the event be handled in onItemClick()
            }
        });

        return rowView;
    }

    @Override
    public void notifyDataSetChanged()
    {
        setNotifyOnChange(false);
        if (comparator != null)
        {
            sort(comparator);
        }
        setNotifyOnChange(true);
        super.notifyDataSetChanged();
    }

    @Override
    public boolean isEnabled(int position)
    {
        return (getItem(position).isComplete());
    }
}
