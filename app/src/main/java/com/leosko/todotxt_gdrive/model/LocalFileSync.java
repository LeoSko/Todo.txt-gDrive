package com.leosko.todotxt_gdrive.model;

import com.leosko.todotxt_gdrive.MainActivity;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;

/**
 * Created by LeoSko on 28.10.2015.
 */
public class LocalFileSync
{
    public static final String TODO_FILE = "todo.txt";
    static File file;
    public LocalFileSync() throws IOException
    {
        file = new File(MainActivity.getAppcntxt().getFilesDir(), TODO_FILE);
        if (!file.exists())
        {
            if (!file.createNewFile())
            {
                throw new IOException("Cannot create file");
            }
        }
    }

    public void load()
    {
        String s;
        try
        {
            BufferedReader br = new BufferedReader(new FileReader(file));
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
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));
            for (Task t: MainActivity.model.getTasks())
            {
                bw.write(t.getText());
            }
            bw.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
