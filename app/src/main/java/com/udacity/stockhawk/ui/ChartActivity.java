package com.udacity.stockhawk.ui;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.udacity.stockhawk.R;
import com.udacity.stockhawk.data.Contract;
import com.udacity.stockhawk.ui.graph.MyXAxisFormatter;
import com.udacity.stockhawk.ui.graph.MyYAxisFormatter;
import com.udacity.stockhawk.utils.StringUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChartActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final String STOCK_URI = "URI";

    private static final int STOCK_LOADER = 679;

    private static final String[] STOCK_PROJECTION = {
            Contract.Quote.COLUMN_SYMBOL,
            Contract.Quote.COLUMN_NAME,
            Contract.Quote.COLUMN_PRICE,
            Contract.Quote.COLUMN_HISTORY
    };

    private static final int SYMBOL_COLUMN_ID = 0;
    private static final int NAME_COLUMN_ID = 1;
    private static final int PRICE_COLUMN_ID = 2;
    private static final int HISTORY_COLUMN_ID = 3;

    private Uri mUri;

    @SuppressWarnings("WeakerAccess")
    @BindView(R.id.chart_symbol)
    TextView symbolTextView;

    @SuppressWarnings("WeakerAccess")
    @BindView(R.id.chart_name)
    TextView nameTextView;

    @SuppressWarnings("WeakerAccess")
    @BindView(R.id.chart_price)
    TextView priceTextView;

    @SuppressWarnings("WeakerAccess")
    @BindView(R.id.chart)
    LineChart historyChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(STOCK_URI)) {
            mUri = intent.getParcelableExtra(STOCK_URI);
        }

        getSupportLoaderManager().initLoader(STOCK_LOADER, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, Bundle args) {

        switch (loaderId) {
            case STOCK_LOADER:
                return new CursorLoader(
                        getApplicationContext(),
                        mUri,
                        STOCK_PROJECTION,
                        null, null, null
                );

            default:
                throw new RuntimeException("Loader not implemented: " + loaderId);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data == null || !data.moveToFirst()) {
            return;
        }

        String symbol = data.getString(SYMBOL_COLUMN_ID);
        String name = data.getString(NAME_COLUMN_ID);
        String price = data.getString(PRICE_COLUMN_ID);

        symbolTextView.setText(symbol);
        nameTextView.setText(name);
        priceTextView.setText(price);

        String history = data.getString(HISTORY_COLUMN_ID);

        setupChart(history);
    }

    private void setupChart(String history) {

        List<Entry> chartData = StringUtils.parseHistoryString(history);

        LineDataSet dataSet = new LineDataSet(chartData, "");
        dataSet.setColor(Color.YELLOW);
        dataSet.setLineWidth(2f);
        dataSet.setDrawCircles(false);

        LineData lineData = new LineData(dataSet);
        historyChart.setData(lineData);
        historyChart.setDragEnabled(false);
        historyChart.setTouchEnabled(false);

        Legend legend = historyChart.getLegend();
        legend.setEnabled(false);

        Description description = new Description();
        description.setText("");
        historyChart.setDescription(description);

        YAxis left = historyChart.getAxisLeft();
        left.setValueFormatter(new MyYAxisFormatter());

        YAxis right = historyChart.getAxisRight();
        right.setEnabled(false);

        XAxis top = historyChart.getXAxis();
        top.setValueFormatter(new MyXAxisFormatter());

        historyChart.setContentDescription(getString(R.string.a11y_chart));

        historyChart.invalidate();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
