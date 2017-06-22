package com.maxchehab.bulldogbucks;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.Bind;

public class BalanceActivity extends AppCompatActivity {

    SwipeRefreshLayout swipeContainer;
    @Bind(R.id.balanceText) TextView _balanceText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balance);

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
    }

    void updateBalance(){
       // swipeContainer.setRefreshing(false);
    }
}
