package com.brandon14.checkbook.widgets;

import android.annotation.TargetApi;
import android.content.Context;
import android.support.design.widget.TextInputEditText;
import android.util.AttributeSet;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;

/**
 * Created by brandon on 5/20/16.
 */
public class NonSelectableEditText extends TextInputEditText {
    public NonSelectableEditText(Context context) {
        super(context);

        init();
    }

    public NonSelectableEditText(Context context, AttributeSet attrs) {
        super (context, attrs);

        init();
    }

    public NonSelectableEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super (context, attrs, defStyleAttr);

        init();
    }

    @Override
    public void onSelectionChanged(int start, int end) {
        CharSequence text = getText();

        if (text != null) {
            if (start != text.length() || end != text.length()) {
                setSelection(text.length(), text.length());

                return;
            }
        }

        super.onSelectionChanged(start, end);
    }

    private void init()
    {
        this.setCustomSelectionActionModeCallback(new ActionModeCallbackInterceptor());
        this.setLongClickable(false);
    }


    /**
     * Prevents the action bar (top horizontal bar with cut, copy, paste, etc.) from appearing
     * by intercepting the callback that would cause it to be created, and returning false.
     */
    private class ActionModeCallbackInterceptor implements ActionMode.Callback
    {
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            return false;
        }

        public void onDestroyActionMode(ActionMode mode) {

        }
    }
}
