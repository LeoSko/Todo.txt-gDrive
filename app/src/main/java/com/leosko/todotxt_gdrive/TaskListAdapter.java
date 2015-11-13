package com.leosko.todotxt_gdrive;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.leosko.todotxt_gdrive.model.Task;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by user on 03.11.2015.
 */
public class TaskListAdapter extends ArrayAdapter<Task> {

    public TaskListAdapter(Context context, int textViewResourceId, ArrayList<Task> objects) {
        super(context, textViewResourceId, objects);
    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Task task = getItem(position);

        final View rowView = inflater.inflate(R.layout.list_item, parent, false);
        TextView descriptionView = (TextView) rowView.findViewById(R.id.description_view);
        TextView dateView = (TextView) rowView.findViewById(R.id.date_view);
        CheckBox cb = (CheckBox) rowView.findViewById(R.id.completion_check_box);

        rowView.setTag(R.layout.list_item);
        descriptionView.setTag(R.id.description_view);
        dateView.setTag(R.id.date_view);
        cb.setTag(R.id.completion_check_box);

        cb.setChecked(task.isComplete());
        dateView.setText(task.getDate());
        descriptionView.setText(task.getText());
        dateView.setText(task.getDate());

        cb.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ((ListView) v.getParent().getParent()).performItemClick(v, position, 0); // Let the event be handled in onItemClick()
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
                ((ListView) v.getParent().getParent().getParent()).performItemClick(v, position, 0); // Let the event be handled in onItemClick()
            }
        });
        descriptionView.setOnClickListener(new View.OnClickListener()
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
    public boolean isEnabled(int position)
    {
        return (getItem(position).isComplete());
    }
}
