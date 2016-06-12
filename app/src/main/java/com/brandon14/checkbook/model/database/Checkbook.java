package com.brandon14.checkbook.model.database;

import android.content.Context;

import com.brandon14.checkbook.model.Account;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by brandon on 2/13/15.
 */
public class Checkbook {
    private static final String LOG_TAG = "Checkbook";

    private static Checkbook sCheckbookInstance;

    private CheckbookDbHelper mDbHelper;

    public Checkbook(Context context) {
        mDbHelper = new CheckbookDbHelper(context);

        sCheckbookInstance = this;
    }

    public static Checkbook getInstance() {
        return sCheckbookInstance;
    }

    // Account functions
    public long addAccount(String accountName, BigDecimal accountBalance, Date accountDate) {
        return mDbHelper.addAccount(accountName, accountBalance, accountDate);
    }

    public boolean updateAccount(long accountId, String accountName, BigDecimal accountBalance, Date accountDate) {
        return mDbHelper.updateAccount(accountId, accountName, accountBalance, accountDate);
    }

    public ArrayList<Account> getAccountEntries() {
        return mDbHelper.getAccountEntries();
    }

    public Account getAccount(long accountId) {
        return mDbHelper.getAccount(accountId);
    }

    public boolean deleteAccount(long accountId) {
        return mDbHelper.deleteAccount(accountId);
    }

    public boolean updateAccountBalance(long accountId) {
        return true;
    }

    public boolean updateAccountClearedBalance(long accountId) {
        return true;
    }

    public interface CheckbookTransactionCallbacks {

    }

    public interface CheckbookTransferCallbacks {

    }

    public static String getDatabaseDateFormat() {
        return CheckbookDbHelper.getDatabaseDateFormat();
    }

    public static String getDatabaseShortDateFormat() {
        return CheckbookDbHelper.getShortDateFormat();
    }
}
