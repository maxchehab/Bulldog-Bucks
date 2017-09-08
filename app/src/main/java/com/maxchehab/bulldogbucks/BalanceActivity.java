package com.maxchehab.bulldogbucks;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip;

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
    @Bind(R.id.freezeCard) Switch _freezeCard;

    @Bind(R.id.logoImage) ImageView _logoImage;


    @Bind(R.id.logoutLayout) LinearLayout _logoutLayout;
    @Bind(R.id.logoutIcon) ImageView _logoutIcon;
    @Bind(R.id.logoutText) TextView _logoutText;


    @Bind(R.id.balanceText) TextView _balanceText;
    @Bind(R.id.balanceDesc) TextView _balanceDesc;

    @Bind(R.id.mealPlan) TextView _mealPlan;

    @Bind(R.id.transaction_parent) LinearLayout _transactionsParent;


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


        /*swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                updateBalance();
            }
        });
        swipeContainer.setColorSchemeResources(R.color.primary);*/


        _freezeCardInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    new SimpleTooltip.Builder(v.getContext())
                            .anchorView(v)
                            .text("If you cannot locate your Zagcard you can freeze it. This action will protect all available Bulldog Bucks and Meal Plan swipes.")
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


        new FreezeCard(new OnFreezeCardListener() {
            @Override
            public void onFailure(String error) {
                Log.d("Freeze Response", error);

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
            public void onSuccess() {
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
            }
        }).execute(new Credential(userID,pin,action.equals("freeze")));
    }

    void updateBalance(){
        ConnectivityManager cm = (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnected();

        if(!isConnected) {
            Toast.makeText(getBaseContext(), "Could not update information.\nNo internet connection.", Toast.LENGTH_LONG).show();
            return;
        }

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

        new GetUserData(new OnUserDataListener() {
            @Override
            public void onFailure(String error) {
                Log.d("Response", error);

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
            public void onSuccess(final UserData userData) {
                final DecimalFormat decimalFormat = new DecimalFormat("#.00");
                final String balance = "$" + decimalFormat.format(userData.getBalance());
                Log.d("BalanceActivity", "balance: " + userData.getBalance());
                final boolean frozen = userData.getFrozen();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        _balanceText.setText(balance);

                        _freezeCard.setChecked(frozen);
                        if (frozen) {
                            _freezeCardText.setText("Unfreeze card");
                        } else {
                            _freezeCardText.setText("Freeze card");
                        }

                        if(userData.getSwipeType().contains("Platinum")){
                            _mealPlan.setText("Unlimited meal swipes");
                          //  _mealPlan.setHighlightColor();
                        }else if(userData.getSwipeType().contains("Gold")){
                            _mealPlan.setText("16 swipes per week");
                        }else if(userData.getSwipeType().contains("Silver")){
                            _mealPlan.setText("12 swipes per week");
                        }else if(userData.getSwipeType().contains("Blue")){
                            _mealPlan.setText(userData.getSwipes() + " available meal swipes");
                        }else if(userData.getSwipeType().contains("White")){
                            _mealPlan.setText(userData.getSwipes() + " available meal swipes");
                        }



                        _transactionsParent.removeAllViews();
                        LayoutInflater inflater = (LayoutInflater)      getSystemService(LAYOUT_INFLATER_SERVICE);
                        for (Transaction transaction : userData.getTransactions()) {
                            View transactionView = inflater.inflate(R.layout.layout_transaction,null);
                            TextView amount = (TextView)transactionView.findViewById(R.id.transactionAmount);
                            TextView location = (TextView) transactionView.findViewById(R.id.transaction_location);
                            ImageView up = (ImageView) transactionView.findViewById(R.id.up);
                            ImageView down = (ImageView) transactionView.findViewById(R.id.down);

                            amount.setText("$" + decimalFormat.format(transaction.getAmount()));
                            location.setText(ellipsize(transaction.getLocation(),15));

                            if(!transaction.getType().equalsIgnoreCase("SALE")){
                                amount.setTextColor(Color.parseColor("#5fba7d"));
                                up.setVisibility(View.VISIBLE);

                            }else{
                                amount.setTextColor(Color.parseColor("#b70101"));
                                down.setVisibility(View.VISIBLE);
                            }

                            _transactionsParent.addView(transactionView);
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
            }
        }).execute(new Credential(userID,pin));
        //swipeContainer.setRefreshing(false);
    }

    private final static String NON_THIN = "[^iIl1\\.,']";

    private static int textWidth(String str) {
        return (int) (str.length() - str.replaceAll(NON_THIN, "").length() / 2);
    }

    public static String ellipsize(String text, int max) {
       if(text.length() > 15){
           if(text.charAt(14) == ' '){
               return text.substring(0,14) + "...";
           }
           return text.substring(0,15) + "...";
       }
       return text;
    }
}
