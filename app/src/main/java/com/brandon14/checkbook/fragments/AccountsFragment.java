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

import com.brandon14.checkbook.AccountActivity;
import com.brandon14.checkbook.database.Checkbook;
import com.brandon14.checkbook.intentkeys.AccountIntentKeys;
import com.brandon14.checkbook.objects.Account;
import com.brandon14.checkbook.adapters.AccountAdapter;
import com.brandon14.checkbook.AddEditAccount;
import com.brandon14.checkbook.R;
import com.brandon14.checkbook.requests.FragmentRequests;
import com.brandon14.checkbook.resultcodes.AccountResultCodes;
import com.brandon14.checkbook.widgets.ContextMenuRecyclerView;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AccountsFragment#getInstance} factory method to
 * create an instance of this fragment.
 */
public class AccountsFragment extends Fragment {
    /**
     *
     */
    private static final String LOG_TAG = "AccountsFragment";
    private static final String RECYCLER_VIEW_STATE_KEY = "recycler_view_state";

    private static AccountsFragment sFragmentInstance;

    private AccountAdapter mAccountAdapter;
    private LinearLayoutManager mLayoutManager;
    private ContextMenuRecyclerView mAccountsRecyclerView;
    private AppBarLayout mTotalBalanceView;
    private TextView mTotalBalanceTextView;
    private TextView mAddAccountMessage;
    private FloatingActionButton mAddFAB;
    private Parcelable mRecyclerViewState;

    private BigDecimal mTotalBalance;

    /**
     * * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment AccountFragment.
     */
    public static AccountsFragment getInstance() {
        sFragmentInstance = sFragmentInstance == null ? new AccountsFragment() : sFragmentInstance;

        return sFragmentInstance;
    }

    /**
     *
     */
    public AccountsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Indicate that this fragment would like to influence the set of actions in the action bar.
        setHasOptionsMenu(true);

        // Retrieve list state and list/item positions
        if(savedInstanceState != null)
            mRecyclerViewState = savedInstanceState.getParcelable(RECYCLER_VIEW_STATE_KEY);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_accounts, container, false);

        mTotalBalanceTextView = (TextView) rootView
                .findViewById(R.id.text_view_total_balance);
        mTotalBalanceView = (AppBarLayout) rootView.findViewById(R.id.total_balance_app_bar);
        mAddAccountMessage = (TextView) rootView.findViewById(R.id.text_view_add_account_message);

        mAccountsRecyclerView = (ContextMenuRecyclerView) rootView.findViewById(R.id.recycler_view_accounts);

        setUpRecyclerView();

        mAddFAB = (FloatingActionButton) rootView.findViewById(R.id.fab_add_accounts);
        mAddFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddEditAccount.class);
                intent.putExtra(AccountIntentKeys.ARG_IS_EDIT, false);

                startActivityForResult(intent, FragmentRequests.ACCOUNT_FRAGMENT_REQUEST);
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

        Activity activity = getActivity();
        activity.setTitle(getResources().getString(R.string.title_accounts));
    }

    @Override
    public void onResume() {
        super.onResume();

        if (mRecyclerViewState != null) {
            mLayoutManager.onRestoreInstanceState(mRecyclerViewState);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);

        mRecyclerViewState = mLayoutManager.onSaveInstanceState();
        state.putParcelable(RECYCLER_VIEW_STATE_KEY, mRecyclerViewState);
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
                                if (!Checkbook.getInstance().deleteAccount(selectedAccount.getAccountId())) {
                                    Snackbar snackbar = Snackbar.make(getActivity().findViewById(R.id.accounts_fragment_coordinator),
                                            getResources().getString(R.string.str_error_deleting_account), Snackbar.LENGTH_LONG);
                                    snackbar.show();
                                } else {
                                    mAccountAdapter.removeAccount(info.position);

                                    refreshAccountMessage();
                                    refreshTotalBalanceView();
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

                Intent intent = new Intent(getActivity(), AddEditAccount.class);
                intent.putExtra(AccountIntentKeys.ARG_IS_EDIT, true);
                intent.putExtra(AccountIntentKeys.ARG_ACCOUNT_TITLE, selectedAccount.getAccountName());
                intent.putExtra(AccountIntentKeys.ARG_ACCOUNT_ID, selectedAccount.getAccountId());
                intent.putExtra(AccountIntentKeys.ARG_ACCOUNT_POSITION, info.position);

                startActivityForResult(intent, FragmentRequests.ACCOUNT_FRAGMENT_REQUEST);

                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == AccountResultCodes.ACCOUNT_CREATED) {
            Account account = (Account) data.getSerializableExtra(AccountIntentKeys.ARG_ACCOUNT_OBJECT);

            mAccountAdapter.addAccount(account);
        } else if (resultCode == AccountResultCodes.ACCOUNT_UPDATED) {
            Account account = (Account) data.getSerializableExtra(AccountIntentKeys.ARG_ACCOUNT_OBJECT);
            int position = data.getIntExtra(AccountIntentKeys.ARG_ACCOUNT_POSITION, -1);

            mAccountAdapter.updateAccount(account, position);
        } else if (resultCode == AccountResultCodes.ACCOUNT_DELETED) {
            int position = data.getIntExtra(AccountIntentKeys.ARG_ACCOUNT_POSITION, -1);

            mAccountAdapter.removeAccount(position);
        }

        refreshAccountMessage();
        refreshTotalBalanceView();
    }

    private void setUpRecyclerView() {
        mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mAccountsRecyclerView.setLayoutManager(mLayoutManager);

        registerForContextMenu(mAccountsRecyclerView);

        mAccountAdapter = new AccountAdapter(getActivity(),
                Checkbook.getInstance().getAccountEntries());
        mAccountAdapter.SetOnViewClickListener(new AccountAdapter.OnViewClickListener() {
            @Override
            public void onViewClick(View v, int position) {
                final Account account = mAccountAdapter.getItem(position);

                Intent intent = new Intent(getActivity(), AccountActivity.class);
                intent.putExtra(AccountIntentKeys.ARG_ACCOUNT_TITLE, account.getAccountName());
                intent.putExtra(AccountIntentKeys.ARG_ACCOUNT_ID, account.getAccountId());
                intent.putExtra(AccountIntentKeys.ARG_ACCOUNT_POSITION, position);

                startActivityForResult(intent, FragmentRequests.ACCOUNT_FRAGMENT_REQUEST);
            }
        });

        refreshAccountMessage();

        mAccountsRecyclerView.setAdapter(mAccountAdapter);
    }

    private void refreshTotalBalanceView() {
        mTotalBalance = mAccountAdapter.getAccountsBalance();

        if (mTotalBalance.compareTo(BigDecimal.ZERO) < 0) {
            mTotalBalanceTextView.setTextColor(ContextCompat.getColor(getContext(), R.color.negative_red));
        }

        mTotalBalanceTextView.setText(DecimalFormat.getCurrencyInstance().format(mTotalBalance.doubleValue()));
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
}
