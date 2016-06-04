package com.brandon14.checkbook.utilities;

import android.content.res.Resources;

import java.text.DecimalFormatSymbols;
import java.util.Currency;

/**
 * Created by brandon on 5/18/16.
 */
public class NumberUtilities {
    public static String cleanNumber (String s) {
        DecimalFormatSymbols decimalSymbols = new DecimalFormatSymbols(Resources.getSystem().getConfiguration().locale);
        char groupSeperator = decimalSymbols.getGroupingSeparator();

        String currencySymbol = Currency.getInstance(Resources.getSystem().getConfiguration().locale).getSymbol();
        String regEx = "[" + currencySymbol + groupSeperator + "]";

        return s.replaceAll(regEx, "");
    }
}
