package com.brandon14.checkbook.database;

import android.provider.BaseColumns;

/**
 * Created by Brandon Clothier on 1/29/15.
 */
public final class CheckbookContract {
    public CheckbookContract() {
        // Empty constructor
    }

    public static abstract class CheckbookDb implements BaseColumns {
        // Database definitions, i.e. table names and column names
        private static final String TABLE_ACCOUNT = "accounts";
        private static final String TABLE_TRANSACTION = "transactions";
        private static final String TABLE_RECURRING = "recurring";
        private static final String TABLE_TRANSFER = "transfers";
        private static final String TABLE_CATEGORY = "categories";
        private static final String TABLE_PAYEE = "payees";

        // Account table columns
        private static final String COLUMN_NAME_ACCOUNT_ID = "account_id";
        private static final String COLUMN_NAME_ACCOUNT_NAME = "account_name";
        private static final String COLUMN_NAME_START_BALANCE = "starting_balance";
        private static final String COLUMN_NAME_AVAILABLE_BALANCE = "available_balance";
        private static final String COLUMN_NAME_CLEARED_BALANCE = "cleared_balance";
        private static final String COLUMN_NAME_DATE_CREATED = "date_created";

        // Transaction table columns
        private static final String COLUMN_NAME_TRANSACTION_ID = "transaction_id";
        private static final String COLUMN_NAME_PAYEE = "transaction_payee";
        private static final String COLUMN_NAME_AMOUNT = "amount";
        private static final String COLUMN_NAME_DATE = "transaction_date";
        private static final String COLUMN_NAME_CHECK_NUMBER = "check_number";
        private static final String COLUMN_NAME_CATEGORY = "category";
        private static final String COLUMN_NAME_NOTES = "notes";
        private static final String COLUMN_NAME_IS_CLEARED = "is_cleared";
        private static final String COLUMN_NAME_IS_RECURRING = "is_recurring";

        // Recurring table columns
        private static final String COLUMN_NAME_RECURRING_ID = "recurring_id";
        private static final String COLUMN_NAME_RECURRING_START = "recurring_start";
        private static final String COLUMN_NAME_RECURRING_LENGTH = "recurring_length";

        // Transfer table columns
        private static final String COLUMN_NAME_TRANSFER_ID = "transfer_id";
        private static final String COLUMN_NAME_TRANSFER_FROM_ID = "transfer_from_id";
        private static final String COLUMN_NAME_TRANSFER_TO_ID = "transfer_to_id";

        // Category table columns
        private static final String COLUMN_NAME_CATEGORY_ID = "category_id";

        // Payee table columns
        private static final String COLUMN_NAME_PAYEE_ID = "payee_id";
        private static final String COLUMN_NAME_PAYEE_NAME = "payee_name";
        private static final String COLUMN_NAME_PAYEE_CATEGORY = "payee_category";

        // Create table strings
        private static final String SQL_CREATE_ACCOUNT_ENTRIES = "CREATE TABLE " + TABLE_ACCOUNT + " (" +
                        COLUMN_NAME_ACCOUNT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT DEFAULT 1, " +
                        COLUMN_NAME_ACCOUNT_NAME + " TEXT NOT NULL, " +
                        COLUMN_NAME_START_BALANCE + " REAL NOT NULL DEFAULT 0.00, " +
                        COLUMN_NAME_AVAILABLE_BALANCE + " REAL NOT NULL DEFAULT 0.00, " +
                        COLUMN_NAME_CLEARED_BALANCE + " REAL NOT NULL DEFAULT 0.00, " +
                        COLUMN_NAME_DATE_CREATED + " TEXT NOT NULL);";

        private static final String SQL_CREATE_TRANSACTION_ENTRIES = "CREATE TABLE " + TABLE_TRANSACTION + " (" +
                        COLUMN_NAME_TRANSACTION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT DEFAULT 1, " +
                        COLUMN_NAME_PAYEE + " INTEGER NOT NULL, " +
                        COLUMN_NAME_AMOUNT + " REAL NOT NULL DEFAULT 0.00, " +
                        COLUMN_NAME_DATE + " TEXT NOT NULL, " +
                        COLUMN_NAME_CHECK_NUMBER + " TEXT, " +
                        COLUMN_NAME_NOTES + " TEXT, " +
                        COLUMN_NAME_IS_CLEARED + " INTEGER NOT NULL DEFAULT 0, " +
                        COLUMN_NAME_IS_RECURRING + " INTEGER NOT NULL DEFAULT 0, " +
                        COLUMN_NAME_ACCOUNT_ID + " INTEGER NOT NULL, " +
                        "FOREIGN KEY (" + COLUMN_NAME_PAYEE + ") REFERENCES " + TABLE_PAYEE +"(" + COLUMN_NAME_PAYEE_ID + ") ON DELETE CASCADE ON UPDATE CASCADE, " +
                        "FOREIGN KEY (" + COLUMN_NAME_ACCOUNT_ID + ") REFERENCES " + TABLE_ACCOUNT +"(" + COLUMN_NAME_ACCOUNT_ID + ") ON DELETE CASCADE ON UPDATE CASCADE);";

        private static final String SQL_CREATE_RECURRING_ENTRIES = "CREATE TABLE " + TABLE_RECURRING + " (" +
                        COLUMN_NAME_RECURRING_ID + " INTEGER PRIMARY KEY AUTOINCREMENT DEFAULT 1, " +
                        COLUMN_NAME_TRANSACTION_ID + " INTEGER NOT NULL, " +
                        COLUMN_NAME_RECURRING_START + " INTEGER NOT NULL, " +
                        COLUMN_NAME_RECURRING_LENGTH + " INTEGER NOT NULL, " +
                        "FOREIGN KEY (" + COLUMN_NAME_TRANSACTION_ID + ") REFERENCES " + TABLE_TRANSACTION + "(" + COLUMN_NAME_TRANSACTION_ID + ") ON DELETE CASCADE ON UPDATE CASCADE);";

        private static final String SQL_CREATE_TRANSFER_ENTRIES = "CREATE TABLE " + TABLE_TRANSFER + " (" +
                        COLUMN_NAME_TRANSFER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT DEFAULT 1, " +
                        COLUMN_NAME_TRANSFER_FROM_ID + " INTEGER NOT NULL, " +
                        COLUMN_NAME_TRANSFER_TO_ID + " INTEGER NOT NULL, " +
                        "FOREIGN KEY (" + COLUMN_NAME_TRANSFER_FROM_ID + ") REFERENCES " + TABLE_TRANSACTION + "(" + COLUMN_NAME_TRANSACTION_ID + ") ON DELETE CASCADE ON UPDATE CASCADE, " +
                        "FOREIGN KEY (" + COLUMN_NAME_TRANSFER_TO_ID + ") REFERENCES " + TABLE_TRANSACTION + "(" + COLUMN_NAME_TRANSACTION_ID + ") ON DELETE CASCADE ON UPDATE CASCADE);";

        private static final String SQL_CREATE_CATEGORY_ENTRIES = "CREATE TABLE " + TABLE_CATEGORY + " (" +
                        COLUMN_NAME_CATEGORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT DEFAULT 1, " +
                        COLUMN_NAME_CATEGORY + " TEXT NOT NULL);";

        private static final String SQL_CREATE_PAYEE_ENTRES = "CREATE TABLE " + TABLE_PAYEE + " (" +
                        COLUMN_NAME_PAYEE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT DEFAULT 1, " +
                        COLUMN_NAME_PAYEE_NAME + " TEXT NOT NULL DEFAULT 'N/A', " +
                        COLUMN_NAME_PAYEE_CATEGORY + " INTEGER NOT NULL, " +
                        "FOREIGN KEY (" + COLUMN_NAME_PAYEE_CATEGORY + ") REFERENCES " + TABLE_CATEGORY + "(" + COLUMN_NAME_CATEGORY_ID + ") ON DELETE CASCADE ON UPDATE CASCADE);";

        public static String getAccountTableName() {
            return TABLE_ACCOUNT;
        }

        public static String getTransactionTableName() {
            return TABLE_TRANSACTION;
        }

        public static String getRecurringTableName() {
            return TABLE_RECURRING;
        }

        public static String getTransferTableName() {
            return TABLE_TRANSFER;
        }

        public static String getCategoryTableName() {
            return TABLE_CATEGORY;
        }

        public static String getPayeeTableName() {
            return TABLE_PAYEE;
        }

        public static String getColumnNameAccountId() {
            return COLUMN_NAME_ACCOUNT_ID;
        }

        public static String getColumnNameAccountName() {
            return COLUMN_NAME_ACCOUNT_NAME;
        }

        public static String getColumnNameStartBalance() {
            return COLUMN_NAME_START_BALANCE;
        }

        public static String getColumnNameAvailableBalance() {
            return COLUMN_NAME_AVAILABLE_BALANCE;
        }

        public static String getColumnNameClearedBalance() {
            return COLUMN_NAME_CLEARED_BALANCE;
        }

        public static String getColumnNameDateCreated() {
            return COLUMN_NAME_DATE_CREATED;
        }

        public static String getColumnNameTransactionId() {
            return COLUMN_NAME_TRANSACTION_ID;
        }

        public static String getColumnNamePayee() {
            return COLUMN_NAME_PAYEE;
        }

        public static String getColumnNameAmount() {
            return COLUMN_NAME_AMOUNT;
        }

        public static String getColumnNameDate() {
            return COLUMN_NAME_DATE;
        }

        public static String getColumnNameCheckNumber() {
            return COLUMN_NAME_CHECK_NUMBER;
        }

        public static String getColumnNameCategory() {
            return COLUMN_NAME_CATEGORY;
        }

        public static String getColumnNameNotes() {
            return COLUMN_NAME_NOTES;
        }

        public static String getColumnNameIsCleared() {
            return COLUMN_NAME_IS_CLEARED;
        }

        public static String getColumnNameIsRecurring() {
            return COLUMN_NAME_IS_RECURRING;
        }

        public static String getColumnNameRecurringId() {
            return COLUMN_NAME_RECURRING_ID;
        }

        public static String getColumnNameRecurringStart() {
            return COLUMN_NAME_RECURRING_START;
        }

        public static String getColumnNameRecurringLength() {
            return COLUMN_NAME_RECURRING_LENGTH;
        }

        public static String getColumnNameTransferId() {
            return COLUMN_NAME_TRANSFER_ID;
        }

        public static String getColumnNameTransferFromId() {
            return COLUMN_NAME_TRANSFER_FROM_ID;
        }

        public static String getColumnNameTransferToId() {
            return COLUMN_NAME_TRANSFER_TO_ID;
        }

        public static String getColumnNameCategoryId() {
            return COLUMN_NAME_CATEGORY_ID;
        }

        public static String getColumnNamePayeeId() {
            return COLUMN_NAME_PAYEE_ID;
        }

        public static String getColumnNamePayeeName() {
            return COLUMN_NAME_PAYEE_NAME;
        }

        public static String getColumnNamePayeeCategory() {
            return COLUMN_NAME_PAYEE_CATEGORY;
        }

        public static String getSqlCreateAccountEntries() {
            return SQL_CREATE_ACCOUNT_ENTRIES;
        }

        public static String getSqlCreateTransactionEntries() {
            return SQL_CREATE_TRANSACTION_ENTRIES;
        }

        public static String getSqlCreateRecurringEntries() {
            return SQL_CREATE_RECURRING_ENTRIES;
        }

        public static String getSqlCreateTransferEntries() {
            return SQL_CREATE_TRANSFER_ENTRIES;
        }

        public static String getSqlCreateCategoryEntries() {
            return SQL_CREATE_CATEGORY_ENTRIES;
        }

        public static String getSqlCreatePayeeEntries() {
            return SQL_CREATE_PAYEE_ENTRES;
        }
    }
}
