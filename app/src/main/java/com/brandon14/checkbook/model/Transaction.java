package com.brandon14.checkbook.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by brandon on 1/30/15.
 */
public class Transaction implements Parcelable {
    private static final String LOG_TAG = "Transaction";

    private long mId;

    private long mAccountId;
    private long mCategory;
    private boolean mIsCleared;
    private boolean mIsRecurring;
    private int mRecurringType;
    private int mRecurringLength;

    private BigDecimal mAmount;
    private long mPayee;
    private String mCheckNumber;
    private String mNotes;
    private Date mDate;

    public Transaction(long id, long transPayee, BigDecimal transAmount,
                       Date transDate, String checkNumber, long transCategory,
                       String transNotes, boolean isCleared, boolean isRecurring,
                       int recurringType, int recurringLength, long accountId) {
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

    public long getId() {
        return mId;
    }

    public void setId(long mId) {
        this.mId = mId;
    }

    public long getAccountId() {
        return mAccountId;
    }

    public void setAccountId(long mAccountId) {
        this.mAccountId = mAccountId;
    }

    public long getCategory() {
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
        return mAmount;
    }

    public void setAmount(BigDecimal mAmount) {
        this.mAmount = mAmount;
    }

    public long getPayee() {
        return mPayee;
    }

    public void setPayee(long mPayee) {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(mId);
        dest.writeLong(mAccountId);
        dest.writeLong(mCategory);
        dest.writeInt(mIsCleared ? 1 : 0);
        dest.writeInt(mIsRecurring ? 1 : 0);
        dest.writeInt(mRecurringType);
        dest.writeInt(mRecurringLength);
        dest.writeString(mAmount.toString());
        dest.writeLong(mPayee);
        dest.writeString(mCheckNumber);
        dest.writeString(mNotes);
        dest.writeLong(mDate.getTime());
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {

        @Override
        public Transaction createFromParcel(Parcel source) {
            return new Transaction(source);
        }

        @Override
        public Transaction[] newArray(int size) {
            return new Transaction[size];
        }
    };

    private Transaction(Parcel in) {
        mId = in.readLong();
        mAccountId = in.readLong();
        mCategory = in.readLong();
        mIsCleared = in.readInt() != 0;
        mIsRecurring = in.readInt() != 0;
        mRecurringType = in.readInt();
        mRecurringLength = in.readInt();
        mAmount = new BigDecimal(in.readString());
        mPayee = in.readLong();
        mCheckNumber = in.readString();
        mNotes = in.readString();
        mDate = new Date(in.readLong());
    }
}
