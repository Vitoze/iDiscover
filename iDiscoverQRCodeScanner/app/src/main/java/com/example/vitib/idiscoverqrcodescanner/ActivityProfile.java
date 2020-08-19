package com.example.vitib.idiscoverqrcodescanner;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ActivityProfile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Intent intent = getIntent();
        String userID = intent.getStringExtra("userName");
        String userMail = intent.getStringExtra("userEmail");

        TextView tv2 = (TextView) findViewById(R.id.textView2);
        tv2.setText(userID);

        TextView tv4 = (TextView) findViewById(R.id.textView4);
        tv4.setText(userMail);

    }
}
