package com.brandon14.checkbook.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.brandon14.checkbook.model.Account;
import com.brandon14.checkbook.R;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by brandon on 2/13/15.
 */
public class AccountAdapter extends RecyclerView.Adapter<AccountAdapter.ViewHolder> {
    private static final String LOG_TAG = "AccountAdapter";

    private ArrayList<Account> mAccountList;
    private Context mContext;

    private OnViewClickListener mViewClickListener;

    public AccountAdapter(Context context, ArrayList<Account> entries) {
        mContext = context;
        mAccountList = entries;
    }

    @Override
    public AccountAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater mInflater = LayoutInflater.from(mContext);
        final View v = mInflater.inflate(R.layout.account_recyclerview_layout, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        final Account account = mAccountList.get(position);

        String accountName = account.getAccountName();
        BigDecimal accountBalance = account.getCurrentBalance();

        viewHolder.mAccountNameView.setText(accountName);
        viewHolder.mAccountBalanceView.setText(DecimalFormat.getCurrencyInstance().format(accountBalance.doubleValue()));

        try {
            if (accountBalance.compareTo(BigDecimal.ZERO) < 0) {
                viewHolder.mAccountBalanceView.setTextColor(ContextCompat.getColor(mContext, R.color.negative_red));
            }
        } catch(NumberFormatException e) {
            Log.e(LOG_TAG, e.getMessage());
        }
    }

    /**
     * How many items are in the data set represented by this Adapter.
     *
     * @return Count of items.
     */
    @Override
    public int getItemCount() {
        return mAccountList.size();
    }

    // Account methods
    public Account getItem(int position) {
        return mAccountList.get(position);
    }

    public void setAccountEntries(ArrayList<Account> entries) {
        mAccountList = entries;

        this.notifyDataSetChanged();
    }

    public void addAccount(Account account) {
        mAccountList.add(account);

        Collections.sort(mAccountList);

        int position = mAccountList.indexOf(account);

        this.notifyItemInserted(position);
    }

    public void removeAccount(int position) {
        mAccountList.remove(position);

        this.notifyItemRemoved(position);
    }

    public void removeAccount(Account account) {
        int position = mAccountList.indexOf(account);
        mAccountList.remove(position);

        this.notifyItemRemoved(position);
    }

    public void updateAccount(Account account, int position) {
        removeAccount(position);
        addAccount(account);
    }

    public BigDecimal getAccountsBalance() {
        BigDecimal accountsBalance = BigDecimal.ZERO;

        if (mAccountList != null) {
            for (int i = 0; i < mAccountList.size(); i++) {
                try {
                    accountsBalance = accountsBalance.add(mAccountList.get(i).getCurrentBalance());
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
        }

        return accountsBalance;
    }

    // Viewholder
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener, View.OnClickListener {
        private TextView mAccountNameView;
        private TextView mAccountBalanceView;

        public ViewHolder(View v) {
            super(v);

            mAccountNameView = (TextView) v.findViewById(R.id.textview_account_recyclerview_account_name);
            mAccountBalanceView = (TextView) v.findViewById(R.id.textview_account_recyclerview_account_balance);

            v.setOnLongClickListener(this);
            v.setOnClickListener(this);
        }

        @Override
        public boolean onLongClick(View v) {
            return v.showContextMenu();
        }

        @Override
        public void onClick(View v) {
            if (mViewClickListener != null) {
                mViewClickListener.onViewClick(v, getAdapterPosition());
            }
        }
    }

    // Inteferace
    public void SetOnViewClickListener(final OnViewClickListener mViewClickListener) {
        this.mViewClickListener = mViewClickListener;
    }

    public interface OnViewClickListener {
        void onViewClick(View v, int position);
    }
}
