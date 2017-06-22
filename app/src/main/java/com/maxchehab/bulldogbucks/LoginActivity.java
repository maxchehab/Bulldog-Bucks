package com.maxchehab.bulldogbucks;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import android.content.Intent;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.Bind;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;

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
        if(sharedPref.contains("pin") && sharedPref.contains("userID")){
            String pin = sharedPref.getString("pin", null);
            String userID = sharedPref.getString("userID", null);

            loginRequest(userID,pin);
        }




        
        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });
    }

    public void login() {
        Log.d(TAG, "Login");

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
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();


        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("pin", pin)
                .addFormDataPart("userID",userID)
                .build();

        Request request = new Request.Builder()
                .url("http://67.204.152.242/bulldogbucks/authenticate.php")
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
                        progressDialog.dismiss();
                        onLoginFailed();
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                String data = response.body().string();
                Log.d("Response", data );

                JsonParser jp = new JsonParser(); //from gson
                JsonElement root = jp.parse(data); //Convert the input stream to a json element
                JsonObject rootobj = root.getAsJsonObject();
                if (rootobj.get("success").getAsBoolean()) {
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

                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();
                            onLoginFailed();
                        }
                    });
                }

            }
        });
    }



        /*RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response", response);

                        JsonParser jp = new JsonParser(); //from gson
                        JsonElement root = jp.parse(response); //Convert the input stream to a json element

                        JsonObject rootobj = root.getAsJsonObject(); //May be an array, may be an object.

                        if(rootobj.get("success").getAsBoolean() == true) {

                            if(_remember.isChecked()){
                                SharedPreferences sharedPref = getSharedPreferences("data", MODE_PRIVATE);

                                SharedPreferences.Editor editor = sharedPref.edit();
                                editor.putString("pin", pin);
                                editor.putString("userID", userID);
                                editor.commit();
                            }


                            progressDialog.dismiss();
                            onLoginSuccess();
                        }else{
                            progressDialog.dismiss();
                            onLoginFailed();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        onLoginFailed();
                        Log.d("Error.Response", error.toString());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("pin", pin);
                params.put("userID", userID);

                return params;
            }
        };
        queue.add(postRequest);*/



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

        startActivity(intent);
        finish();
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

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
