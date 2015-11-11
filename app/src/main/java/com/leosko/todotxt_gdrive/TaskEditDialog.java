package com.leosko.todotxt_gdrive;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.leosko.todotxt_gdrive.com.leosko.todotxt_gdrive.model.Task;

import org.w3c.dom.Text;

/**
 * Created by LeoSko on 06.11.2015.
 */
public class TaskEditDialog
{
    private Context cntxt;
    private Task task = null;

    public TaskEditDialog(Context cntx, Task t)
    {
        this.cntxt = cntx;
        this.task = t;
    }

    public AlertDialog.Builder createTaskEditDialog()
    {
        // get *.xml view
        LayoutInflater layoutInflater = LayoutInflater.from(cntxt);
        View promptView = layoutInflater.inflate(R.layout.task_dialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(cntxt);

        // set *.xml to be the layout file of the alertdialog builder
        final EditText input = (EditText) promptView.findViewById(R.id.editText);
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
                MainActivity.model.addTask(new Task(input.getText().toString()));
            }
        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,	int id) {
                                dialog.cancel();
                            }
                        });
        return alertDialogBuilder;
    }
}
