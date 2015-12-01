package com.leosko.todotxt_gdrive;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.leosko.todotxt_gdrive.model.Task;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by LeoSko on 06.11.2015.
 */
public class TaskEditDialog
{
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    private Context cntxt;
    private Task task = null;

    public TaskEditDialog(Context cntx, Task t)
    {
        this.cntxt = cntx;
        this.task = t;
    }

    public AlertDialog.Builder createTaskCreationDialog()
    {
        // get *.xml view
        LayoutInflater layoutInflater = LayoutInflater.from(cntxt);
        View promptView = layoutInflater.inflate(R.layout.task_dialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(cntxt);

        // set *.xml to be the layout file of the alertdialog builder
        final EditText input = (EditText) promptView.findViewById(R.id.editText);
        final EditText context = (EditText) promptView.findViewById(R.id.editTextContext);
        final EditText project = (EditText) promptView.findViewById(R.id.editTextProject);
        final Spinner priority = (Spinner) promptView.findViewById(R.id.spinnerPriority);
        input.postDelayed(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                InputMethodManager keyboard = (InputMethodManager)cntxt.getSystemService(Context.INPUT_METHOD_SERVICE);
                keyboard.showSoftInput(input, 0);
            }
        },50);
        input.requestFocus();
        final Spinner spin = (Spinner) promptView.findViewById(R.id.spinnerPriority);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(cntxt, R.array.priorities, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spin.setAdapter(adapter);
        alertDialogBuilder.setView(promptView);

        // setup a dialog window
        alertDialogBuilder
                .setCancelable(true).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // get user input and set it to result
                Task nt = new Task(input.getText().toString(), context.getText().toString(), project.getText().toString());
                nt.setPriority(priority.getSelectedItem().toString());
                if (MainActivity.prefs.getBoolean(MainActivity.getAppcntxt().getString(R.string.pref_completionDate), true))
                {
                    nt.setCreationDate(new SimpleDateFormat(DATE_FORMAT).format(new Date()));
                }
                MainActivity.model.addTask(nt);
            }
        })

                .setNegativeButton("Cancel", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        dialog.cancel();
                    }
                });
        return alertDialogBuilder;
    }

    public AlertDialog.Builder createTaskEditDialog(final Task t)
    {
        // get *.xml view
        LayoutInflater layoutInflater = LayoutInflater.from(cntxt);
        View promptView = layoutInflater.inflate(R.layout.task_dialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(cntxt);

        // set *.xml to be the layout file of the alertdialog builder
        final EditText input = (EditText) promptView.findViewById(R.id.editText);
        final EditText context = (EditText) promptView.findViewById(R.id.editTextContext);
        final EditText project = (EditText) promptView.findViewById(R.id.editTextProject);
        final Spinner priority = (Spinner) promptView.findViewById(R.id.spinnerPriority);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(cntxt, R.array.priorities, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        priority.setAdapter(adapter);

        input.setText(t.getText());
        context.setText(t.getRawContexts());
        project.setText(t.getRawProjects());
        String pr = t.getPriority();
        int pridx = 0;
        if (pr.equals(""))
        {
            pridx = 0;
        }
        else
        {
            pridx = pr.charAt(0) - 'A' + 1;
        }
        priority.setSelection(pridx);

        input.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                // TODO Auto-generated method stub
                InputMethodManager keyboard = (InputMethodManager) cntxt.getSystemService(Context.INPUT_METHOD_SERVICE);
                keyboard.showSoftInput(input, 0);
            }
        }, 50);
        input.requestFocus();
        alertDialogBuilder.setView(promptView);

        // setup a dialog window
        alertDialogBuilder
                .setCancelable(true).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // get user input and set it to result
                Task nt = new Task(input.getText().toString(), context.getText().toString(), project.getText().toString());
                nt.setPriority(priority.getSelectedItem().toString());
                if (MainActivity.prefs.getBoolean(MainActivity.getAppcntxt().getString(R.string.pref_creationDate), true))
                {
                    nt.setCreationDate(t.getCreationDate());
                }
                if (t.isComplete())
                {
                    nt.changeCompletion();
                }
                int oldidx = MainActivity.model.getAdapter().getPosition(t);
                MainActivity.model.getAdapter().remove(t);
                MainActivity.model.getAdapter().insert(nt, oldidx);
            }
        })

                .setNegativeButton("Cancel", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        dialog.cancel();
                    }
                });
        return alertDialogBuilder;
    }
}
