package com.maxchehab.bulldogbucks;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;

public class UpdateLargeWidgetService extends Service {

    @Override
    public void onStart(Intent intent, int startId) {
        RemoteViews remoteViews = new RemoteViews(this.getApplicationContext().getPackageName(),R.layout.balance_widget_large);
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this.getApplicationContext());
        String balance = "CheckLogin";
        String updateTime = "Please select 'Remember me'";
        int[] allWidgetIds = intent
                .getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS);

        SharedPreferences sharedPref = getSharedPreferences("data", MODE_PRIVATE);

        for (final int widgetId : allWidgetIds) {
            Log.d("widget-debug","checking");

            if(sharedPref.contains("pin") && sharedPref.contains("userID") && sharedPref.contains("balance") && sharedPref.contains("updateTime")) {
                balance = sharedPref.getString("balance", null);
                updateTime = "Last updated at " + sharedPref.getString("updateTime", null);
            }

            remoteViews.setTextViewText(R.id.balanceText,balance);
            remoteViews.setTextViewText(R.id.balanceDesc,updateTime);



            Intent clickIntent = new Intent("android.intent.action.MAIN");
            clickIntent.addCategory("android.intent.category.LAUNCHER");

            clickIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            clickIntent.setComponent(new ComponentName(getApplicationContext().getPackageName(), LoginActivity.class.getName()));
            PendingIntent pendingIntent = PendingIntent.getActivity(
                    getApplicationContext(), 0, clickIntent, 0);
            remoteViews.setOnClickPendingIntent(R.id.widgetLayout, pendingIntent);
            appWidgetManager.updateAppWidget(widgetId, remoteViews);




        }
        stopSelf();

        super.onStart(intent, startId);
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
