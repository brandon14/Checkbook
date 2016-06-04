package com.brandon14.checkbook.database;

import android.content.Context;
import android.util.Log;

import com.brandon14.checkbook.objects.Account;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by brandon on 2/13/15.
 */
public class Checkbook {
    private static final String LOG_TAG = "Checkbook";

    private CheckbookAccountCallbacks mAccountCallbacks;

    private CheckbookDbHelper mDbHelper;
    private ArrayList<Account> mAccountList;

    public Checkbook(Context context) {
        mDbHelper = new CheckbookDbHelper(context);
    }

    // Account functions
    public void setAccountCallback(CheckbookAccountCallbacks callbacks) {
        this.mAccountCallbacks = callbacks;
    }

    public boolean addAccount(String accountName, BigDecimal accountBalance, Date accountDate) {
        return mDbHelper.addAccount(accountName, accountBalance, accountDate);
    }

    public boolean updateAccount(int accountId, String accountName, BigDecimal accountBalance, Date accountDate, int position) {
        if (mAccountCallbacks == null) {
            Log.e(LOG_TAG, "Account callbacks is not stup! Please implement callbacks in calling class!");

            return false;
        }

        mAccountCallbacks.accountUpdated(position);

        return mDbHelper.updateAccount(accountId, accountName, accountBalance, accountDate);
    }

    public ArrayList<Account> getAccountEntries() {
        mAccountList = mDbHelper.getAccountEntries();

        return mAccountList;
    }

    public Account getAccount(int accountId) {
        return mDbHelper.getAccount(accountId);
    }

    public boolean deleteAccount(int accountId, int position) {
        if (mAccountCallbacks == null) {
            Log.e(LOG_TAG, "Account callbacks is not setup! Please implement callbacks in calling class!");

            return false;
        }

        mAccountCallbacks.accountDeleted(position);

        return mDbHelper.deleteAccount(accountId);
    }

    public boolean updateAccountBalance(int accountId) {
        return true;
    }

    public boolean updateAccountClearedBalance(int accountId) {
        return true;
    }

    public BigDecimal getTotalAccountsBalance() {
        BigDecimal accountsBalance = BigDecimal.ZERO;

        if (mAccountList != null) {
            for (int i = 0; i < mAccountList.size(); i++) {
                try {
                    accountsBalance = accountsBalance.add(mAccountList.get(i).getCurrentBalance());
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
        }

        return accountsBalance;
    }

    public interface CheckbookAccountCallbacks {
        void accountUpdated(int position);
        void accountDeleted(int position);
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
