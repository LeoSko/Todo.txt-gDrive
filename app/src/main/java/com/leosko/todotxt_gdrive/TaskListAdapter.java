package com.leosko.todotxt_gdrive;

import android.content.Context;
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

        SwipeLayout rowView = (SwipeLayout) inflater.inflate(R.layout.swipe_list_item, parent, false);

        //set show mode.
        rowView.setShowMode(SwipeLayout.ShowMode.LayDown);

        //add drag edge.(If the BottomView has 'layout_gravity' attribute, this line is unnecessary)
        //rowView.addDrag(SwipeLayout.DragEdge.Left, findViewById(R.id.bottom_wrapper));

        rowView.addSwipeListener(new SwipeLayout.SwipeListener() {
            @Override
            public void onClose(SwipeLayout layout) {
                //when the SurfaceView totally cover the BottomView.
            }

            @Override
            public void onUpdate(SwipeLayout layout, int leftOffset, int topOffset) {
                //you are swiping.
            }

            @Override
            public void onStartOpen(SwipeLayout layout) {

            }

            @Override
            public void onOpen(SwipeLayout layout) {
                //when the BottomView totally show.
            }

            @Override
            public void onStartClose(SwipeLayout layout) {

            }

            @Override
            public void onHandRelease(SwipeLayout layout, float xvel, float yvel) {
                //when user's hand released.
            }
        });

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
    public boolean isEnabled(int position)
    {
        return (getItem(position).isComplete());
    }
}
