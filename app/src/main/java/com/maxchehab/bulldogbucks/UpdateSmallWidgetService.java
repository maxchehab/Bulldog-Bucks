package com.maxchehab.bulldogbucks;

import java.io.IOException;
import java.util.Random;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UpdateSmallWidgetService extends Service {

    @Override
    public void onStart(Intent intent, int startId) {
        RemoteViews remoteViews = new RemoteViews(this.getApplicationContext().getPackageName(),R.layout.balance_widget_small);
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
                updateTime = "Updated at " + sharedPref.getString("updateTime", null);
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
