package com.leosko.todotxt_gdrive;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Layout;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Spinner;

import com.leosko.todotxt_gdrive.R;

/**
 * Created by user on 29.10.2015.
 */
public class ListItemActivity extends Activity {
    private Spinner actionsSpinner;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listitem);
    }
}
