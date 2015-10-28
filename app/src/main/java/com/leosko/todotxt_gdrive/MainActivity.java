package com.leosko.todotxt_gdrive;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.leosko.todotxt_gdrive.com.leosko.todotxt_gdrive.model.LocalFileSync;
import com.leosko.todotxt_gdrive.com.leosko.todotxt_gdrive.model.Model;

import java.io.IOException;

public class MainActivity extends AppCompatActivity
{
    public static final String CANT_CREATE_FILE = "Cannot create file";
    private Model model;
    private SharedPreferences prefs;
    private LocalFileSync lfs;
    private static Context appcntxt;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        appcntxt = getApplicationContext();
        prefs = PreferenceManager.getDefaultSharedPreferences(appcntxt);
        try
        {
            lfs = new LocalFileSync();
        }
        catch (IOException e)
        {
            //if we can't create file then just quit no matter what for now
            Toast.makeText(appcntxt, CANT_CREATE_FILE, Toast.LENGTH_LONG);
            finish();
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                //Create new task

                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
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