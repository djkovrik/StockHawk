package com.udacity.stockhawk.ui.graph;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;
import com.udacity.stockhawk.R;
import com.udacity.stockhawk.utils.StringUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

@SuppressLint("ViewConstructor")
public class ChartCustomMarkerView extends MarkerView {

    @SuppressWarnings("WeakerAccess")
    @BindView(R.id.chart_marker_view_text)
    TextView markerViewText;

    private MPPointF mOffset;

    @SuppressWarnings("SameParameterValue")
    public ChartCustomMarkerView(Context context, int layoutResource) {
        super(context, layoutResource);
        ButterKnife.bind(this);
    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {

        float rawDate = e.getX();
        float rawPrice = e.getY();

        String date = StringUtils.getMarkerDateFromFloat(rawDate);
        String price = StringUtils.getPrice(rawPrice);

        markerViewText.setText(String.format("%s: %s", date, price));

        super.refreshContent(e, highlight);
    }

    @Override
    public MPPointF getOffset() {

        if(mOffset == null) {
            mOffset = new MPPointF(-(getWidth()), -getHeight());
        }

        return mOffset;
    }
}
