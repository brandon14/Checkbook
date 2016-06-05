package com.brandon14.checkbook.utilities;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.brandon14.checkbook.R;

import java.lang.ref.WeakReference;
import java.math.BigDecimal;
import java.text.NumberFormat;

/**
 *
 * Created by brandon on 5/18/16.
 *
 * Paritally taken from ToddH's answer on stackoverflow here:
 * http://stackoverflow.com/questions/5107901/better-way-to-format-currency-input-edittext
 * I modified it to only display the numbers without the currency symbol, and also to allow for deletion of the
 * text instead of always staying at 0.00.
 */
public class MoneyTextWatcher implements TextWatcher {
    private final WeakReference<EditText> mEditTextWeakReference;

    private Context mContext;

    public MoneyTextWatcher(Context context, EditText editText) {
        mEditTextWeakReference = new WeakReference<>(editText);

        mContext = context;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable editable) {
        EditText editText = mEditTextWeakReference.get();

        if (editText == null) {
            return;
        }

        String s = editable.toString();

        if (s.isEmpty()) {
            return;
        }

        String zeroString = NumberFormat.getCurrencyInstance().format(BigDecimal.ZERO);
        zeroString = zeroString.substring(0, zeroString.length() - 1);

        if (s.equals(zeroString)) {
            editText.removeTextChangedListener(this);

            String empty = "";

            editText.setText(empty);
            editText.setSelection(empty.length());

            editText.addTextChangedListener(this);

            return;
        }

        editText.removeTextChangedListener(this);

        String cleanString = s.replaceAll("[^0-9]", "");
        BigDecimal parsed = new BigDecimal(cleanString).setScale(2, BigDecimal.ROUND_HALF_UP).divide(new BigDecimal(100), BigDecimal.ROUND_HALF_UP);
        String formatted = String.format(mContext.getResources().getConfiguration().locale, mContext.getResources().getString(R.string.str_amount_input_format), parsed);

        editText.setText(formatted);
        editText.setSelection(formatted.length());

        editText.addTextChangedListener(this);
    }
}
