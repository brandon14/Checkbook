package com.brandon14.checkbook.fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;

import com.brandon14.checkbook.R;
import com.brandon14.checkbook.model.Account;
import com.brandon14.checkbook.model.database.Checkbook;
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

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnAccountAddEditCallbacks} interface
 * to handle interaction events.
 * Use the {@link AddEditAccountFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddEditAccountFragment extends Fragment implements View.OnClickListener, DatePickerDialog.OnDateSetListener {
    private static final String ARG_IS_EDIT = "arg_is_edit";
    private static final String ARG_ACCOUNT_ID = "arg_account_id";
    private static final String ARG_ACCOUNT_POSITION = "arg_account_poistion";

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
    private long mAccountId;
    private int mAccountPosition;

    private Date mAccountDate;
    /**
     *
     */
    private CharSequence mTitle;

    private Account mAccount;

    private OnAccountAddEditCallbacks mCallback;

    public AddEditAccountFragment() {
        // Required empty public constructor
    }

    public static AddEditAccountFragment newInstance(boolean isEdit, long accountId, int accountPosition) {
        AddEditAccountFragment fragment = new AddEditAccountFragment();

        Bundle args = new Bundle();

        args.putBoolean(ARG_IS_EDIT, isEdit);
        args.putLong(ARG_ACCOUNT_ID, accountId);
        args.putInt(ARG_ACCOUNT_POSITION, accountPosition);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mIsEdit = getArguments().getBoolean(ARG_IS_EDIT);
            mAccountId = getArguments().getLong(ARG_ACCOUNT_ID);
            mAccountPosition = getArguments().getInt(ARG_ACCOUNT_POSITION);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Indicate that this fragment would like to influence the set of actions in the action bar.
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_add_edit_account, container, false);

        if (mIsEdit) {
            mTitle = getString(R.string.title_edit_account);
        } else {
            mTitle = getString(R.string.title_add_account);
        }

        mAccountNameEditText = (TextInputEditText) rootView.findViewById(R.id.edit_text_account_name);

        mAccountBalanceEditText = (NonSelectableEditText) rootView.findViewById(R.id.edit_text_account_balance);
        mDateCreatedButton = (Button) rootView.findViewById(R.id.btn_date_created_spinner);

        mAccountBalanceEditText.addTextChangedListener(new MoneyTextWatcher(getContext(), mAccountBalanceEditText));

        Calendar cal = new GregorianCalendar();

        SimpleDateFormat dateFormat = new SimpleDateFormat(Checkbook.SHORT_DATE_FORMAT, getResources().getConfiguration().locale);

        if (mIsEdit) {
            mAccount = Checkbook.getInstance(getContext()).getAccount(mAccountId);
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


        mDatePickerDialog = new LimitedRangeDatePickerDialog(getContext(), this, cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), minDate, maxDate);

        mDateCreatedButton.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        Activity activity = getActivity();
        activity.setTitle(mTitle);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_add_edit_account, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch(id) {
            case android.R.id.home:
                finish();

                return true;
            case R.id.action_add_edit_account_save:
                if (mIsEdit) {
                    boolean isUpdated = updateAccount();

                    if (isUpdated) {
                        // Add results to mAdapter in Fragment Class
                        mCallback.onAccountUpdated(mAccountPosition, mAccount);

                        finish();
                    }
                } else {
                    Account account = createAccount();

                    if (account != null) {
                        // Add results to mAdapter in Fragment Class.
                        mCallback.onAccountAdded(account);

                        finish();
                    }
                }

                return true;
            case R.id.action_add_edit_account_cancel:
                finish();

                return true;
            default:

                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof OnAccountAddEditCallbacks) {
            mCallback = (OnAccountAddEditCallbacks) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnAccountAddEditCallbacks");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

        mCallback = null;
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

        long accountId = Checkbook.getInstance(getContext()).addAccount(accountName, accountBalance, mAccountDate);

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

        return Checkbook.getInstance(getContext()).updateAccount(mAccount.getAccountId(), accountName, accountBalance,
                mAccountDate);
    }

    private boolean checkNameTextbox() {
        if (mAccountNameEditText.getEditableText().toString().trim().length() == 0) {
            // Show snackbar error message.
            CoordinatorLayout coordLayout = (CoordinatorLayout) getActivity().findViewById(R.id.layout_add_edit_account_coordinator);

            if (coordLayout != null) {
                Snackbar.make(coordLayout, getResources().getString(R.string.str_must_enter_account_name), Snackbar.LENGTH_LONG).show();
            }

            return false;
        }

        return true;
    }

    private void finish() {
        View view = this.getView();

        if (view != null) {
            final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        getActivity().onBackPressed();
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnAccountAddEditCallbacks {
        void onAccountAdded(Account account);
        void onAccountUpdated(int position, Account account);
    }
}
