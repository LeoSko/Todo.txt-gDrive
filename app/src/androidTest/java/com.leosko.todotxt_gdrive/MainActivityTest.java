package com.leosko.todotxt_gdrive;

import android.app.Activity;
import android.app.Instrumentation;
import android.support.design.widget.FloatingActionButton;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.action.CoordinatesProvider;
import android.support.test.espresso.action.GeneralClickAction;
import android.support.test.espresso.action.Press;
import android.support.test.espresso.action.Tap;
import android.support.test.espresso.action.ViewActions;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.test.UiThreadTest;
import android.test.suitebuilder.annotation.MediumTest;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.leosko.todotxt_gdrive.model.Task;

import static android.support.test.espresso.Espresso.*;

/**
 * Created by LeoSko on 25.11.2015.
 */
public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity>
{
    private static ViewAction clickXY(final int x, final int y) {
        return new GeneralClickAction(
                Tap.SINGLE,
                new CoordinatesProvider() {
                    @Override
                    public float[] calculateCoordinates(View view) {

                        final int[] screenPos = new int[2];
                        view.getLocationOnScreen(screenPos);

                        final float screenX = screenPos[0] + x;
                        final float screenY = screenPos[1] + y;
                        float[] coordinates = {screenX, screenY};

                        return coordinates;
                    }
                },
                Press.FINGER);
    }
    MainActivity mainActivity;
    FloatingActionButton fab;
    ListView lv;

    @MediumTest
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
        assertEquals(4, lv.getAdapter().getCount());
        for (int i = 800; i < 1000; i+=1)
        {
            clickXY(350, i);
        }
        assertEquals(4, lv.getAdapter().getCount());
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
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
        mainActivity = getActivity();
        fab = (FloatingActionButton) mainActivity.findViewById(R.id.fab);
        lv = (ListView) mainActivity.findViewById(R.id.TaskListView);
    }

    public MainActivityTest()
    {
        super(MainActivity.class);
    }
}
