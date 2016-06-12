package com.brandon14.checkbook.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.brandon14.checkbook.R;
import com.brandon14.checkbook.model.Transaction;
import com.brandon14.checkbook.model.database.Checkbook;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by brandon on 2/13/15.
 */
public class TransactionAdapter extends BaseAdapter {
    private ArrayList<Transaction> mTransactionEntries;
    private LayoutInflater mInflater;
    private Context mContext;

    public TransactionAdapter(Context context, ArrayList<Transaction> entries) {
        mContext = context.getApplicationContext();
        mTransactionEntries = entries;
        mInflater = LayoutInflater.from(context);
    }

    public void setTransactionEntries(ArrayList<Transaction> entries) {
        mTransactionEntries = entries;
    }

    public static class TransactionViewHolder {
        TextView transactionPayeeView;
        TextView transactionAmountView;
        TextView transactionDateView;
    }

    /**
     * How many items are in the data set represented by this Adapter.
     *
     * @return Count of items.
     */
    @Override
    public int getCount() {
        return mTransactionEntries.size();
    }

    /**
     * Get the data item associated with the specified position in the data set.
     *
     * @param position Position of the item whose data we want within the adapter's
     *                 data set.
     * @return The data at the specified position.
     */
    @Override
    public Object getItem(int position) {
        return mTransactionEntries.get(position);
    }

    /**
     * Get the row id associated with the specified position in the list.
     *
     * @param position The position of the item within the adapter's data set whose row id we want.
     * @return The id of the item at the specified position.
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * Get a View that displays the data at the specified position in the data set. You can either
     * create a View manually or inflate it from an XML layout file. When the View is inflated, the
     * parent View (GridView, ListView...) will apply default layout parameters unless you use
     * {@link android.view.LayoutInflater#inflate(int, android.view.ViewGroup, boolean)}
     * to specify a root view and to prevent attachment to the root.
     *
     * @param position    The position of the item within the adapter's data set of the item whose view
     *                    we want.
     * @param convertView The old view to reuse, if possible. Note: You should check that this view
     *                    is non-null and of an appropriate type before using. If it is not possible to convert
     *                    this view to display the correct data, this method can create a new view.
     *                    Heterogeneous lists can specify their number of view types, so that this View is
     *                    always of the right type (see {@link #getViewTypeCount()} and
     *                    {@link #getItemViewType(int)}).
     * @param parent      The parent that this view will eventually be attached to
     * @return A View corresponding to the data at the specified position.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TransactionViewHolder viewHolder;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.transaction_listview_layout, parent, false);

            viewHolder = new TransactionViewHolder();
            viewHolder.transactionPayeeView = (TextView) convertView.findViewById(
                    R.id.textview_transaction_listview_transaction_payee);
            viewHolder.transactionAmountView = (TextView) convertView.findViewById(
                    R.id.textview_transaction_listview_transaction_amount);
            viewHolder.transactionDateView = (TextView) convertView.findViewById(
                    R.id.textview_transaction_listview_transaction_date);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (TransactionViewHolder) convertView.getTag();
        }

        Transaction trans = mTransactionEntries.get(position);

        BigDecimal amount = trans.getAmount();

        String transactionPayee = Checkbook.getInstance(mContext).getPayeeName(trans.getPayee());
        String transactionAmount = DecimalFormat.getCurrencyInstance().format(amount.doubleValue());

        String transactionDate = new SimpleDateFormat(Checkbook.SHORT_DATE_FORMAT, mContext.getResources().getConfiguration().locale).format(trans.getDate());

        viewHolder.transactionPayeeView.setText(transactionPayee);
        viewHolder.transactionAmountView.setText(transactionAmount);
        viewHolder.transactionDateView.setText(transactionDate);

        return convertView;
    }
}
