package com.maxchehab.bulldogbucks;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class BalanceActivity extends AppCompatActivity {

    SwipeRefreshLayout swipeContainer;
    String pin;
    String userID;
    boolean paused = false;
    boolean loading = true;

    ProgressDialog progressDialog;
    ProgressDialog freezeDialog;

    @Bind(R.id.freezeCardInfo) ImageView _freezeCardInfo;
    @Bind(R.id.freezeCardText) TextView _freezeCardText;
    @Bind(R.id.freezeCardLoader) TextView _freezeCardLoader;
    @Bind(R.id.freezeCard) Switch _freezeCard;

    @Bind(R.id.logoLayout) LinearLayout _logoLayout;
    @Bind(R.id.logoImage) ImageView _logoImage;


    @Bind(R.id.logoutLayout) LinearLayout _logoutLayout;
    @Bind(R.id.logoutIcon) ImageView _logoutIcon;
    @Bind(R.id.logoutText) TextView _logoutText;


    @Bind(R.id.balanceText) TextView _balanceText;
    @Bind(R.id.balanceDesc) TextView _balanceDesc;


    String verbage;

    Boolean retry = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balance);
        ButterKnife.bind(this);



        pin = getIntent().getStringExtra("pin");
        userID = getIntent().getStringExtra("userID");


        progressDialog = new ProgressDialog(BalanceActivity.this, R.style.AppTheme_Dark_Dialog);
        freezeDialog = new ProgressDialog(BalanceActivity.this, R.style.AppTheme_Dark_Dialog);


        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                updateBalance();
            }
        });
        swipeContainer.setColorSchemeResources(R.color.primary);


        _freezeCardInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    new SimpleTooltip.Builder(v.getContext())
                            .anchorView(v)
                            .text("If you cannot locate your ZAGCARD you can freeze it. This action will stop your ZAGCARD from working and protect all available Bulldog Bucks and Meal Plan swipes.")
                            .gravity(Gravity.BOTTOM)
                            .animated(false)
                            .transparentOverlay(false)
                            .build()
                            .show();

            }
        });

        _logoutLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout(true);
            }
        });

        _freezeCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!loading){
                    verbage = "Unfreeze";

                    if(_freezeCard.isChecked()){
                        verbage = "Freeze";
                    }
                    new AlertDialog.Builder(v.getContext(),R.style.AlertDialogCustom)
                            .setTitle(verbage + " card")

                            .setMessage("Do you really want to " + verbage.toLowerCase() +  " your card?")
                            .setIcon(R.drawable.ic_warning)
                            .setPositiveButton("yes", new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int whichButton) {
                                    freezeCard(verbage.toLowerCase());
                                }})
                            .setNegativeButton("no", null).show();
                }
                _freezeCard.setChecked(!_freezeCard.isChecked());

            }
        });


        updateBalance();
    }


    @Override
    protected void onResume() {
        SharedPreferences sharedPref = getSharedPreferences("data", MODE_PRIVATE);
        if((!sharedPref.contains("pin") || !sharedPref.contains("userID")) && paused){
            logout(false);
        }else{
            updateBalance();
        }

        super.onResume();
    }
    @Override
    protected  void onPause(){
        paused = true;

        super.onPause();
    }


    void logout(boolean ask) {
        if (ask) {
            new AlertDialog.Builder(this, R.style.AlertDialogCustom)
                    .setTitle("Logout")

                    .setMessage("Do you really want to logout?")
                    .setIcon(R.drawable.ic_warning)
                    .setPositiveButton("yes", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int whichButton) {
                            getBaseContext().getSharedPreferences("data", 0).edit().clear().commit();
                            Intent intent = new Intent(getBaseContext(), LoginActivity.class);
                            updateWidgets();
                            startActivity(intent);

                            finish();
                            Toast.makeText(BalanceActivity.this, "Logged out", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("no", null).show();
        } else {
            getBaseContext().getSharedPreferences("data", 0).edit().clear().commit();
            Intent intent = new Intent(getBaseContext(), LoginActivity.class);
            startActivity(intent);
            finish();
            Toast.makeText(BalanceActivity.this, "Logged out", Toast.LENGTH_SHORT).show();

            updateWidgets();
        }
    }

    private void updateWidgets(){
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getApplicationContext());
        int[] largeAppWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, BalanceWidgetLarge.class));
        if (largeAppWidgetIds.length > 0) {
            new BalanceWidgetLarge().onUpdate(this, appWidgetManager, largeAppWidgetIds);
        }
        int[] smallAppWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, BalanceWidgetSmall.class));
        if (smallAppWidgetIds.length > 0) {
            new BalanceWidgetSmall().onUpdate(this, appWidgetManager, smallAppWidgetIds);
        }
    }

    void freezeCard(final String action){
        loading = true;
        Log.d("freezing",action + ", " + userID + ", " + pin);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                freezeDialog.setCancelable(false);
                freezeDialog.setIndeterminate(true);
                if(action.equals("freeze")){
                    freezeDialog.setMessage("Freezing card....");
                }else{
                    freezeDialog.setMessage("Unfreezing card....");
                }
                freezeDialog.show();
            }
        });

        OkHttpClient client = new OkHttpClient();

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("pin", pin)
                .addFormDataPart("userID",userID)
                .addFormDataPart("action",action)
                .build();

        Request request = new Request.Builder()
                .url("http://104.236.141.69/bulldogbucks/freezeAccount.php")
                .method("POST", RequestBody.create(null, new byte[0]))
                .post(requestBody)
                .build();

        final Gson gson = new Gson();


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("Freeze Response", e.toString());

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("equals","<" + action + ">");
                        if(action.equals("freeze")){
                            Toast.makeText(getBaseContext(), "Freezing card failed", Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(getBaseContext(), "Unfreezing card failed", Toast.LENGTH_LONG).show();
                        }
                        freezeDialog.dismiss();
                        loading = false;
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                String data = response.body().string();
                Log.d("Freeze Response", data );
                try{
                    JsonParser jp = new JsonParser(); //from gson
                    JsonElement root = jp.parse(data); //Convert the input stream to a json element
                    JsonObject rootobj = root.getAsJsonObject();
                    if (rootobj.get("success").getAsBoolean()) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(action.equals("freeze")){
                                    _freezeCardText.setText("Unfreeze card");
                                    _freezeCard.setChecked(true);

                                }else{
                                    _freezeCardText.setText("Freeze card");
                                    _freezeCard.setChecked(false);

                                }
                                loading = false;
                                freezeDialog.dismiss();
                            }
                        });
                    }else{
                        if(action.equals("freeze")){
                            Toast.makeText(getBaseContext(), "Freezing card failed", Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(getBaseContext(), "Unfreezing card failed", Toast.LENGTH_LONG).show();
                        }
                        freezeDialog.dismiss();
                    }
                }catch(Exception e){
                    Log.d("Freeze error", e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(action.equals("freeze")){
                                Toast.makeText(getBaseContext(), "Freezing card failed", Toast.LENGTH_LONG).show();
                            }else{
                                Toast.makeText(getBaseContext(), "Unfreezing card failed", Toast.LENGTH_LONG).show();
                            }
                            loading = false;
                            freezeDialog.dismiss();
                        }
                    });

                }

            }
        });

    }

    void updateBalance(){
        loading = true;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressDialog.setCancelable(false);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Retrieving account information....");
                progressDialog.show();
            }
        });

        OkHttpClient client = new OkHttpClient();

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("pin", pin)
                .addFormDataPart("userID",userID)
                .build();

        Request request = new Request.Builder()
                .url("http://104.236.141.69/bulldogbucks/balance.php")
                .method("POST", RequestBody.create(null, new byte[0]))
                .post(requestBody)
                .build();

        final Gson gson = new Gson();


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("Response", e.toString());

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getBaseContext(), "Retrieving balance failed", Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                        loading = false;
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                String data = response.body().string();
                Log.d("Response", data );
                try{
                    JsonParser jp = new JsonParser(); //from gson
                    JsonElement root = jp.parse(data); //Convert the input stream to a json element
                    JsonObject rootobj = root.getAsJsonObject();
                    if (rootobj.get("success").getAsBoolean()) {
                        final String balance = rootobj.get("balance").getAsString();
                        final boolean frozen = rootobj.get("frozen").getAsBoolean();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                _balanceText.setText(balance);
                                _balanceText.setBackgroundColor(getResources().getColor(R.color.white));
                                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                params.setMargins(0, 0, 0, 0);
                                _balanceText.setTextSize(24);
                                _balanceText.setLayoutParams(params);

                                _balanceDesc.setBackgroundColor(getResources().getColor(R.color.white));
                                _balanceDesc.setTextColor(getResources().getColor(R.color.primary));

                                _freezeCardLoader.setVisibility(View.GONE);
                                _freezeCardInfo.setVisibility(View.VISIBLE);
                                _freezeCardText.setVisibility(View.VISIBLE);
                                _freezeCard.setVisibility(View.VISIBLE);

                                _logoLayout.setBackgroundColor(getResources().getColor(R.color.white));
                                _logoutLayout.setBackgroundColor(getResources().getColor(R.color.white));
                                _logoutIcon.setVisibility(View.VISIBLE);
                                _logoutText.setVisibility(View.VISIBLE);
                                _logoImage.setVisibility(View.VISIBLE);

                                _freezeCard.setChecked(frozen);
                                if(frozen){
                                    _freezeCardText.setText("Unfreeze card");
                                }else{
                                    _freezeCardText.setText("Freeze card");
                                }


                                DateFormat dfTime = new SimpleDateFormat("hh:mm a");
                                String time = dfTime.format(Calendar.getInstance().getTime());

                                SharedPreferences sharedPref = getSharedPreferences("data", MODE_APPEND);
                                SharedPreferences.Editor editor = sharedPref.edit();
                                editor.putString("balance", balance);
                                editor.putString("updateTime", time);
                                editor.commit();
                                updateWidgets();

                                loading = false;
                                progressDialog.dismiss();
                            }
                        });
                    }else{
                        Toast.makeText(getBaseContext(), "Retrieving balance failed", Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                    }
                }catch(Exception e){
                    if(retry){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getBaseContext(), "Retrieving balance failed", Toast.LENGTH_LONG).show();
                            }
                        });
                        retry = false;
                    }else{
                        retry = true;
                        updateBalance();
                    }
                }

            }
        });

        swipeContainer.setRefreshing(false);
    }
}
