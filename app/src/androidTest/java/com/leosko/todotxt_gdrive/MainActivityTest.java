package com.leosko.todotxt_gdrive;

import android.app.Activity;
import android.support.design.widget.FloatingActionButton;
import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import android.widget.ListView;

import com.leosko.todotxt_gdrive.com.leosko.todotxt_gdrive.model.Task;

/**
 * Created by LeoSko on 25.11.2015.
 */
public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity>
{
    MainActivity mainActivity;
    FloatingActionButton fab;
    ListView lv;

    @UiThreadTest
    public void testListViewClick() throws Exception
    {
        assertNotNull(mainActivity);
        assertNotNull(fab);
        assertNotNull(lv);
        assertNotNull(mainActivity.model);
        mainActivity.model.getAdapter().add(new Task("Test 1"));
        mainActivity.model.getAdapter().add(new Task("Test 2"));
        mainActivity.model.getAdapter().add(new Task("Test 3"));
        mainActivity.model.getAdapter().add(new Task("Test 4"));
        assertEquals(lv.getAdapter().getCount(), 4);
    }

    @Override
    public void tearDown() throws Exception
    {
        super.tearDown();
    }

    @Override
    public void setUp() throws Exception
    {
        super.setUp();
        mainActivity = getActivity();
        fab = (FloatingActionButton) mainActivity.findViewById(R.id.fab);
        lv = (ListView) mainActivity.findViewById(R.id.TaskListView);
    }

    public MainActivityTest()
    {
        super(MainActivity.class);
    }
}
