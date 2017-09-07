package com.maxchehab.bulldogbucks;

import android.app.ProgressDialog;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;


import butterknife.ButterKnife;
import butterknife.Bind;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";

    @Bind(R.id.input_userID) EditText _userIdtext;
    @Bind(R.id.input_PIN) EditText _pinText;
    @Bind(R.id.btn_login) Button _loginButton;
    @Bind(R.id.input_remember) CheckBox _remember;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);


        SharedPreferences sharedPref = getSharedPreferences("data", MODE_PRIVATE);

        ConnectivityManager cm = (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnected();

        if(isConnected){
            if(sharedPref.contains("pin") && sharedPref.contains("userID")){
                String pin = sharedPref.getString("pin", null);
                String userID = sharedPref.getString("userID", null);

                loginRequest(userID,pin);
            }
        }else{
            Toast.makeText(getBaseContext(), "No internet connection.", Toast.LENGTH_LONG).show();
            if(sharedPref.contains("pin") && sharedPref.contains("userID")){
                String pin = sharedPref.getString("pin", null);
                String userID = sharedPref.getString("userID", null);

                _pinText.setText(pin);
                _userIdtext.setText(userID);
            }
        }


        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });
    }

    public void login() {
        Log.d(TAG, "CheckLogin");

        if (!validate()) {
            onLoginFailed();
            return;
        }


        final String userID = _userIdtext.getText().toString();
        final String pin = _pinText.getText().toString();

        loginRequest(userID,pin);

    }

    void loginRequest(final String userID, final String pin) {

        _loginButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();


        new CheckLogin(new OnLoginListener(){
            @Override
            public void onFailure(String error){
                Log.d("Response", error);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        onLoginFailed();
                    }
                });
            }

            @Override
            public void onSuccess(){
                if (_remember.isChecked()) {
                    SharedPreferences sharedPref = getSharedPreferences("data", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("pin", pin);
                    editor.putString("userID", userID);
                    editor.commit();
                }else{
                    getBaseContext().getSharedPreferences("data", 0).edit().clear().commit();
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        onLoginSuccess(userID, pin);
                    }
                });
            }
        }).execute(new Credential(userID,pin));
    }




    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess(String userID, String pin) {
        _loginButton.setEnabled(true);
        Intent intent = new Intent(this, BalanceActivity.class);
        intent.putExtra("userID", userID);
        intent.putExtra("pin", pin);

        updateWidgets();

        startActivity(intent);
        finish();
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


    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Authentication failed", Toast.LENGTH_LONG).show();

        _loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String userID = _userIdtext.getText().toString();
        String pin = _pinText.getText().toString();

        if (userID.isEmpty()) {
            _userIdtext.setError("enter a valid User ID");
            valid = false;
        } else {
            _userIdtext.setError(null);
        }

        if (pin.isEmpty()) {
            _pinText.setError("enter a valid PIN");
            valid = false;
        } else {
            _pinText.setError(null);
        }

        return valid;
    }
}
