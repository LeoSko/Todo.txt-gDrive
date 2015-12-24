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
import java.util.Date;
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

    public static Date lastModifiedDate()
    {
        File file = new File(MainActivity.getAppcntxt().getFilesDir() + "/" + TODO_FILE);
        if (file.exists())
        {
            return new Date(file.lastModified());
        }
        else
        {
            return new Date(0);
        }
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

    public static void syncFromString(String source)
    {
        try
        {
            OutputStreamWriter osw = new OutputStreamWriter(MainActivity.getAppcntxt().openFileOutput(TODO_FILE, Context.MODE_PRIVATE));
            osw.write(source);
            osw.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public String getRawString()
    {
        String s;
        StringBuilder stringBuilder = new StringBuilder();
        try
        {
            BufferedReader br = new BufferedReader(new InputStreamReader(MainActivity.getAppcntxt().openFileInput(TODO_FILE)));
            while ((s = br.readLine()) != null)
            {
                stringBuilder.append(s + '\n');
            }
            br.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return stringBuilder.toString();
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
