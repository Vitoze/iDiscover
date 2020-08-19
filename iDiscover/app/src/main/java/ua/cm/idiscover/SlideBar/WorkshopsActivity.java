package ua.cm.idiscover.SlideBar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import ua.cm.idiscover.BeginMenu.CustomAdapter;
import ua.cm.idiscover.BeginMenu.Event;
import ua.cm.idiscover.Main.SignInActivity;
import ua.cm.idiscover.R;

/**
 * Created by vitib on 23/03/2018.
 */

public class WorkshopsActivity extends AppCompatActivity /*implements NavigationView.OnNavigationItemSelectedListener*/ {

    final ArrayList<Event> events = new ArrayList<Event>();
    final Activity activity = this;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workshops);


        loadActivity();


    }

    public void refreshListviewAdapter(){
        ListView listView = (ListView) findViewById(R.id.workshopsView);
        WorkshopsAdapter customAdapter = new WorkshopsAdapter(getBaseContext(), this, events);
        listView.setAdapter(customAdapter);
    }

    public void loadActivity(){

        Log.d("Debug", "Activity launched");

        events.clear();

        final ProgressBar pb = (ProgressBar) findViewById(R.id.progressBar3);
        final ListView lv = (ListView) findViewById(R.id.workshopsView);
        final TextView tv = (TextView) findViewById(R.id.textView17);

        pb.setVisibility(View.VISIBLE);
        lv.setVisibility(View.INVISIBLE);
        tv.setVisibility(View.INVISIBLE);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        FirebaseAuth fauth = FirebaseAuth.getInstance();

        final FirebaseUser user = fauth.getCurrentUser();

        CollectionReference workshops = db.collection("Workshops");

        if (user.getEmail() != null) {

            workshops.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        //Lista workshops; document = workshop;
                        for (final DocumentSnapshot c : task.getResult()) {
                            Log.d("TAG", c.getId() + " => " + c.getData());

                            CollectionReference inscritos = c.getReference().collection("Inscritos");

                            inscritos.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        //Lista inscritos; document = inscritos;
                                        for (DocumentSnapshot document : task.getResult()) {
                                            Log.d("TAG", document.getId() + " => " + document.getData());

                                            String pattern = "HH:mm";
                                            SimpleDateFormat format = new SimpleDateFormat(pattern);
                                            Date date = new Date();
                                            try {
                                                //date = format.parse(c.get("Hora").toString());
                                                date = format.parse(c.getData().get("Hora").toString());
                                                //Log.d("FRAG", "Horas: " + date);
                                            } catch (ParseException e) {
                                                e.printStackTrace();
                                            }

                                            if (document.getId().equals(user.getEmail())) {
                                                Log.d("E-mail", "User e-mail: "+user.getEmail());
                                                events.add(new Event(c.getId().toString(), c.getData().get("Tipo").toString(), c.get("Nome").toString(), c.get("Oradores").toString(), date , c.get("Sala").toString(), null));
                                                Log.d("Events", "Size: "+events.size());
                                            }
                                        }

                                        ListView listView = (ListView) findViewById(R.id.workshopsView);
                                        WorkshopsAdapter customAdapter = new WorkshopsAdapter(getBaseContext(),activity, events);
                                        listView.setAdapter(customAdapter);

                                        pb.setVisibility(View.INVISIBLE);
                                        if(events.isEmpty()){
                                            tv.setVisibility(View.VISIBLE);
                                        } else {
                                            lv.setVisibility(View.VISIBLE);
                                        }

                                    }
                                }
                            });

                        }
                    }
                }
            });
        } else {
            Toast.makeText(WorkshopsActivity.this, "Authentication required.",
                    Toast.LENGTH_SHORT).show();
        }

    }
}
