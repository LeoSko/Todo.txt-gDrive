package com.leosko.todotxt_gdrive;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.leosko.todotxt_gdrive.model.LocalFileSync;
import com.leosko.todotxt_gdrive.model.Model;

import java.io.IOException;

public class MainActivity extends AppCompatActivity
{
    public static final String CANT_CREATE_FILE = "Cannot create file";
    public static Model model;
    public static LocalFileSync lfs;
    public static SharedPreferences prefs;
    private TaskListItemClickListener tlicl;
    private static Context appcntxt;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        appcntxt = getApplicationContext();
        model = new Model();
        tlicl = new TaskListItemClickListener(MainActivity.this);
        prefs = PreferenceManager.getDefaultSharedPreferences(appcntxt);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setLogo(R.drawable.ic_launcher);
        //toolbar.setNavigationIcon(R.drawable.ic_launcher);

        ListView taskListView = (ListView) findViewById(R.id.TaskListView);

        //ListView taskListView = (ListView) findViewById(R.id.TaskListView);
        taskListView.setAdapter(model.getAdapter());
        taskListView.setOnItemClickListener(tlicl);
        taskListView.setOnItemLongClickListener(tlicl);

        lfs = new LocalFileSync();
        lfs.load();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                //Create new task
                TaskEditDialog ted = new TaskEditDialog(MainActivity.this, null);
                final AlertDialog alertD = ted.createTaskCreationDialog();
                alertD.show();
            }
        });
    }

    public static Context getAppcntxt()
    {
        return appcntxt;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
