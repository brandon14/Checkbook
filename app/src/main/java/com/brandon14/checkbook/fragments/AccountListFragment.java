package com.brandon14.checkbook.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.brandon14.checkbook.model.database.Checkbook;
import com.brandon14.checkbook.model.Account;
import com.brandon14.checkbook.adapters.AccountAdapter;
import com.brandon14.checkbook.R;
import com.brandon14.checkbook.widgets.ContextMenuRecyclerView;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AccountListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccountListFragment extends Fragment {
    /**
     *
     */
    private static final String LOG_TAG = "AccountListFragment";
    private static final String RECYCLER_VIEW_STATE_KEY = "recycler_view_state";
    private static final String ACCOUNT_LIST_STATE_KEY = "account_list_state";

    private AccountListNavigationCallbacks mNavigationCallback;

    private AccountAdapter mAccountAdapter;
    private LinearLayoutManager mLayoutManager;
    private ContextMenuRecyclerView mAccountsRecyclerView;
    private AppBarLayout mTotalBalanceView;
    private TextView mTotalBalanceTextView;
    private TextView mAddAccountMessage;
    private FloatingActionButton mAddFAB;
    private Parcelable mRecyclerViewState;

    private ArrayList<Account> mAccountList;

    /**
     * * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment AccountFragment.
     */
    public static AccountListFragment newInstance() {
        return new AccountListFragment();
    }

    /**
     *
     */
    public AccountListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Retrieve list state and list/item positions
        if(savedInstanceState != null) {
            mRecyclerViewState = savedInstanceState.getParcelable(RECYCLER_VIEW_STATE_KEY);
            mAccountList = savedInstanceState.getParcelableArrayList(ACCOUNT_LIST_STATE_KEY);
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
        View rootView = inflater.inflate(R.layout.fragment_account_list, container, false);

        mTotalBalanceTextView = (TextView) rootView
                .findViewById(R.id.text_view_total_balance);
        mTotalBalanceView = (AppBarLayout) rootView.findViewById(R.id.total_balance_app_bar);
        mAddAccountMessage = (TextView) rootView.findViewById(R.id.text_view_add_account_message);

        mAccountsRecyclerView = (ContextMenuRecyclerView) rootView.findViewById(R.id.recycler_view_accounts);

        if (mAccountList == null) {
            mAccountList = Checkbook.getInstance(getContext()).getAccountEntries();
        }

        setUpRecyclerView();

        mAddFAB = (FloatingActionButton) rootView.findViewById(R.id.fab_add_accounts);
        mAddFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNavigationCallback.launchAddEditAccount(false, -1, -1);
            }
        });

        refreshAccountMessage();
        refreshTotalBalanceView();

        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof AccountListNavigationCallbacks) {
            mNavigationCallback = (AccountListNavigationCallbacks) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement AccountListNavigationCallbacks.");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

        mNavigationCallback = null;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (mRecyclerViewState != null) {
            mLayoutManager.onRestoreInstanceState(mRecyclerViewState);
        }

        Activity activity = getActivity();
        activity.setTitle(getResources().getString(R.string.title_accounts));
    }

    @Override
    public void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);

        if (mLayoutManager != null) {
            mRecyclerViewState = mLayoutManager.onSaveInstanceState();
            state.putParcelable(RECYCLER_VIEW_STATE_KEY, mRecyclerViewState);
        }

        if (mAccountList != null) {
            state.putParcelableArrayList(ACCOUNT_LIST_STATE_KEY, mAccountList);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        if (v.getId() == R.id.recycler_view_accounts) {
            MenuInflater inflater = getActivity().getMenuInflater();
            inflater.inflate(R.menu.menu_account_list_menu, menu);

            int itemIndex = ((ContextMenuRecyclerView.RecyclerViewContextMenuInfo) menuInfo).position;
            Account selectedAccount = mAccountAdapter.getItem(itemIndex);

            menu.setHeaderTitle(selectedAccount.getAccountName());
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        final ContextMenuRecyclerView.RecyclerViewContextMenuInfo info = (ContextMenuRecyclerView.RecyclerViewContextMenuInfo) item.getMenuInfo();
        final Account selectedAccount = mAccountAdapter.getItem(info.position);

        switch(item.getItemId()) {
            case R.id.account_list_delete:
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                if (!Checkbook.getInstance(getContext()).deleteAccount(selectedAccount.getAccountId())) {
                                    Snackbar snackbar = Snackbar.make(getActivity().findViewById(R.id.accounts_fragment_coordinator),
                                            getResources().getString(R.string.str_error_deleting_account), Snackbar.LENGTH_LONG);
                                    snackbar.show();
                                } else {
                                    mAccountAdapter.removeAccount(info.position);

                                    notifyAccountDataChanged();
                                }

                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                builder.setMessage(getResources().getString(R.string.str_confirm_account_deletion)).setPositiveButton(getResources().getString(R.string.str_yes), dialogClickListener)
                        .setNegativeButton(getResources().getString(R.string.str_no), dialogClickListener).show();

                return true;
            case R.id.account_list_edit:
                mNavigationCallback.launchAddEditAccount(true, selectedAccount.getAccountId(), info.position);

                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void setUpRecyclerView() {
        mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mAccountsRecyclerView.setLayoutManager(mLayoutManager);

        registerForContextMenu(mAccountsRecyclerView);

        mAccountAdapter = new AccountAdapter(getActivity(), mAccountList);
        mAccountAdapter.SetOnViewClickListener(new AccountAdapter.OnViewClickListener() {
            @Override
            public void onViewClick(View v, int position) {
                final Account account = mAccountAdapter.getItem(position);

                mNavigationCallback.launchAccountFragment(account.getAccountId(), account.getAccountName(), position);
            }
        });

        notifyAccountDataChanged();

        mAccountsRecyclerView.setAdapter(mAccountAdapter);
    }

    private void notifyAccountDataChanged() {
        refreshTotalBalanceView();
        refreshAccountMessage();
    }

    private void refreshTotalBalanceView() {
        BigDecimal totalBalance = mAccountAdapter.getAccountsBalance();

        if (totalBalance.compareTo(BigDecimal.ZERO) < 0) {
            mTotalBalanceTextView.setTextColor(ContextCompat.getColor(getContext(), R.color.negative_red));
        }

        mTotalBalanceTextView.setText(DecimalFormat.getCurrencyInstance().format(totalBalance.doubleValue()));
    }

    private void refreshAccountMessage() {
        if (mAccountAdapter.getItemCount() == 0) {
            mTotalBalanceView.setVisibility(View.GONE);
            mAccountsRecyclerView.setVisibility(View.GONE);
            mAddAccountMessage.setVisibility(View.VISIBLE);
        } else {
            mTotalBalanceView.setVisibility(View.VISIBLE);
            mAccountsRecyclerView.setVisibility(View.VISIBLE);
            mAddAccountMessage.setVisibility(View.GONE);
        }
    }

    public void onAccountDeleted(int position) {
        mAccountAdapter.removeAccount(position);

        notifyAccountDataChanged();
    }

    public void onAccountUpdated(int position, Account account) {
        mAccountAdapter.updateAccount(account, position);

        notifyAccountDataChanged();
    }

    public void onAccountAdded(Account account) {
        mAccountAdapter.addAccount(account);

        notifyAccountDataChanged();
    }

    public interface AccountListNavigationCallbacks {
        void launchAccountFragment(long accountId, String accountName, int accountPosition);
        void launchAddEditAccount(boolean isEdit, long accountId, int accountPosition);
    }
}
