package com.brandon14.checkbook.widgets;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.ContextMenu;
import android.view.View;

/**
 * Created by brandon on 5/28/16.
 * Partially taken from josh2112 and Renaud Cerrato's answer on stackoverflow here:
 * http://stackoverflow.com/questions/26466877/how-to-create-context-menu-for-recyclerview.
 * I updated it to use getChildAdapterPosition since getPosition is depreciated.
 */
public class ContextMenuRecyclerView extends RecyclerView {
    private RecyclerViewContextMenuInfo mContextMenuInfo;

    public ContextMenuRecyclerView(Context context) {
        super(context);
    }

    public ContextMenuRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ContextMenuRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean showContextMenuForChild(View originalView) {
        final int longPressPosition = getChildAdapterPosition(originalView);

        if (longPressPosition >= 0) {
            long longPressId = getAdapter().getItemId(longPressPosition);

            mContextMenuInfo = new RecyclerViewContextMenuInfo(longPressPosition, longPressId);

            return super.showContextMenuForChild(originalView);
        }

        return false;
    }

    @Override
    protected ContextMenu.ContextMenuInfo getContextMenuInfo() {
        return mContextMenuInfo;
    }

    public static class RecyclerViewContextMenuInfo implements ContextMenu.ContextMenuInfo {
        final public int position;
        final public long id;

        public RecyclerViewContextMenuInfo(int position, long id) {
            this.position = position;
            this.id = id;
        }
    }
}
