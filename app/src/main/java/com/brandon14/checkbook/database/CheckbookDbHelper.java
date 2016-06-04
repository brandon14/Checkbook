package com.brandon14.checkbook.database;

import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.util.Log;

import com.brandon14.checkbook.objects.Account;

import java.math.BigDecimal;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Brandon Clothier on 1/29/15.
 */
public class CheckbookDbHelper extends SQLiteOpenHelper {
    private static final String LOG_TAG = "CheckbookDbHelper";

    private static final String DATABASE_DATE_FORMAT = "MM/dd/yyyy HH:mm:ss.SSSZ";
    private static final String SHORT_DATE_FORMAT = "MM/dd/yyyy";

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Checkbook.db";

    private Context mContext;

    public CheckbookDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        if (!db.isReadOnly()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                db.setForeignKeyConstraintsEnabled(true);
            } else {
                db.execSQL("PRAGMA foreign_keys=ON");
            }
        }

        db.execSQL(CheckbookContract.CheckbookDb.getSqlCreateAccountEntries());
        db.execSQL(CheckbookContract.CheckbookDb.getSqlCreateCategoryEntries());
        db.execSQL(CheckbookContract.CheckbookDb.getSqlCreatePayeeEntries());

        populateCategories();

        db.execSQL(CheckbookContract.CheckbookDb.getSqlCreateTransactionEntries());
        db.execSQL(CheckbookContract.CheckbookDb.getSqlCreateTransferEntries());
        db.execSQL(CheckbookContract.CheckbookDb.getSqlCreateRecurringEntries());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + CheckbookContract.CheckbookDb.getAccountTableName());
        db.execSQL("DROP TABLE IF EXISTS " + CheckbookContract.CheckbookDb.getTransactionTableName());
        db.execSQL("DROP TABLE IF EXISTS " + CheckbookContract.CheckbookDb.getTransferTableName());
        db.execSQL("DROP TABLE IF EXISTS " + CheckbookContract.CheckbookDb.getCategoryTableName());
        db.execSQL("DROP TABLE IF EXISTS " + CheckbookContract.CheckbookDb.getPayeeTableName());
        db.execSQL("DROP TABLE IF EXISTS " + CheckbookContract.CheckbookDb.getRecurringTableName());

        onCreate(db);
    }

    @Override
    @TargetApi(16)
    public void onConfigure(SQLiteDatabase db){
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            db.setForeignKeyConstraintsEnabled(true);
        } else {
            db.execSQL("PRAGMA foreign_keys=ON");
        }
    }

    // Category functions
    private void populateCategories() {

    }

    // Account functions
    public boolean addAccount(String accountName, BigDecimal accountBalance, Date date) {

        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(CheckbookContract.CheckbookDb.getColumnNameAccountName(), accountName);
        values.put(CheckbookContract.CheckbookDb.getColumnNameStartBalance(),
                accountBalance.doubleValue());
        values.put(CheckbookContract.CheckbookDb.getColumnNameAvailableBalance(),
                accountBalance.doubleValue());
        values.put(CheckbookContract.CheckbookDb.getColumnNameClearedBalance(),
                accountBalance.doubleValue());
        values.put(CheckbookContract.CheckbookDb.getColumnNameDateCreated(), new SimpleDateFormat(DATABASE_DATE_FORMAT, mContext.getResources().getConfiguration().locale).format(date));

        long newRowId = db.insert(CheckbookContract.CheckbookDb.getAccountTableName(),
                null, values);

        if (newRowId == -1) {
            Log.e(LOG_TAG, "Error creating account in the database!");

            db.close();

            return false;
        }

        db.close();

        return true;
    }

    public boolean updateAccount(int accountId, String accountName, BigDecimal accountBalance, Date date) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(CheckbookContract.CheckbookDb.getColumnNameAccountName(), accountName);
        values.put(CheckbookContract.CheckbookDb.getColumnNameStartBalance(),
                accountBalance.doubleValue());
        values.put(CheckbookContract.CheckbookDb.getColumnNameDateCreated(), new SimpleDateFormat(DATABASE_DATE_FORMAT, mContext.getResources().getConfiguration().locale).format(date));

        String whereClause = CheckbookContract.CheckbookDb.getColumnNameAccountId() + " = ?";
        String[] whereArgs = new String[] { Integer.toString(accountId) };

        int rowsAffected = db.update(CheckbookContract.CheckbookDb.getAccountTableName(), values, whereClause, whereArgs);

        if (rowsAffected != 1) {

            db.close();

            return false;
        }

        db.close();

        return true;
    }

    public ArrayList<Account> getAccountEntries() {
        ArrayList<Account> accountList = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();

        String orderBy = CheckbookContract.CheckbookDb.getColumnNameAccountName() + " COLLATE NOCASE";

        Cursor cursor = db.query(CheckbookContract.CheckbookDb.getAccountTableName(),
                null, null, null, null, null, orderBy);

        if (cursor.getCount() == 0) {
            Log.w(LOG_TAG, "No accounts found in database!");

            return accountList;
        }

        cursor.moveToFirst();

        int idIndex = cursor.getColumnIndex(CheckbookContract.CheckbookDb.getColumnNameAccountId());
        int nameIndex = cursor.getColumnIndex(CheckbookContract.CheckbookDb
                .getColumnNameAccountName());
        int startBalanceIndex = cursor.getColumnIndex(CheckbookContract.CheckbookDb
                .getColumnNameStartBalance());
        int availBalanceIndex = cursor.getColumnIndex(CheckbookContract.CheckbookDb
                .getColumnNameAvailableBalance());
        int clearedBalanceIndex = cursor.getColumnIndex(CheckbookContract.CheckbookDb
                .getColumnNameClearedBalance());
        int dateIndex = cursor.getColumnIndex(CheckbookContract.CheckbookDb
                .getColumnNameDateCreated());

        while (true) {
            int id = cursor.getInt(idIndex);
            String name = cursor.getString(nameIndex);
            BigDecimal startBalance = BigDecimal.valueOf(cursor.getDouble(startBalanceIndex));
            BigDecimal availBalance = BigDecimal.valueOf(cursor.getDouble(availBalanceIndex));
            BigDecimal clearedBalance = BigDecimal.valueOf(cursor.getDouble(clearedBalanceIndex));
            Date dateCreated = new SimpleDateFormat(DATABASE_DATE_FORMAT, mContext.getResources().getConfiguration().locale).parse(cursor.getString(dateIndex), new ParsePosition(0));

            accountList.add(new Account(id, name, startBalance, availBalance, clearedBalance, dateCreated));

            if (cursor.isLast()) {
                break;
            }

            cursor.moveToNext();
        }

        cursor.close();
        db.close();

        return accountList;
    }

    public Account getAccount(int accountId) {
        Account account;

        SQLiteDatabase db = getReadableDatabase();

        String selectionClause = CheckbookContract.CheckbookDb.getColumnNameAccountId() + " = ?";
        String selectionArgs[] = new String[] { Integer.toString(accountId) };

        Cursor cursor = db.query(CheckbookContract.CheckbookDb.getAccountTableName(), null, selectionClause, selectionArgs, null, null, null);

        if (cursor.getCount() == 0) {
            Log.e(LOG_TAG, "No accounts found in database!");

            return null;
        }

        cursor.moveToFirst();

        int idIndex = cursor.getColumnIndex(CheckbookContract.CheckbookDb.getColumnNameAccountId());
        int nameIndex = cursor.getColumnIndex(CheckbookContract.CheckbookDb
                .getColumnNameAccountName());
        int startBalanceIndex = cursor.getColumnIndex(CheckbookContract.CheckbookDb
                .getColumnNameStartBalance());
        int availBalanceIndex = cursor.getColumnIndex(CheckbookContract.CheckbookDb
                .getColumnNameAvailableBalance());
        int clearedBalanceIndex = cursor.getColumnIndex(CheckbookContract.CheckbookDb
                .getColumnNameClearedBalance());
        int dateIndex = cursor.getColumnIndex(CheckbookContract.CheckbookDb
                .getColumnNameDateCreated());

        int id = cursor.getInt(idIndex);
        String name = cursor.getString(nameIndex);
        BigDecimal startBalance = BigDecimal.valueOf(cursor.getDouble(startBalanceIndex));
        BigDecimal availBalance = BigDecimal.valueOf(cursor.getDouble(availBalanceIndex));
        BigDecimal clearedBalance = BigDecimal.valueOf(cursor.getDouble(clearedBalanceIndex));
        Date dateCreated = new SimpleDateFormat(DATABASE_DATE_FORMAT, mContext.getResources().getConfiguration().locale).parse(cursor.getString(dateIndex), new ParsePosition(0));

        account = new Account(id, name, startBalance, availBalance, clearedBalance, dateCreated);

        cursor.close();
        db.close();

        return account;
    }

    public boolean deleteAccount(int accountId) {

        SQLiteDatabase db = getWritableDatabase();

        String whereClause = CheckbookContract.CheckbookDb.getColumnNameAccountId() + " = ?";
        String[] whereArgs = new String[] { Integer.toString(accountId) };

        int rowsAffected = db.delete(CheckbookContract.CheckbookDb.getAccountTableName(), whereClause, whereArgs);

        if (rowsAffected != 1) {

            db.close();

            return false;
        }

        db.close();

        return true;
    }

    // Transaction functions

    // Transfer functions

    // Recurring functions

    // Payee functions

    // Getters
    public static String getDatabaseDateFormat() {
        return DATABASE_DATE_FORMAT;
    }

    public static String getShortDateFormat() {
        return SHORT_DATE_FORMAT;
    }
}
