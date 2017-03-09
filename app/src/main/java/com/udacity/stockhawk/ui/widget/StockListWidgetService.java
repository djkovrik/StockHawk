package com.udacity.stockhawk.ui.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;


public class StockListWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new StockListWidgetFactory(getApplicationContext(), intent);
    }
}
