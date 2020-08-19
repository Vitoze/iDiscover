package com.example.vitib.idiscoverqrcodescanner;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ActivityWorkshop extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workshop);

        Intent intent = getIntent();
        String workshop_name = intent.getStringExtra("workshopN");
        String userMail = intent.getStringExtra("userEmail");
        String timestamp = intent.getStringExtra("timestamp");

        TextView tvW = (TextView) findViewById(R.id.workshop_name);
        tvW.setText(workshop_name);

        TextView tv2 = (TextView) findViewById(R.id.textView2);
        tv2.setText(userMail);

        TextView tv4 = (TextView) findViewById(R.id.textView4);
        tv4.setText(timestamp);

    }
}
