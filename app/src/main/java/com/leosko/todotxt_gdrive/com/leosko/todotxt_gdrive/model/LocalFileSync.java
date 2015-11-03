package com.leosko.todotxt_gdrive.com.leosko.todotxt_gdrive.model;

import com.leosko.todotxt_gdrive.MainActivity;

import java.io.File;
import java.io.IOException;

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

    }

    public void save()
    {

    }
}
