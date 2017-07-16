package com.maxchehab.bulldogbucks;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.util.Log;
import android.content.Intent;

/**
 * Implementation of App Widget functionality.
 */
public class BalanceWidgetSmall extends AppWidgetProvider {

    static String pin = "update";
    static String userID;


    private static final String LOG = "small-widget";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,int[] appWidgetIds) {

        Log.w(LOG, "onUpdate method called");
        // Get all ids
        ComponentName thisWidget = new ComponentName(context,BalanceWidgetSmall.class);
        int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);

        // Build the intent to call the service
        Intent intent = new Intent(context.getApplicationContext(), UpdateSmallWidgetService.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, allWidgetIds);

        // Update the widgets via the service
        context.startService(intent);
    }
}

