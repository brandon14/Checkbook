package com.brandon14.checkbook.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Brandon Clothier on 1/29/15.
 */
public class Account implements Comparable<Account>, Parcelable {
    private static final String LOG_TAG = "Accounts";

    private long mAccountId;

    private BigDecimal mStartingBalance;
    private BigDecimal mCurrentBalance;
    private BigDecimal mClearedBalance;
    private String mAccountName;
    private Date mAccountDateCreated;

    private ArrayList<Transaction> mTransactionEntries;

    public Account(long id, String name, BigDecimal startingBalance, Date date) {
        this.mAccountId = id;
        this.mAccountName = name;
        this.mStartingBalance = startingBalance;
        this.mCurrentBalance = startingBalance;
        this.mClearedBalance = startingBalance;
        this.mAccountDateCreated = date;
        this.mTransactionEntries = new ArrayList<>();
    }

    public Account(long id, String name, BigDecimal startingBalance, BigDecimal currentBalance,
                   BigDecimal clearedBalance, Date date) {
        this.mAccountId = id;
        this.mAccountName = name;
        this.mStartingBalance = startingBalance;
        this.mCurrentBalance = currentBalance;
        this.mClearedBalance = clearedBalance;
        this.mAccountDateCreated = date;
        this.mTransactionEntries = new ArrayList<>();
    }

    public long getAccountId() {
        return mAccountId;
    }

    public void setAccountId(int mAccountId) {
        this.mAccountId = mAccountId;
    }

    public BigDecimal getStartingBalance() {
        return mStartingBalance;
    }

    public void setStartingBalance(BigDecimal mStartingBalance) {
        this.mStartingBalance = mStartingBalance;
    }

    public BigDecimal getCurrentBalance() {
        return mCurrentBalance;
    }

    public void setCurrentBalance(BigDecimal mCurrentBalance) {
        this.mCurrentBalance = mCurrentBalance;
    }

    public BigDecimal getClearedBalance() {
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

    // Sort by account name lexographically.
    @Override
    public int compareTo(@NonNull Account another) {
        return this.mAccountName.compareToIgnoreCase(another.getAccountName());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(mAccountId);
        dest.writeString(mStartingBalance.toString());
        dest.writeString(mCurrentBalance.toString());
        dest.writeString(mClearedBalance.toString());
        dest.writeString(mAccountName);
        dest.writeLong(mAccountDateCreated.getTime());
        dest.writeTypedList(mTransactionEntries);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {

        @Override
        public Account createFromParcel(Parcel source) {
            return new Account(source);
        }

        @Override
        public Account[] newArray(int size) {
            return new Account[size];
        }
    };

    private Account(Parcel in) {
        mAccountId = in.readLong();
        mStartingBalance = new BigDecimal(in.readString());
        mCurrentBalance = new BigDecimal(in.readString());
        mClearedBalance = new BigDecimal(in.readString());
        mAccountName = in.readString();
        mAccountDateCreated = new Date(in.readLong());
        mTransactionEntries = new ArrayList<>();
        in.readTypedList(mTransactionEntries, Transaction.CREATOR);
    }
}
