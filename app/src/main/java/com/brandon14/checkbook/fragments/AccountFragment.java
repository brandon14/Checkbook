package com.brandon14.checkbook.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.brandon14.checkbook.AddEditAccount;
import com.brandon14.checkbook.R;
import com.brandon14.checkbook.adapters.TransactionAdapter;
import com.brandon14.checkbook.intentkeys.AccountIntentKeys;
import com.brandon14.checkbook.model.Account;
import com.brandon14.checkbook.model.Transaction;
import com.brandon14.checkbook.model.database.Checkbook;
import com.brandon14.checkbook.requests.ActivityRequests;
import com.brandon14.checkbook.widgets.ContextMenuRecyclerView;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AccountNavigationCallbacks} interface
 * to handle interaction events.
 * Use the {@link AccountFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccountFragment extends Fragment {
    private static final String ARG_ACCOUNT_ID = "arg_account_id";
    private static final String ARG_ACCOUNT_NAME = "arg_account_name";
    private static final String ARG_ACCOUNT_POSITION = "arg_account_position";

    private static final String RECYCLER_VIEW_STATE_KEY = "recycler_view_state";
    private static final String TRANSACTION_LIST_STATE_KEY = "transaction_list_state";

    private FloatingActionButton mAddFAB;
    private TextView mAccountBalanceTextView;
    private TextView mAddTransactionMessage;

    private ContextMenuRecyclerView mTransactionRecyclerView;
    private TransactionAdapter mTransactionAdapter;
    private LinearLayoutManager mLayoutManager;

    private AppBarLayout mAccountBalanceView;

    private Parcelable mRecyclerViewState;

    /**
     *
     */
    private long mAccountId;
    private String mAccountName;
    private int mAccountPosition;

    private ArrayList<Transaction> mTransactionList;

    private Account mAccount;

    private AccountNavigationCallbacks mCallback;

    public AccountFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param accountId Id of the account.
     * @param accountName Name of the account.
     * @param accountPosition position of the account in the account recyclerview list.
     * @return A new instance of fragment AccountFragment.
     */
    public static AccountFragment newInstance(long accountId, String accountName, int accountPosition) {
        AccountFragment fragment = new AccountFragment();

        Bundle args = new Bundle();
        args.putLong(ARG_ACCOUNT_ID, accountId);
        args.putString(ARG_ACCOUNT_NAME, accountName);
        args.putInt(ARG_ACCOUNT_POSITION, accountPosition);

        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mAccountId = getArguments().getLong(ARG_ACCOUNT_ID);
            mAccountName = getArguments().getString(ARG_ACCOUNT_NAME);
            mAccountPosition = getArguments().getInt(ARG_ACCOUNT_POSITION);
        }

        // Retrieve list state and list/item positions
        if(savedInstanceState != null) {
            mRecyclerViewState = savedInstanceState.getParcelable(RECYCLER_VIEW_STATE_KEY);
            mTransactionList = savedInstanceState.getParcelableArrayList(TRANSACTION_LIST_STATE_KEY);
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
        View rootView = inflater.inflate(R.layout.fragment_account, container, false);

        mAccount = Checkbook.getInstance(getContext()).getAccount(mAccountId);

        mAddFAB = (FloatingActionButton) rootView.findViewById(R.id.fab_add_transactions);

        mAccountBalanceTextView = (TextView) rootView.findViewById(R.id.text_view_account_balance);
        mAddTransactionMessage = (TextView) rootView.findViewById(R.id.textview_add_transaction_message);
        mAccountBalanceView = (AppBarLayout) rootView.findViewById(R.id.account_balance_app_bar);

        mTransactionRecyclerView = (ContextMenuRecyclerView) rootView.findViewById(R.id.recycler_view_transactions);

        if (mTransactionList == null) {
            mTransactionList = Checkbook.getInstance(getContext()).getTransactions(mAccountId);
        }

        setUpRecyclerView();

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (mRecyclerViewState != null) {
            mLayoutManager.onRestoreInstanceState(mRecyclerViewState);
        }

        Activity activity = getActivity();
        activity.setTitle(mAccountName);
    }

    @Override
    public void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);

        mRecyclerViewState = mLayoutManager.onSaveInstanceState();
        state.putParcelable(RECYCLER_VIEW_STATE_KEY, mRecyclerViewState);
        state.putParcelableArrayList(TRANSACTION_LIST_STATE_KEY, mTransactionList);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_account, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                getActivity().onBackPressed();

                return true;
            case R.id.action_account_edit:
                Intent intent = new Intent(getActivity(), AddEditAccount.class);
                intent.putExtra(AccountIntentKeys.ARG_IS_EDIT, true);
                intent.putExtra(AccountIntentKeys.ARG_ACCOUNT_ID, mAccount.getAccountId());

                startActivityForResult(intent, ActivityRequests.ACCOUNT_REQUEST);

                return true;
            case R.id.action_account_delete:
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                if (!Checkbook.getInstance(getContext()).deleteAccount(mAccount.getAccountId())) {
                                    CoordinatorLayout coordinatorLayout = (CoordinatorLayout) getActivity().findViewById(R.id.accounts_fragment_coordinator);

                                    if (coordinatorLayout != null) {
                                        Snackbar snackbar = Snackbar.make(coordinatorLayout,
                                                getResources().getString(R.string.str_error_deleting_account), Snackbar.LENGTH_LONG);
                                        snackbar.show();
                                    }
                                }

                                // Switch back to account list fragment

                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                builder.setMessage(getResources().getString(R.string.str_confirm_account_deletion)).setPositiveButton(getResources().getString(R.string.str_yes), dialogClickListener)
                        .setNegativeButton(getResources().getString(R.string.str_no), dialogClickListener).show();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof AccountNavigationCallbacks) {
            mCallback = (AccountNavigationCallbacks) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement AccountNavigationCallbacks");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

        mCallback = null;
    }

    private void setUpRecyclerView() {
        mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mTransactionRecyclerView.setLayoutManager(mLayoutManager);

        registerForContextMenu(mTransactionRecyclerView);

        mTransactionAdapter = new TransactionAdapter(getActivity(), mTransactionList);
        mTransactionAdapter.SetOnViewClickListener(new TransactionAdapter.OnViewClickListener() {
            @Override
            public void onViewClick(View v, int position) {
                // Handle launching transaction details fragment
            }
        });

        notifyAccountDataChanged();

        mTransactionRecyclerView.setAdapter(mTransactionAdapter);
    }

    private void notifyAccountDataChanged() {
        refreshAccountBalanceView();
        refreshTransactionMessage();
    }

    private void refreshAccountBalanceView() {
        BigDecimal accountBalance = mTransactionAdapter.getAccountBalance();

        if (accountBalance.compareTo(BigDecimal.ZERO) < 0) {
            mAccountBalanceTextView.setTextColor(ContextCompat.getColor(getContext(), R.color.negative_red));
        }

        mAccountBalanceTextView.setText(DecimalFormat.getCurrencyInstance().format(accountBalance.doubleValue()));
    }

    private void refreshTransactionMessage() {
        if (mTransactionAdapter.getItemCount() == 0) {
            mAccountBalanceView.setVisibility(View.GONE);
            mTransactionRecyclerView.setVisibility(View.GONE);
            mAddTransactionMessage.setVisibility(View.VISIBLE);
        } else {
            mAccountBalanceView.setVisibility(View.VISIBLE);
            mTransactionRecyclerView.setVisibility(View.VISIBLE);
            mAddTransactionMessage.setVisibility(View.GONE);
        }
    }

    public interface AccountNavigationCallbacks {

    }
}
