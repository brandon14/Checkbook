package com.brandon14.checkbook.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.brandon14.checkbook.R;
import com.brandon14.checkbook.model.Account;
import com.brandon14.checkbook.model.Transaction;
import com.brandon14.checkbook.model.database.Checkbook;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by brandon on 2/13/15.
 */
public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.ViewHolder> {
    private static final String LOG_TAG = "TransactionAdapter";

    private ArrayList<Transaction> mTransactionList;
    private Context mContext;

    private OnViewClickListener mViewClickListener;

    public TransactionAdapter(Context context, ArrayList<Transaction> entries) {
        mContext = context.getApplicationContext();
        mTransactionList = entries;
    }

    @Override
    public TransactionAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater mInflater = LayoutInflater.from(parent.getContext());
        final View v = mInflater.inflate(R.layout.transaction_recyclerview_layout, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        final Transaction transaction = mTransactionList.get(position);

        BigDecimal transactionAmount = transaction.getAmount();
        String transactionPayee = Checkbook.getInstance(mContext).getPayeeName(transaction.getPayee());
        String transactionDate = new SimpleDateFormat(Checkbook.SHORT_DATE_FORMAT, mContext.getResources().getConfiguration().locale).format(transaction.getDate());

        viewHolder.mTransactionPayeeView.setText(transactionPayee);
        viewHolder.mTransactionAmountView.setText(DecimalFormat.getCurrencyInstance().format(transactionAmount.doubleValue()));
        viewHolder.mTransactionDateView.setText(transactionDate);

        try {
            if (transactionAmount.compareTo(BigDecimal.ZERO) < 0) {
                viewHolder.mTransactionAmountView.setTextColor(ContextCompat.getColor(mContext, R.color.negative_red));
            }
        } catch(NumberFormatException e) {
            Log.e(LOG_TAG, e.getMessage());
        }

    }

    public void setTransactionEntries(ArrayList<Transaction> entries) {
        mTransactionList = entries;

        this.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mTransactionList.size();
    }

    public Transaction getItem(int position) {
        return mTransactionList.get(position);
    }

    public BigDecimal getAccountBalance() {
        BigDecimal accountsBalance = BigDecimal.ZERO;

        // Do database logic to get account balance

        return accountsBalance;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener, View.OnClickListener {
        TextView mTransactionPayeeView;
        TextView mTransactionAmountView;
        TextView mTransactionDateView;

        public ViewHolder(View v) {
            super(v);

            mTransactionPayeeView = (TextView) v.findViewById(R.id.textview_transaction_listview_transaction_payee);
            mTransactionAmountView = (TextView) v.findViewById(R.id.textview_transaction_listview_transaction_amount);
            mTransactionDateView = (TextView) v.findViewById(R.id.textview_transaction_listview_transaction_date);

            v.setOnClickListener(this);
            v.setOnLongClickListener(this);
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
