package com.example.vitib.idiscoverqrcodescanner;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class LIstSubscriptions extends AppCompatActivity {

    final FirebaseFirestore db = FirebaseFirestore.getInstance();

    TextView nsubs;
    ListView listsubs;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_subscription);

        nsubs = (TextView) findViewById(R.id.nSubscriptions);
        listsubs = (ListView) findViewById(R.id.listSubscription);

        final ArrayList<User> arraylist = new ArrayList<User>();
        final ListItemAdapter adapter = new ListItemAdapter(this, arraylist);

        CollectionReference users = db.collection("Users");

        users.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            Integer count = 0;
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (DocumentSnapshot document : task.getResult()) {
                    Log.d("Users", document.getId() + " -> " + document.getData());
                    arraylist.add(new User(document.getData().get("Nome").toString(), document.getData().get("Idade").toString(), document.getId()));
                    count++;
                }
                Log.d("ArrayList", arraylist.toString());
                listsubs.setAdapter(adapter);
                nsubs.setText(count.toString());
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(LIstSubscriptions.this, MainActivity.class);
        startActivity(intent);
    }
}
