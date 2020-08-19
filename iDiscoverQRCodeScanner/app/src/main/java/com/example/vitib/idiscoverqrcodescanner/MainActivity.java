package com.example.vitib.idiscoverqrcodescanner;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.Manifest;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ZXingScannerView scannerView;
    Button btnScan;
    //Button btn_W;
    //ImageView check, notCheck;
    ListView lv;
    ProgressBar pb;
    TextView tv;
    MenuItem itemSubscription, itemCheckIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnScan = (Button) findViewById(R.id.button2);
        //check = (ImageView) findViewById(R.id.check);
        //notCheck = (ImageView) findViewById(R.id.notcheck);
        //btn_W = (Button) findViewById(R.id.button3);
        lv = (ListView) findViewById(R.id.elv);
        pb = (ProgressBar) findViewById(R.id.progressBar2);
        tv = (TextView) findViewById(R.id.textView5);

        pb.setVisibility(View.VISIBLE);
        btnScan.setVisibility(View.INVISIBLE);
        //btn_W.setVisibility(View.INVISIBLE);
        lv.setVisibility(View.INVISIBLE);
        tv.setVisibility(View.INVISIBLE);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        CollectionReference col = db.collection("Workshops");

        final ArrayList<Workshop> list = new ArrayList<>();

        final Context ctx = this;

        col.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    //doc é um workshop
                    for (DocumentSnapshot doc : task.getResult()) {
                        list.add(new Workshop(doc.getId(), doc.getData().get("Nome").toString()));
                    }
                    Log.d("List", "Size: " + list.size());
                    final ArrayAdapter<Workshop> adapter = new ArrayAdapter<Workshop>(ctx, android.R.layout.simple_list_item_1, list);
                    lv.setAdapter(adapter);
                    pb.setVisibility(View.INVISIBLE);
                    btnScan.setVisibility(View.VISIBLE);
                    //btn_W.setVisibility(View.VISIBLE);
                    lv.setVisibility(View.VISIBLE);
                    tv.setVisibility(View.VISIBLE);
                }
            }
        });

        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanCode(v);
            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                final Workshop item = (Workshop) parent.getItemAtPosition(position);
                scannerView = new ZXingScannerView(getBaseContext());
                scannerView.setResultHandler(new ZXingScannerHandler2(item));

                setContentView(scannerView);
                scannerView.startCamera();
            }

        });


        /*btn_W.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scannerView = new ZXingScannerView(getBaseContext());
                scannerView.setResultHandler(new ZXingScannerHandler2());

                setContentView(scannerView);
                scannerView.startCamera();
            }
        });*/

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 0);
        }

    }

    public void scanCode(View view) {
        scannerView = new ZXingScannerView(this);
        scannerView.setResultHandler(new ZXingScannerHandler());

        setContentView(scannerView);
        scannerView.startCamera();
    }

    /*@Override
    protected void onPause() {
        super.onPause();
        scannerView.stopCamera();
    }
*/
    class ZXingScannerHandler implements ZXingScannerView.ResultHandler
    {
        @Override
        public void handleResult(final Result result) {
            final String resultCode = result.getText();

            FirebaseFirestore db = FirebaseFirestore.getInstance();

            CollectionReference col = db.collection("Users");
            col.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot document : task.getResult()) {
                            Log.d("TAG", document.getId() + " => " + document.getData());
                            if (resultCode.equals(document.getData().get("codigo"))) {
                                FirebaseFirestore db = FirebaseFirestore.getInstance();
                                CollectionReference checkins = db.collection("CheckIns");

                                checkins.document(document.getId()).set(new User(document.getData().get("Nome").toString(), document.getData().get("Idade").toString(), document.getId()));

                                Intent intent = new Intent(MainActivity.this, ActivityProfile.class);
                                intent.putExtra("userName", document.getData().get("Nome").toString());
                                intent.putExtra("userEmail", document.getId());
                                startActivity(intent);
                                return;
                            }
                        }
                        Toast.makeText(MainActivity.this, "Permission denied!", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.d("TAG", "Error getting documents: ", task.getException());
                    }
                }
            });
        }
    }

    class ZXingScannerHandler2 implements ZXingScannerView.ResultHandler
    {

        Workshop workshop;

        public ZXingScannerHandler2(Workshop workshop) {
            this.workshop = workshop;
        }

        @Override
        public void handleResult(final Result result) {
            final String resultCode = result.getText();

            final FirebaseFirestore db = FirebaseFirestore.getInstance();

            CollectionReference col = db.collection("Users");
            col.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        //Users/ana@ua.pt - document
                        for (final DocumentSnapshot document : task.getResult()) {
                            Log.d("TAG", document.getId() + " => " + document.getData());
                            //Se o QR Code coincidir com o código do user document
                            if (resultCode.equals(document.getData().get("codigo"))) {

                                CollectionReference col = db.collection("Workshops").document(workshop.getId()).collection("Inscritos");

                                col.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            //doc -> /Workshops/lshflksfhsjhfd/Inscritos/ana@ua.pt
                                            for (DocumentSnapshot doc : task.getResult()) {
                                                if(doc.getId().equals(document.getId())){
                                                    Intent intent = new Intent(MainActivity.this, ActivityWorkshop.class);
                                                    intent.putExtra("workshopN", workshop.toString());
                                                    intent.putExtra("userEmail", doc.getId());
                                                    intent.putExtra("timestamp", doc.getData().get("Data").toString());
                                                    startActivity(intent);
                                                    return;
                                                }
                                            }
                                            Toast.makeText(MainActivity.this, "Permission denied!", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        }

                    } else {
                        Log.d("TAG", "Error getting documents: ", task.getException());
                    }
                }
            });
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        btnScan.setVisibility(View.VISIBLE);
        //notCheck.setVisibility(View.INVISIBLE);
        //check.setVisibility(View.INVISIBLE);
        //btn_W.setVisibility(View.VISIBLE);
        lv.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_navigation, menu);
        itemCheckIn = (MenuItem) menu.findItem(R.id.btnlistCheckin);
        itemSubscription = (MenuItem) menu.findItem(R.id.btnlistSubscription);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d("Item ID ", item.getTitle().toString());

        if(item.getItemId() == R.id.btnlistCheckin) {
            Intent intent = new Intent(MainActivity.this, ListCheckIns.class);
            startActivity(intent);
        } else if(item.getItemId() == R.id.btnlistSubscription) {
            Intent intent = new Intent(MainActivity.this, LIstSubscriptions.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
