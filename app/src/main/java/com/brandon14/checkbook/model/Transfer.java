package com.brandon14.checkbook.model;

/**
 * Created by brandon on 1/30/15.
 */
public class Transfer {
    private int mId;
    private int mToAccountId;
    private int mFromAccountId;

    public Transfer() {

    }

    public Transfer(int id, int toAccountId, int fromAccountId) {
        this.mId = id;
        this.mToAccountId = toAccountId;
        this.mFromAccountId = fromAccountId;
    }

    public int getFromAccountId() {
        return mFromAccountId;
    }

    public void setFromAccountId(int mFromAccountId) {
        this.mFromAccountId = mFromAccountId;
    }

    public int getId() {
        return mId;
    }

    public void setId(int mId) {
        this.mId = mId;
    }

    public int getToAccountId() {
        return mToAccountId;
    }

    public void setToAccountId(int mToAccountId) {
        this.mToAccountId = mToAccountId;
    }
}
