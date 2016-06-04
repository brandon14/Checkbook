package com.brandon14.checkbook.objects;

import android.util.Log;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Brandon Clothier on 1/29/15.
 */
public class Account {
    private static final String LOG_TAG = "Accounts";

    private int mAccountId;

    private BigDecimal mStartingBalance;
    private BigDecimal mCurrentBalance;
    private BigDecimal mClearedBalance;
    private String mAccountName;
    private Date mAccountDateCreated;

    private ArrayList<Transaction> mTransactionEntries;

    public Account(int id, String name) {
        this.mAccountId = id;
        this.mAccountName = name;
        this.mStartingBalance = BigDecimal.ZERO;
        this.mCurrentBalance = BigDecimal.ZERO;
        this.mClearedBalance = BigDecimal.ZERO;
    }

    public Account(int id, String name, BigDecimal startingBalance) {
        this.mAccountId = id;
        this.mAccountName = name;
        this.mStartingBalance = startingBalance;
        this.mCurrentBalance = startingBalance;
        this.mClearedBalance = startingBalance;
    }

    public Account(int id, String name, BigDecimal startingBalance, Date date) {
        this.mAccountId = id;
        this.mAccountName = name;
        this.mStartingBalance = startingBalance;
        this.mCurrentBalance = startingBalance;
        this.mClearedBalance = startingBalance;
        this.mAccountDateCreated = date;
    }

    public Account(int id, String name, BigDecimal startingBalance, BigDecimal currentBalance,
                   BigDecimal clearedBalance, Date date) {
        this.mAccountId = id;
        this.mAccountName = name;
        this.mStartingBalance = startingBalance;
        this.mCurrentBalance = currentBalance;
        this.mClearedBalance = clearedBalance;
        this.mAccountDateCreated = date;
    }

    public int getAccountId() {
        return mAccountId;
    }

    public void setAccountId(int mAccountId) {
        this.mAccountId = mAccountId;
    }

    public BigDecimal getStartingBalance() {
        try {
            mStartingBalance = mStartingBalance.setScale(2, RoundingMode.HALF_UP);
        } catch (ArithmeticException | NullPointerException e) {
            Log.e(LOG_TAG, e.getMessage());
        }

        return mStartingBalance;
    }

    public void setStartingBalance(BigDecimal mStartingBalance) {
        this.mStartingBalance = mStartingBalance;
    }

    public BigDecimal getCurrentBalance() {
        try {
            mCurrentBalance = mCurrentBalance.setScale(2, RoundingMode.HALF_UP);
        } catch (ArithmeticException | NullPointerException e) {
            Log.e(LOG_TAG, e.getMessage());
        }

        return mCurrentBalance;
    }

    public void setCurrentBalance(BigDecimal mCurrentBalance) {
        this.mCurrentBalance = mCurrentBalance;
    }

    public BigDecimal getClearedBalance() {
        try {
            mClearedBalance = mClearedBalance.setScale(2, RoundingMode.HALF_UP);
        } catch (ArithmeticException | NullPointerException e) {
            Log.e(LOG_TAG, e.getMessage());
        }

        return mClearedBalance;
    }

    public void setClearedBalance(BigDecimal mClearedBalance) {
        this.mClearedBalance = mClearedBalance;
    }

    public String getAccountName() {
        return mAccountName;
    }

    public void setAccountName(String mAccountName) {
        this.mAccountName = mAccountName;
    }

    public Date getAccountDateCreated() {
        return mAccountDateCreated;
    }

    public void setAccountDateCreated(Date mAccountDateCreated) {
        this.mAccountDateCreated = mAccountDateCreated;
    }


    public ArrayList<Transaction> getTransactionEntries() {
        return mTransactionEntries;
    }

    public void setTransactionEntries(ArrayList<Transaction> mTransactionEntries) {
        this.mTransactionEntries = mTransactionEntries;
    }
}
