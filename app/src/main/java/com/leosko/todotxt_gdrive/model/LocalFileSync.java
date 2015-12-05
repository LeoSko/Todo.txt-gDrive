package com.leosko.todotxt_gdrive.model;

import android.content.Context;

import com.leosko.todotxt_gdrive.MainActivity;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.util.FormatFlagsConversionMismatchException;

/**
 * Created by LeoSko on 28.10.2015.
 */
public class LocalFileSync
{
    public static final String TODO_FILE = "todo.txt";
    File file;

    public LocalFileSync()
    {
    }

    public void load()
    {
        String s;
        try
        {
            BufferedReader br = new BufferedReader(new InputStreamReader(MainActivity.getAppcntxt().openFileInput(TODO_FILE)));
            MainActivity.model.getAdapter().clear();
            while ((s = br.readLine()) != null)
            {
                MainActivity.model.addTask(new Task(s));
            }
            br.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void save()
    {
        String s;
        try
        {
            OutputStreamWriter osw = new OutputStreamWriter(MainActivity.getAppcntxt().openFileOutput(TODO_FILE, Context.MODE_PRIVATE));
            for (Task t: MainActivity.model.getTasks())
            {
                osw.write(t.getRawText() + '\n');
            }
            osw.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
