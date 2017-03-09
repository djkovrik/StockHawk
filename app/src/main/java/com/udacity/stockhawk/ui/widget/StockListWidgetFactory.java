package com.udacity.stockhawk.ui.widget;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Binder;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.udacity.stockhawk.R;
import com.udacity.stockhawk.data.Contract;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

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

    private final DecimalFormat dollarFormat;
    private final DecimalFormat percentageFormat;

    public StockListWidgetFactory(Context mContext, Intent mIntent) {
        this.mContext = mContext;
        this.mIntent = mIntent;
        mCursor = null;

        dollarFormat = (DecimalFormat) NumberFormat.getCurrencyInstance(Locale.US);
        percentageFormat = (DecimalFormat) NumberFormat.getPercentInstance(Locale.getDefault());
        percentageFormat.setMaximumFractionDigits(2);
        percentageFormat.setMinimumFractionDigits(2);
        percentageFormat.setPositivePrefix("+");
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
        float rawChange = mCursor.getFloat(INDEX_PERCENTAGE_CHANGE);
        String percentage = percentageFormat.format(rawChange / 100);

        views.setTextViewText(R.id.widget_symbol, symbol);
        views.setTextViewText(R.id.widget_price, dollarFormat.format(rawPrice));
        views.setTextViewText(R.id.widget_change, percentage);

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
