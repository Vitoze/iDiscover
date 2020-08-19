package com.example.vitib.idiscoverqrcodescanner;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ListCheckIns extends AppCompatActivity {

    final FirebaseFirestore db = FirebaseFirestore.getInstance();

    TextView nchecks;
    ListView listchecks;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listcheckin);

        nchecks = (TextView) findViewById(R.id.nCheckins);
        listchecks = (ListView) findViewById(R.id.listCheckins);

        final ArrayList<User> arraylist = new ArrayList<User>();
        final ListItemAdapter adapter = new ListItemAdapter(this, arraylist);

        CollectionReference checkins = db.collection("CheckIns");

        checkins.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            Integer count = 0;
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (DocumentSnapshot document : task.getResult()) {
                    if(!task.getResult().isEmpty()) {
                        Log.d("Users", document.getId() + " -> " + document.getData());
                        arraylist.add(new User(document.getData().get("username").toString(), document.getData().get("userage").toString().split(" ")[1], document.getId()));
                        count++;
                    }
                }
                Log.d("ArrayList", arraylist.toString());
                listchecks.setAdapter(adapter);
                nchecks.setText(count.toString());
            }
        });
    }
}
