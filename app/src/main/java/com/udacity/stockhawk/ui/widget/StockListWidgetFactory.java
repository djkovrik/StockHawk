package com.udacity.stockhawk.ui.widget;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Binder;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.udacity.stockhawk.R;
import com.udacity.stockhawk.data.Contract;
import com.udacity.stockhawk.ui.MainActivity;
import com.udacity.stockhawk.utils.StringUtils;

import timber.log.Timber;

public class StockListWidgetFactory implements RemoteViewsService.RemoteViewsFactory {

    // Query projection
    private static final String[] STOCK_PROJECTION = {
            Contract.Quote.COLUMN_SYMBOL,
            Contract.Quote.COLUMN_PRICE,
            Contract.Quote.COLUMN_PERCENTAGE_CHANGE
    };

    // Columns indexes
    private static final int INDEX_SYMBOL = 0;
    private static final int INDEX_PRICE = 1;
    private static final int INDEX_PERCENTAGE_CHANGE = 2;

    private Context mContext;
    private Intent mIntent;
    private Cursor mCursor;

    public StockListWidgetFactory(Context mContext, Intent mIntent) {
        this.mContext = mContext;
        this.mIntent = mIntent;
        mCursor = null;
    }


    @Override
    public void onCreate() {
        // Nothing to do
    }

    @Override
    public void onDataSetChanged() {
        if (mCursor != null) {
            mCursor.close();
        }

        final long identityToken = Binder.clearCallingIdentity();

        mCursor = mContext.getContentResolver().query(
                Contract.Quote.URI,
                STOCK_PROJECTION,
                null,
                null,
                Contract.Quote.COLUMN_SYMBOL + " ASC"
        );

        Binder.restoreCallingIdentity(identityToken);

        Timber.d("Cursor size: " + getCount());
    }


    @Override
    public void onDestroy() {
        if (mCursor != null) {
            mCursor.close();
            mCursor = null;
        }
    }

    @Override
    public int getCount() {
        return mCursor == null ? 0 : mCursor.getCount();
    }

    @Override
    public RemoteViews getViewAt(int position) {

        if (mCursor == null || !mCursor.moveToPosition(position)) {
            return null;
        }

        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.widget_stock_list_item);

        String symbol = mCursor.getString(INDEX_SYMBOL);
        float rawPrice = mCursor.getFloat(INDEX_PRICE);
        String price = StringUtils.getPrice(rawPrice);
        float rawChange = mCursor.getFloat(INDEX_PERCENTAGE_CHANGE);
        String percentage = StringUtils.getPercentageChange(rawChange);

        views.setTextViewText(R.id.widget_symbol, symbol);
        views.setTextViewText(R.id.widget_price, price);
        views.setTextViewText(R.id.widget_change, percentage);

        Intent intent = new Intent(mContext, MainActivity.class);
        views.setOnClickFillInIntent(R.id.widget_list_item, intent);

        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return new RemoteViews(mContext.getPackageName(), R.layout.widget_stock_list_item);
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
