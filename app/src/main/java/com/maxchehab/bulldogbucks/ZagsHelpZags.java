package com.maxchehab.bulldogbucks;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ZagsHelpZags extends AppCompatActivity {

    @Bind(R.id.sendData) TextView _sendData;
    @Bind(R.id.zagsData) TextInputEditText _zagsData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zags_help_zags);

        ButterKnife.bind(this);

        _sendData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(_zagsData.getText().length() == 0){
                    Toast.makeText(ZagsHelpZags.this, "Please type a suggestion", Toast.LENGTH_SHORT).show();
                }else{

                    Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
                    emailIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    emailIntent.setType("vnd.android.cursor.item/email");
                    emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[] {"maxchehab@gmail.com"});
                    emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Zags Help Zags");
                    emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, _zagsData.getText());
                    startActivity(Intent.createChooser(emailIntent, "Send suggestion using..."));

                    finish();
                }
            }
        });
    }
}
