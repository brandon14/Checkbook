package com.brandon14.checkbook;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.support.v4.widget.DrawerLayout;
import android.view.View;

import com.brandon14.checkbook.fragments.AboutFragment;
import com.brandon14.checkbook.fragments.AccountsFragment;
import com.brandon14.checkbook.fragments.HelpFragment;
import com.brandon14.checkbook.fragments.NavigationDrawerFragment;
import com.brandon14.checkbook.fragments.RecurringFragment;
import com.brandon14.checkbook.fragments.ReportsFragment;
import com.brandon14.checkbook.fragments.SettingsFragment;

/**
 *
 */
public class MainActivity extends AppCompatActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks,
        SettingsFragment.OnSettingsFragmentInteractionListener {
    private static final String LOG_TAG = "MainActivity";

    private static final int BACK_PRESS_TIME = 2000;

    private NavigationDrawerFragment mNavigationDrawerFragment;

    private Toolbar mToolbar;

    private boolean mDoubleBackToExitPressedOnce = false;
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        final FragmentManager fragmentManager = getSupportFragmentManager();

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                fragmentManager.findFragmentById(R.id.navigation_drawer);

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    @Override
    public void onResume() {
        super.onResume();

        restoreActionBar();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onNavigationDrawerItemSelected(int id) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();

        switch(id) {
            case R.id.navigation_menu_accounts:
                if (fragmentManager.findFragmentById(R.id.container)
                        instanceof AccountsFragment) {
                    break;
                }

                fragmentManager.beginTransaction()
                        .replace(R.id.container, AccountsFragment.getInstance(), AccountsFragment.class.getSimpleName())
                        .commit();

                break;
            case R.id.navigation_menu_recurring:
                if (fragmentManager.findFragmentById(R.id.container)
                        instanceof RecurringFragment) {
                    break;
                }

                fragmentManager.beginTransaction()
                        .replace(R.id.container, RecurringFragment.newInstance(), RecurringFragment.class.getSimpleName())
                        .commit();

                break;
            case R.id.navigation_menu_reports:
                if (fragmentManager.findFragmentById(R.id.container)
                        instanceof ReportsFragment) {
                    break;
                }

                fragmentManager.beginTransaction()
                        .replace(R.id.container, ReportsFragment.newInstance(), ReportsFragment.class.getSimpleName())
                        .commit();

                break;
            case R.id.navigation_menu_settings:
                if (fragmentManager.findFragmentById(R.id.container)
                        instanceof SettingsFragment) {
                    break;
                }

                fragmentManager.beginTransaction()
                        .replace(R.id.container, SettingsFragment.newInstance(), SettingsFragment.class.getSimpleName())
                        .commit();

                break;
            case R.id.navigation_menu_help:
                if (fragmentManager.findFragmentById(R.id.container)
                        instanceof HelpFragment) {
                    break;
                }

                fragmentManager.beginTransaction()
                        .replace(R.id.container, HelpFragment.newInstance(), HelpFragment.class.getSimpleName())
                        .commit();

                break;
            case R.id.navigation_menu_about:
                if (fragmentManager.findFragmentById(R.id.container)
                        instanceof AboutFragment) {
                    break;
                }

                fragmentManager.beginTransaction()
                        .replace(R.id.container, AboutFragment.newInstance(), AboutFragment.class.getSimpleName())
                        .commit();

                break;
            default:

                break;
        }

        return true;
    }

    @Override
    public void onBackPressed() {
        // If we have already pressed back once, then we can exit as normal.
        if (mDoubleBackToExitPressedOnce) {
            super.onBackPressed();

            return;
        }

        // If the drawer is open we need to close it and return.
        if (mNavigationDrawerFragment.isDrawerOpen()) {
            mNavigationDrawerFragment.closeDrawer();

            return;
        }

        final FragmentManager fragmentManager = getSupportFragmentManager();
        final AccountsFragment fragment = (AccountsFragment) fragmentManager.findFragmentByTag(AccountsFragment.class.getSimpleName());

        // If the Accounts fragment is not shown, show it and select it in the drawer.
        if (fragment == null || !fragment.isVisible()) {
            fragmentManager.beginTransaction()
                    .replace(R.id.container, AccountsFragment.getInstance(), AccountsFragment.class.getSimpleName())
                    .commit();

            mNavigationDrawerFragment.selectNavigationDrawerItem(0);

            return;
        }

        // Otherwise we are in the accounts fragment and we can exit with a double back press normally
        View coordLayout = findViewById(R.id.accounts_fragment_coordinator);

        if (coordLayout != null) {
            Snackbar.make(coordLayout, getResources().getString(R.string.str_press_back_to_exit), Snackbar.LENGTH_SHORT).show();
        }

        mDoubleBackToExitPressedOnce = true;

        // Post a new delayed runnable to return mDoubleBackToExitPressed back to false in BACK_PRESS_TIME amount of time.
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                mDoubleBackToExitPressedOnce = false;
            }
        }, BACK_PRESS_TIME);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;

        if (mToolbar != null) {
            mToolbar.setTitle(mTitle);
        }
    }

    /**
     *
     */
    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setTitle(mTitle);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            restoreActionBar();

            return true;
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onSettingsFragmentInteraction(String id) {

    }
}
