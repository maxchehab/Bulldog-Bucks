package com.maxchehab.bulldogbucks;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class BalanceActivity extends AppCompatActivity {

    SwipeRefreshLayout swipeContainer;
    @Bind(R.id.balanceText) TextView _balanceText;
    @Bind(R.id.textLogout) TextView _logoutText;
    @Bind(R.id.imageLogout) ImageView _logoutImage;

    String pin;
    String userID;
    boolean paused = false;


    Boolean retry = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balance);
        ButterKnife.bind(this);

        pin = getIntent().getStringExtra("pin");
        userID = getIntent().getStringExtra("userID");

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                updateBalance();
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(R.color.swipe);

        _logoutImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout(true);
            }
        });
        _logoutText.setOnClickListener(new View.OnClickListener() {
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
        //updateBalance();

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
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton("yes", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int whichButton) {
                            getBaseContext().getSharedPreferences("data", 0).edit().clear().commit();
                            Intent intent = new Intent(getBaseContext(), LoginActivity.class);
                            startActivity(intent);
                            finish();
                            Toast.makeText(BalanceActivity.this, "Logged out", Toast.LENGTH_SHORT).show();
                        }})
                    .setNegativeButton("no", null).show();
        }else{
            getBaseContext().getSharedPreferences("data", 0).edit().clear().commit();
            Intent intent = new Intent(getBaseContext(), LoginActivity.class);
            startActivity(intent);
            finish();
            Toast.makeText(BalanceActivity.this, "Logged out", Toast.LENGTH_SHORT).show();
        }


    }

    void updateBalance(){
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
