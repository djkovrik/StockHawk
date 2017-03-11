package com.udacity.stockhawk.utils;

import com.github.mikephil.charting.data.Entry;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class StringUtils {
    private static final DecimalFormat dollarFormatWithPlus;
    private static final DecimalFormat dollarFormat;
    private static final DecimalFormat percentageFormat;
    private static final SimpleDateFormat dateFormatter;

    static {
        dollarFormat = (DecimalFormat) NumberFormat.getCurrencyInstance(Locale.US);

        dollarFormatWithPlus = (DecimalFormat) NumberFormat.getCurrencyInstance(Locale.US);
        dollarFormatWithPlus.setPositivePrefix("+$");

        percentageFormat = (DecimalFormat) NumberFormat.getPercentInstance(Locale.getDefault());
        percentageFormat.setMaximumFractionDigits(2);
        percentageFormat.setMinimumFractionDigits(2);
        percentageFormat.setPositivePrefix("+");

        dateFormatter = new SimpleDateFormat("MMM yy", Locale.US);
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

    public static String getDateFromFloat(float time) {
        return dateFormatter.format(new Date((long)time));
    }

    public static List<Entry> parseHistoryString(String history) {

        List<String> pairs = Arrays.asList(history.split("\\n"));
        Collections.reverse(pairs);

        List<Float> dateValues = new ArrayList<>();
        List<Float> priceValues = new ArrayList<>();

        for (String pair : pairs) {
            String[] entry = pair.split(", ");
            dateValues.add(Float.valueOf(entry[0]));
            priceValues.add(Float.valueOf(entry[1]));
        }

        if (dateValues.size() != priceValues.size()) {
            throw new RuntimeException("History parsing error, list sizes mismatch!");
        }

        List<Entry> entries = new ArrayList<>();

        for (int i = 0; i < dateValues.size(); i++) {
            entries.add(new Entry( dateValues.get(i), priceValues.get(i) ));
        }

        return entries;
    }
}
