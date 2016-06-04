package com.brandon14.checkbook;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;


public class AddEditTransaction extends AppCompatActivity {
    /**
     *
     */
    private static final String ARG_IS_EDIT = "arg_is_edit";
    private static final String ARG_ACCOUNT_ID = "arg_account_id";

    private Toolbar mToolbar;
    /**
     *
     */
    private int mAccountId;
    private boolean mIsEdit;
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        mIsEdit = intent.getBooleanExtra(ARG_IS_EDIT, false);
        mAccountId = intent.getIntExtra(ARG_ACCOUNT_ID, -1);

        if (mIsEdit) {
            mTitle = getString(R.string.title_edit_transaction);
        } else {
            mTitle = getString(R.string.title_add_transaction);
        }

        setContentView(R.layout.activity_add_edit_transaction);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mToolbar.setTitle(mTitle);
    }

    @Override
    public void onResume() {
        super.onResume();

        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setTitle(mTitle);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_add_edit_transaction, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }
}
