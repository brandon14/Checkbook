package com.brandon14.checkbook.widgets;

import android.app.DatePickerDialog;
import android.content.Context;
import android.widget.DatePicker;

import java.util.Calendar;

/**
 * Created by brandon on 2/10/15.
 */
public class LimitedRangeDatePickerDialog extends DatePickerDialog {
    private Calendar mMinDate;
    private Calendar mMaxDate;
    private java.text.DateFormat mTitleDateFormat;

    public LimitedRangeDatePickerDialog (Context context, DatePickerDialog.OnDateSetListener callBack,
                                         int year, int monthOfYear, int dayOfMonth, Calendar minDate, Calendar maxDate) {
        super(context, callBack, year, monthOfYear, dayOfMonth);

        this.mMinDate = minDate;
        this.mMaxDate = maxDate;

        mTitleDateFormat = java.text.DateFormat.getDateInstance(java.text.DateFormat.FULL);
    }

    public void onDateChanged (DatePicker view, int year, int month, int day) {
        Calendar newDate=Calendar.getInstance();
        newDate.set(year, month,day);

        if(mMinDate != null && mMinDate.after(newDate)) {
            view.init(mMinDate.get(Calendar.YEAR), mMinDate.get(Calendar.MONTH),
                    mMinDate.get(Calendar.DAY_OF_MONTH), this);
            setTitle(mTitleDateFormat.format(mMinDate.getTime()));
        }
        else if(mMaxDate != null && mMaxDate.before(newDate)) {
            view.init(mMaxDate.get(Calendar.YEAR), mMaxDate.get(Calendar.MONTH),
                    mMaxDate.get(Calendar.DAY_OF_MONTH), this);
            setTitle(mTitleDateFormat.format(mMaxDate.getTime()));
        }
        else {
            view.init(year, month, day, this);
            setTitle(mTitleDateFormat.format(newDate.getTime()));
        }
    }
}
