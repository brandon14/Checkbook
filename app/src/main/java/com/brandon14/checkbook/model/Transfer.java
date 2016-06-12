package com.brandon14.checkbook.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by brandon on 1/30/15.
 */
public class Transfer implements Parcelable {
    private long mId;
    private long mToAccountId;
    private long mFromAccountId;

    public Transfer(long id, long toAccountId, long fromAccountId) {
        this.mId = id;
        this.mToAccountId = toAccountId;
        this.mFromAccountId = fromAccountId;
    }

    public long getFromAccountId() {
        return mFromAccountId;
    }

    public void setFromAccountId(long mFromAccountId) {
        this.mFromAccountId = mFromAccountId;
    }

    public long getId() {
        return mId;
    }

    public void setId(int mId) {
        this.mId = mId;
    }

    public long getToAccountId() {
        return mToAccountId;
    }

    public void setToAccountId(long mToAccountId) {
        this.mToAccountId = mToAccountId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(mId);
        dest.writeLong(mToAccountId);
        dest.writeLong(mFromAccountId);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {

        @Override
        public Transfer createFromParcel(Parcel source) {
            return new Transfer(source);
        }

        @Override
        public Transfer[] newArray(int size) {
            return new Transfer[size];
        }
    };

    private Transfer(Parcel in) {
        mId = in.readLong();
        mToAccountId = in.readLong();
        mFromAccountId = in.readLong();
    }
}
