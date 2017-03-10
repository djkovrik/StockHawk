package com.udacity.stockhawk.utils;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class StringUtils {
    private static final DecimalFormat dollarFormatWithPlus;
    private static final DecimalFormat dollarFormat;
    private static final DecimalFormat percentageFormat;

    static {
        dollarFormat = (DecimalFormat) NumberFormat.getCurrencyInstance(Locale.US);
        dollarFormatWithPlus = (DecimalFormat) NumberFormat.getCurrencyInstance(Locale.US);
        dollarFormatWithPlus.setPositivePrefix("+$");
        percentageFormat = (DecimalFormat) NumberFormat.getPercentInstance(Locale.getDefault());
        percentageFormat.setMaximumFractionDigits(2);
        percentageFormat.setMinimumFractionDigits(2);
        percentageFormat.setPositivePrefix("+");
    }

    public static String getPrice(float price) {
        return dollarFormat.format(price);
    }

    public static String getSignedPriceChange(float change) {
        return dollarFormatWithPlus.format(change);
    }

    public static String getPercentageChange(float change) {
        return percentageFormat.format(change / 100);
    }
}
