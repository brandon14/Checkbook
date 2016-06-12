package com.brandon14.checkbook;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

import com.brandon14.checkbook.intentkeys.AccountIntentKeys;
import com.brandon14.checkbook.model.Account;
import com.brandon14.checkbook.model.database.Checkbook;
import com.brandon14.checkbook.resultcodes.AccountResultCodes;
import com.brandon14.checkbook.utilities.MoneyTextWatcher;
import com.brandon14.checkbook.utilities.NumberUtilities;
import com.brandon14.checkbook.widgets.LimitedRangeDatePickerDialog;
import com.brandon14.checkbook.widgets.NonSelectableEditText;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class AddEditAccount extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener {
    /**
     *
     */
    private static final String LOG_TAG = "AddEditAccount";

    private TextInputEditText mAccountNameEditText;
    private NonSelectableEditText mAccountBalanceEditText;
    private Button mDateCreatedButton;
    private LimitedRangeDatePickerDialog mDatePickerDialog;

    /**
     *
     */
    private boolean mIsEdit;
    private int mAccountPosition;

    private Date mAccountDate;
    /**
     *
     */
    private CharSequence mTitle;

    private Account mAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        mIsEdit = intent.getBooleanExtra(AccountIntentKeys.ARG_IS_EDIT, false);
        long accountId = intent.getLongExtra(AccountIntentKeys.ARG_ACCOUNT_ID, -1);
        mAccountPosition = intent.getIntExtra(AccountIntentKeys.ARG_ACCOUNT_POSITION, -1);

        if (mIsEdit) {
            mTitle = getString(R.string.title_edit_account);
        } else {
            mTitle = getString(R.string.title_add_account);
        }

        setContentView(R.layout.activity_add_edit_account);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);

            actionBar.setTitle(mTitle);
        }

        mAccountNameEditText = (TextInputEditText) findViewById(R.id.edit_text_account_name);

        mAccountBalanceEditText = (NonSelectableEditText) findViewById(R.id.edit_text_account_balance);
        mDateCreatedButton = (Button) findViewById(R.id.btn_date_created_spinner);

        mAccountBalanceEditText.addTextChangedListener(new MoneyTextWatcher(getApplicationContext(), mAccountBalanceEditText));

        Calendar cal = new GregorianCalendar();

        SimpleDateFormat dateFormat = new SimpleDateFormat(Checkbook.SHORT_DATE_FORMAT, getResources().getConfiguration().locale);

        if (mIsEdit) {
            mAccount = Checkbook.getInstance(getApplicationContext()).getAccount(accountId);
            mAccountNameEditText.setText(mAccount.getAccountName());
            mAccountBalanceEditText.setText(DecimalFormat.getCurrencyInstance().format(mAccount.getStartingBalance().doubleValue()));
            mAccountDate = mAccount.getAccountDateCreated();

        } else {
            mAccountDate = dateFormat.parse(cal.get(Calendar.MONTH) + 1 + "/" + cal.get(Calendar.DAY_OF_MONTH) + "/" + cal.get(Calendar.YEAR), new ParsePosition(0));
        }

        cal.setTime(mAccountDate);
        mDateCreatedButton.setText(dateFormat.format(mAccountDate));

        Calendar minDate = new GregorianCalendar(1990, 0, 1);
        Calendar maxDate = new GregorianCalendar(cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));


        mDatePickerDialog = new LimitedRangeDatePickerDialog(this, this, cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), minDate, maxDate);

        mDateCreatedButton.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();

        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setTitle(mTitle);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_add_edit_account, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_add_edit_account_save) {
            if (mIsEdit) {
                boolean isUpdated = updateAccount();

                if (isUpdated) {
                    // Add results to mAdapter in Fragment Class
                    Intent intent = getIntent();
                    intent.putExtra(AccountIntentKeys.ARG_ACCOUNT_OBJECT, mAccount);
                    intent.putExtra(AccountIntentKeys.ARG_ACCOUNT_POSITION, mAccountPosition);

                    this.setResult(AccountResultCodes.ACCOUNT_UPDATED, intent);
                    this.finish();
                }
            } else {
                Account account = createAccount();

                if (account != null) {
                    // Add results to mAdapter in Fragment Class.
                    Intent intent = getIntent();
                    intent.putExtra(AccountIntentKeys.ARG_ACCOUNT_OBJECT, account);
                    
                    this.setResult(AccountResultCodes.ACCOUNT_CREATED, intent);
                    this.finish();
                }
            }
        } else if (id == R.id.action_add_edit_account_cancel) {
            cancel();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        cancel();
    }

    private Account createAccount() {
        if (!checkNameTextbox()) {
            return null;
        }

        BigDecimal accountBalance;

        if (mAccountBalanceEditText.getEditableText().toString().isEmpty()) {
            accountBalance = BigDecimal.ZERO;
        } else {
            try {
                accountBalance = new BigDecimal(NumberUtilities.cleanNumber(mAccountBalanceEditText.getEditableText().toString()));
            } catch (NumberFormatException e) {
                Log.e(LOG_TAG, e.getMessage());

                accountBalance = BigDecimal.ZERO;
            }
        }

        String accountName = mAccountNameEditText.getEditableText().toString();

        long accountId = Checkbook.getInstance(getApplicationContext()).addAccount(accountName, accountBalance, mAccountDate);

        if (accountId != -1) {
            return new Account(accountId, accountName, accountBalance, mAccountDate);
        }

        return null;
    }

    private boolean updateAccount() {
        if (!checkNameTextbox()) {
            return false;
        }

        BigDecimal accountBalance;

        if (mAccountBalanceEditText.getEditableText().toString().trim().length() == 0) {
            accountBalance = BigDecimal.ZERO;
        } else {
            try {
                accountBalance = new BigDecimal(NumberUtilities.cleanNumber(mAccountBalanceEditText.getEditableText().toString()));
            } catch (NumberFormatException e) {
                Log.e(LOG_TAG, e.getMessage());

                accountBalance = BigDecimal.ZERO;
            }
        }

        String accountName = mAccountNameEditText.getEditableText().toString();

        mAccount.setAccountName(accountName);
        mAccount.setStartingBalance(accountBalance);

        return Checkbook.getInstance(getApplicationContext()).updateAccount(mAccount.getAccountId(), accountName, accountBalance,
                mAccountDate);
    }

    private boolean checkNameTextbox() {
        if (mAccountNameEditText.getEditableText().toString().trim().length() == 0) {
            // Show snackbar error message.
            CoordinatorLayout coordLayout = (CoordinatorLayout) findViewById(R.id.layout_add_edit_account_coordinator);

            if (coordLayout != null) {
                Snackbar.make(coordLayout, getResources().getString(R.string.str_must_enter_account_name), Snackbar.LENGTH_LONG).show();
            }

            return false;
        }

        return true;
    }

    private void cancel() {
        this.setResult(AccountResultCodes.NO_ACTION);
        this.finish();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(Checkbook.SHORT_DATE_FORMAT, getResources().getConfiguration().locale);

        mAccountDate = dateFormat.parse(monthOfYear + 1 + "/" + dayOfMonth + "/" + year, new ParsePosition(0));

        if (mDateCreatedButton != null) {
            mDateCreatedButton.setText(dateFormat.format(mAccountDate));
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_date_created_spinner) {
            if (mDatePickerDialog != null) {
                mDatePickerDialog.show();
            }
        }
    }
}
