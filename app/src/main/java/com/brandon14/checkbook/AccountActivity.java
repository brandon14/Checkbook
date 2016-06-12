package com.brandon14.checkbook;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.brandon14.checkbook.adapters.TransactionAdapter;
import com.brandon14.checkbook.intentkeys.AccountIntentKeys;
import com.brandon14.checkbook.model.Account;
import com.brandon14.checkbook.model.database.Checkbook;
import com.brandon14.checkbook.requests.ActivityRequests;
import com.brandon14.checkbook.resultcodes.AccountResultCodes;

import java.text.DecimalFormat;

public class AccountActivity extends AppCompatActivity {
    private static final String ARG_IS_EDIT = "arg_is_edit";
    private static final String ARG_ACCOUNT_ID = "arg_account_id";
    private static final String ARG_ACCOUNT_TITLE = "arg_account_title";
    private static final String ARG_ACCOUNT_POSITION = "arg_account_position";

    private Toolbar mToolbar;
    private FloatingActionButton mAddFAB;
    private TextView mAccountBalanceTextView;
    private TextView mAddTransactionMessage;

    private TransactionAdapter mTransactionAdapter;

    /**
     *
     */
    private long mAccountId;
    private String mAccountName;
    private int mAccountPosition;
    private CharSequence mTitle;

    private Account mAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();

        mAccountId = intent.getLongExtra(ARG_ACCOUNT_ID, -1);
        mAccountName = intent.getStringExtra(ARG_ACCOUNT_TITLE);
        mAccountPosition = intent.getIntExtra(ARG_ACCOUNT_POSITION, -1);

        mTitle = mAccountName;

        mAccount = Checkbook.getInstance(getApplicationContext()).getAccount(mAccountId);

        setContentView(R.layout.activity_account);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mToolbar.setTitle(mTitle);

        mAddFAB = (FloatingActionButton) findViewById(R.id.fab_add_transactions);

        mAccountBalanceTextView = (TextView) findViewById(R.id.text_view_account_balance);
        mAddTransactionMessage = (TextView) findViewById(R.id.textview_add_transaction_message);

        mAccountBalanceTextView.setText(DecimalFormat.getCurrencyInstance().format(mAccount.getCurrentBalance().doubleValue()));
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
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_account, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_account_edit) {
            Intent intent = new Intent(this, AddEditAccount.class);
            intent.putExtra(ARG_IS_EDIT, true);
            intent.putExtra(ARG_ACCOUNT_ID, mAccount.getAccountId());

            startActivityForResult(intent, ActivityRequests.ACCOUNT_REQUEST);
        } else if (id == R.id.action_account_delete) {
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which){
                        case DialogInterface.BUTTON_POSITIVE:
                            if (!Checkbook.getInstance(getApplicationContext()).deleteAccount(mAccount.getAccountId())) {
                                CoordinatorLayout coordinatorLayout = (CoordinatorLayout) findViewById(R.id.accounts_fragment_coordinator);

                                if (coordinatorLayout != null) {
                                    Snackbar snackbar = Snackbar.make(coordinatorLayout,
                                            getResources().getString(R.string.str_error_deleting_account), Snackbar.LENGTH_LONG);
                                    snackbar.show();
                                }
                            }

                            Intent intent = getIntent().putExtra(AccountIntentKeys.ARG_ACCOUNT_POSITION, mAccountPosition);

                            setResult(AccountResultCodes.ACCOUNT_DELETED, intent);
                            finish();

                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            break;
                    }
                }
            };

            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setMessage(getResources().getString(R.string.str_confirm_account_deletion)).setPositiveButton(getResources().getString(R.string.str_yes), dialogClickListener)
                    .setNegativeButton(getResources().getString(R.string.str_no), dialogClickListener).show();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        mAccount = Checkbook.getInstance(getApplicationContext()).getAccount(mAccountId);
        mAccountName = mAccount.getAccountName();

        mTitle = mAccountName;

        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setTitle(mTitle);
        }

        mAccountBalanceTextView.setText(DecimalFormat.getCurrencyInstance().format(mAccount.getCurrentBalance().doubleValue()));
    }
}
