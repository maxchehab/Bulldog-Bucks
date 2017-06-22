package com.maxchehab.bulldogbucks;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.view.View.GONE;

public class BalanceActivity2 extends AppCompatActivity {

    SwipeRefreshLayout swipeContainer;
    String pin;
    String userID;
    boolean paused = false;

    ProgressDialog progressDialog;

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




    Boolean retry = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balance2);
        ButterKnife.bind(this);



        pin = getIntent().getStringExtra("pin");
        userID = getIntent().getStringExtra("userID");


        progressDialog = new ProgressDialog(BalanceActivity2.this, R.style.AppTheme_Dark_Dialog);

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
                            .text("If you cannot locate your ZAGCARD you can freeze it. This action will stop your ZAGCARD from working and protect all available Bulldog Bucks, and Meal Plan swipes.")
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


    void logout(boolean ask){
        if(ask){
            new AlertDialog.Builder(this,R.style.AlertDialogCustom)
                    .setTitle("Logout")

                    .setMessage("Do you really want to logout?")
                    .setIcon(R.drawable.ic_warning)
                    .setPositiveButton("yes", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int whichButton) {
                            getBaseContext().getSharedPreferences("data", 0).edit().clear().commit();
                            Intent intent = new Intent(getBaseContext(), LoginActivity.class);
                            startActivity(intent);
                            finish();
                            Toast.makeText(BalanceActivity2.this, "Logged out", Toast.LENGTH_SHORT).show();
                        }})
                    .setNegativeButton("no", null).show();
        }else{
            getBaseContext().getSharedPreferences("data", 0).edit().clear().commit();
            Intent intent = new Intent(getBaseContext(), LoginActivity.class);
            startActivity(intent);
            finish();
            Toast.makeText(BalanceActivity2.this, "Logged out", Toast.LENGTH_SHORT).show();
        }


    }

    void updateBalance(){

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
                .url("http://bulldogbucks.maxchehab.com/balance.php")
                .method("POST", RequestBody.create(null, new byte[0]))
                .post(requestBody)
                .build();

        final Gson gson = new Gson();


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("Response", e.toString());

                Toast.makeText(getBaseContext(), "Retrieving balance failed", Toast.LENGTH_LONG).show();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
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

                                progressDialog.dismiss();
                            }
                        });
                    }else{
                        Toast.makeText(getBaseContext(), "Retrieving balance failed", Toast.LENGTH_LONG).show();

                    }
                }catch(Exception e){
                    if(retry){
                        logout(false);
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
