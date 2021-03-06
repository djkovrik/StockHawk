package com.udacity.stockhawk.ui.graph;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.udacity.stockhawk.utils.StringUtils;

public class MyXAxisFormatter implements IAxisValueFormatter {

    @Override
    public String getFormattedValue(float v, AxisBase axisBase) {
        return StringUtils.getDateFromFloat(v);
    }
}
