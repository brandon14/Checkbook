package com.brandon14.checkbook.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.brandon14.checkbook.objects.Account;
import com.brandon14.checkbook.R;
import com.brandon14.checkbook.utilities.RippleForegroundListener;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by brandon on 2/13/15.
 */
public class AccountAdapter extends RecyclerView.Adapter<AccountAdapter.ViewHolder> {
    private static final String LOG_TAG = "AccountAdapter";

    private ArrayList<Account> mAccountEntries;
    private AccountAdapterCallbacks mCallbacks;
    private Context mContext;
    private LayoutInflater mInflater;

    public AccountAdapter(Context context, ArrayList<Account> entries) {
        mContext = context;
        mAccountEntries = entries;
        mInflater = LayoutInflater.from(context);
    }

    public void setCallback(AccountAdapterCallbacks callbacks) {
        this.mCallbacks = callbacks;
    }

    public void setAccountEntries(ArrayList<Account> entries) {
        mAccountEntries = entries;

        this.notifyDataSetChanged();
    }

    public void addAccount(Account account) {
        mAccountEntries.add(account);

        this.notifyDataSetChanged();
    }

    public void removeAccount(int position) {
        mAccountEntries.remove(position);

        this.notifyItemRemoved(position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
        private TextView mAccountNameView;
        private TextView mAccountBalanceView;
        private View mView;

        public ViewHolder(View v) {
            super(v);

            mAccountNameView = (TextView) v.findViewById(R.id.textview_account_recyclerview_account_name);
            mAccountBalanceView = (TextView) v.findViewById(R.id.textview_account_recyclerview_account_balance);
            mView = v;

            v.setOnLongClickListener(this);
        }

        @Override
        public boolean onLongClick(View v) {
            return v.showContextMenu();
        }
    }

    @Override
    public AccountAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = mInflater.inflate(R.layout.account_recyclerview_layout, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        final int viewPosition = viewHolder.getAdapterPosition();
        final Account account = mAccountEntries.get(position);

        viewHolder.mView.setOnTouchListener(new RippleForegroundListener(R.id.account_recyclerview_cardview));
        viewHolder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCallbacks != null) {
                    mCallbacks.launchAccountActivity(account, viewPosition);
                } else {
                    Log.e(LOG_TAG, "Must set up callback interface!");
                }
            }
        });

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
        return mAccountEntries.size();
    }

    public Account getItem(int position) {
        return mAccountEntries.get(position);
    }

    public interface AccountAdapterCallbacks {
        void launchAccountActivity(Account account, int position);
    }
}
