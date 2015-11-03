package com.leosko.todotxt_gdrive;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.leosko.todotxt_gdrive.com.leosko.todotxt_gdrive.model.Task;

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
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        Task task = getItem(position);

        View rowView = inflater.inflate(R.layout.listitem, parent, false);
        TextView descriptionView = (TextView) rowView.findViewById(R.id.description_view);
        descriptionView.setText(task.getText());

        return rowView;
    }
    
}
