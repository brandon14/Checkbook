package com.brandon14.checkbook.objects;

import android.util.Log;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

/**
 * Created by brandon on 1/30/15.
 */
public class Transaction {
    private static final String LOG_TAG = "Transaction";

    private int mId;

    private int mAccountId;
    private int mCategory;
    private boolean mIsCleared;
    private boolean mIsRecurring;
    private int mRecurringType;
    private int mRecurringLength;

    private BigDecimal mAmount;
    private String mPayee;
    private String mCheckNumber;
    private String mNotes;
    private Date mDate;

    public Transaction() {

    }

    public Transaction(int id, String transPayee, BigDecimal transAmount,
                       Date transDate, String checkNumber, int transCategory,
                       String transNotes, boolean isCleared, boolean isRecurring,
                       int recurringType, int recurringLength, int accountId) {
        this.mId = id;
        this.mAmount = transAmount;
        this.mDate = transDate;
        this.mCheckNumber = checkNumber;
        this.mPayee = transPayee;
        this.mCategory = transCategory;
        this.mNotes = transNotes;
        this.mIsCleared = isCleared;
        this.mIsRecurring = isRecurring;
        this.mRecurringType = recurringType;
        this.mRecurringLength = recurringLength;
        this.mAccountId = accountId;
    }

    public int getId() {
        return mId;
    }

    public void setId(int mId) {
        this.mId = mId;
    }

    public int getAccountId() {
        return mAccountId;
    }

    public void setAccountId(int mAccountId) {
        this.mAccountId = mAccountId;
    }

    public int getCategory() {
        return mCategory;
    }

    public void setCategory(int mCategory) {
        this.mCategory = mCategory;
    }

    public boolean isCleared() {
        return mIsCleared;
    }

    public void setIsCleared(boolean mIsCleared) {
        this.mIsCleared = mIsCleared;
    }

    public boolean isRecurring() {
        return mIsRecurring;
    }

    public void setIsRecurring(boolean mIsRecurring) {
        this.mIsRecurring = mIsRecurring;
    }

    public int getRecurringType() {
        return mRecurringType;
    }

    public void setRecurringType(int mRecurringType) {
        this.mRecurringType = mRecurringType;
    }

    public int getRecurringLength() {
        return mRecurringLength;
    }

    public void setRecurringLength(int mRecurringLength) {
        this.mRecurringLength = mRecurringLength;
    }

    public BigDecimal getAmount() {
        try {
            mAmount = mAmount.setScale(2, RoundingMode.HALF_UP);
        } catch (ArithmeticException | NullPointerException e) {
            Log.e(LOG_TAG, e.getMessage());
        }

        return mAmount;
    }

    public void setAmount(BigDecimal mAmount) {
        this.mAmount = mAmount;
    }

    public String getPayee() {
        return mPayee;
    }

    public void setPayee(String mPayee) {
        this.mPayee = mPayee;
    }

    public String getCheckNumber() {
        return mCheckNumber;
    }

    public void setCheckNumber(String mCheckNumber) {
        this.mCheckNumber = mCheckNumber;
    }

    public String getNotes() {
        return mNotes;
    }

    public void setNotes(String mNotes) {
        this.mNotes = mNotes;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date mDate) {
        this.mDate = mDate;
    }
}
