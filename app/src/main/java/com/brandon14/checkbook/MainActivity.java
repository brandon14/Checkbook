package com.brandon14.checkbook;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.support.v4.widget.DrawerLayout;
import android.view.View;

import com.brandon14.checkbook.application.IMMLeaks;
import com.brandon14.checkbook.fragments.AboutFragment;
import com.brandon14.checkbook.fragments.AccountFragment;
import com.brandon14.checkbook.fragments.AccountListFragment;
import com.brandon14.checkbook.fragments.AddEditAccountFragment;
import com.brandon14.checkbook.fragments.AddEditTransactionFragment;
import com.brandon14.checkbook.fragments.HelpFragment;
import com.brandon14.checkbook.fragments.NavigationDrawerFragment;
import com.brandon14.checkbook.fragments.RecurringFragment;
import com.brandon14.checkbook.fragments.ReportsFragment;
import com.brandon14.checkbook.fragments.SettingsFragment;
import com.brandon14.checkbook.model.Account;
import com.brandon14.checkbook.model.Transaction;

/**
 *
 */
public class MainActivity extends AppCompatActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks,
        AccountListFragment.AccountListNavigationCallbacks,
        AccountFragment.OnAccountUpdateCallbacks,
        AccountFragment.AccountNavigationCallbacks,
        AddEditAccountFragment.OnAccountAddEditCallbacks,
        AddEditTransactionFragment.OnTransactionAddEditCallbacks,
        SettingsFragment.OnSettingsFragmentInteractionListener {
    private static final String LOG_TAG = "MainActivity";
    private static final int BACK_PRESS_TIME = 2000;

    private static boolean sDoubleBackToExitPressedOnce = false;

    private static class BackPressHandler extends Handler {
        public BackPressHandler() {
            super();
        }
    }
    private static final Runnable sBackPressRunnable = new Runnable() {
        @Override
        public void run() {
            sDoubleBackToExitPressedOnce = false;
        }
    };

    private final BackPressHandler mBackPressHandler = new BackPressHandler();

    private NavigationDrawerFragment mNavigationDrawerFragment;
    private Toolbar mToolbar;

    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        IMMLeaks.fixFocusedViewLeak(getApplication());

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

        fragmentManager.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
                    mNavigationDrawerFragment.toggleHomeIndicator(true);
                }
            }
        });
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
                        instanceof AccountListFragment) {
                    break;
                }

                fragmentManager.beginTransaction()
                        .replace(R.id.container, AccountListFragment.newInstance(), AccountListFragment.class.getSimpleName())
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit();

                break;
            case R.id.navigation_menu_recurring:
                if (fragmentManager.findFragmentById(R.id.container)
                        instanceof RecurringFragment) {
                    break;
                }

                fragmentManager.beginTransaction()
                        .replace(R.id.container, RecurringFragment.newInstance(), RecurringFragment.class.getSimpleName())
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit();

                break;
            case R.id.navigation_menu_reports:
                if (fragmentManager.findFragmentById(R.id.container)
                        instanceof ReportsFragment) {
                    break;
                }

                fragmentManager.beginTransaction()
                        .replace(R.id.container, ReportsFragment.newInstance(), ReportsFragment.class.getSimpleName())
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit();

                break;
            case R.id.navigation_menu_settings:
                if (fragmentManager.findFragmentById(R.id.container)
                        instanceof SettingsFragment) {
                    break;
                }

                fragmentManager.beginTransaction()
                        .replace(R.id.container, SettingsFragment.newInstance(), SettingsFragment.class.getSimpleName())
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit();

                break;
            case R.id.navigation_menu_help:
                if (fragmentManager.findFragmentById(R.id.container)
                        instanceof HelpFragment) {
                    break;
                }

                fragmentManager.beginTransaction()
                        .replace(R.id.container, HelpFragment.newInstance(), HelpFragment.class.getSimpleName())
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit();

                break;
            case R.id.navigation_menu_about:
                if (fragmentManager.findFragmentById(R.id.container)
                        instanceof AboutFragment) {
                    break;
                }

                fragmentManager.beginTransaction()
                        .replace(R.id.container, AboutFragment.newInstance(), AboutFragment.class.getSimpleName())
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit();

                break;
            default:

                break;
        }

        return true;
    }

    @Override
    public void onBackPressed() {
        // If the drawer is open we need to close it and return.
        if (mNavigationDrawerFragment.isDrawerOpen()) {
            mNavigationDrawerFragment.closeDrawer();

            return;
        }

        // If we have already pressed back once, then we can exit as normal.
        if (sDoubleBackToExitPressedOnce) {
            super.onBackPressed();

            return;
        }

        final FragmentManager fragmentManager = getSupportFragmentManager();

        if (fragmentManager.getBackStackEntryCount() == 0) {
            final AccountListFragment fragment = (AccountListFragment) fragmentManager.findFragmentByTag(AccountListFragment.class.getSimpleName());

            // If the Accounts fragment is not shown, show it and select it in the drawer.
            if (fragment == null || !fragment.isVisible()) {
                fragmentManager.beginTransaction()
                        .replace(R.id.container, AccountListFragment.newInstance(), AccountListFragment.class.getSimpleName())
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit();

                mNavigationDrawerFragment.selectNavigationDrawerItem(0);

                return;
            }
        } else {
            fragmentManager.popBackStack();

            return;
        }

        // Otherwise we are in the accounts fragment and we can exit with a double back press normally
        View coordLayout = findViewById(R.id.accounts_fragment_coordinator);

        if (coordLayout != null) {
            Snackbar.make(coordLayout, getResources().getString(R.string.str_press_back_to_exit), Snackbar.LENGTH_SHORT).show();
        }

        sDoubleBackToExitPressedOnce = true;

        // Post a new delayed runnable to return sDoubleBackToExitPressed back to false in BACK_PRESS_TIME amount of time.
        mBackPressHandler.postDelayed(sBackPressRunnable, BACK_PRESS_TIME);
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

    // TODO redesign the SettingsFragment and redo the interface. Probably use a MaterialPreference library.
    @Override
    public void onSettingsFragmentInteraction(String id) {

    }

    @Override
    public void launchAccountFragment(long accountId, String accountName, int accountPosition) {
        FragmentManager fragmentManager = getSupportFragmentManager();

        mNavigationDrawerFragment.toggleHomeIndicator(false);

        fragmentManager.beginTransaction()
                .replace(R.id.container, AccountFragment.newInstance(accountId, accountName, accountPosition), AccountFragment.class.getSimpleName())
                .addToBackStack(AccountFragment.class.getSimpleName())
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit();
    }

    @Override
    public void launchAddEditAccount(boolean isEdit, long accountId, int accountPosition) {
        FragmentManager fragmentManager = getSupportFragmentManager();

        mNavigationDrawerFragment.toggleHomeIndicator(false);

        fragmentManager.beginTransaction()
                .replace(R.id.container, AddEditAccountFragment.newInstance(isEdit, accountId, accountPosition), AddEditAccountFragment.class.getSimpleName())
                .addToBackStack(AddEditAccountFragment.class.getSimpleName())
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit();
    }

    @Override
    public void launchAddEditTransaction(boolean isEdit, long accountId, long transactionId, int transactionPosition) {
        FragmentManager fragmentManager = getSupportFragmentManager();

        mNavigationDrawerFragment.toggleHomeIndicator(false);

        fragmentManager.beginTransaction()
                .replace(R.id.container, AddEditTransactionFragment.newInstance(isEdit, accountId, transactionId, transactionPosition), AddEditTransactionFragment.class.getSimpleName())
                .addToBackStack(AddEditTransactionFragment.class.getSimpleName())
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit();
    }

    @Override
    public void onAccountDeleted(int position) {
        FragmentManager fragmentManager = getSupportFragmentManager();

        AccountListFragment listFrag = (AccountListFragment) fragmentManager.findFragmentByTag(AccountListFragment.class.getSimpleName());

        if (listFrag != null) {
            listFrag.onAccountDeleted(position);
        } else {
            Log.e(LOG_TAG, "AccountListFragment was not found!");
        }
    }

    @Override
    public void onAccountAdded(Account account) {
        FragmentManager fragmentManager = getSupportFragmentManager();

        AccountListFragment listFrag = (AccountListFragment) fragmentManager.findFragmentByTag(AccountListFragment.class.getSimpleName());

        if (listFrag != null) {
            listFrag.onAccountAdded(account);
        } else {
            Log.e(LOG_TAG, "AccountListFragment was not found!");
        }
    }

    @Override
    public void onAccountUpdated(int position, Account account) {
        FragmentManager fragmentManager = getSupportFragmentManager();

        AccountListFragment listFrag = (AccountListFragment) fragmentManager.findFragmentByTag(AccountListFragment.class.getSimpleName());

        if (listFrag != null) {
            listFrag.onAccountUpdated(position, account);
        } else {
            Log.e(LOG_TAG, "AccountListFragment was not found!");
        }
    }

    @Override
    public void onTransactionAdded(Transaction transaction) {

    }

    @Override
    public void onTransactionUpdated(int position, Transaction transaction) {

    }
}
