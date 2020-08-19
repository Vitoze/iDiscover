package ua.cm.idiscover.BeginMenu;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import ua.cm.idiscover.Forum.ChanelActivity;
import ua.cm.idiscover.Forum.CreateChanel;
import ua.cm.idiscover.R;

/**
 * Created by vitib on 19/03/2018.
 */

public class ForumActivity extends AppCompatActivity {

    final FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum);

        FloatingActionButton addChanel = (FloatingActionButton) findViewById(R.id.addChanel);

        final ArrayList<String> arraylist = new ArrayList<String>();
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.list_chanel, R.id.c_titulo, arraylist);

        CollectionReference canais = db.collection("Canais");

        canais.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (DocumentSnapshot document : task.getResult()) {
                    Log.d("Canais", document.getId() + " -> " + document.getData());
                    arraylist.add(document.getId());
                }
                Log.d("ArrayList", arraylist.toString());

                ListView list = findViewById(R.id.listchanels);
                list.setAdapter(adapter);
            }
        });

        addChanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ForumActivity.this, CreateChanel.class);
                startActivity(intent);
            }
        });

        ListView list = findViewById(R.id.listchanels);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ForumActivity.this, ChanelActivity.class);
                ListView list = findViewById(R.id.listchanels);
                intent.putExtra("item", list.getItemAtPosition(position).toString());
                startActivity(intent);
            }
        });

    }
}
