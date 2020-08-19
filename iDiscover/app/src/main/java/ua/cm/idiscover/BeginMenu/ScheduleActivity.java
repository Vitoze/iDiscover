package ua.cm.idiscover.BeginMenu;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
//import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Iterator;

import ua.cm.idiscover.R;

/**
 * Created by vitib on 19/03/2018.
 */

public class ScheduleActivity extends AppCompatActivity {

    ProgressBar pb;
    ViewPager vp;

    private MenuItem itemP;
    private MenuItem itemW;

    PageAdapter adapter = null;
    ViewPager viewPager = null;

    ArrayList<DocumentSnapshot> arrayPal = new ArrayList<>();
    ArrayList<DocumentSnapshot> arrayWork = new ArrayList<>();
    ArrayList<DocumentSnapshot> finalArray = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        pb = (ProgressBar) findViewById(R.id.pbHeaderProgress);
        vp = (ViewPager) findViewById(R.id.pager);

        vp.setVisibility(View.INVISIBLE);

        //getActionBar().hide();

        //final ArrayList<DocumentSnapshot> arrayPal = new ArrayList<>();
        //final ArrayList<DocumentSnapshot> arrayWork = new ArrayList<>();

        final FirebaseFirestore db = FirebaseFirestore.getInstance();

        FirebaseAuth fauth = FirebaseAuth.getInstance();

        Log.d("USER", "User: " + fauth.getCurrentUser());

        //new accessDB().execute();

        final TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("23 Abril"));
        tabLayout.addTab(tabLayout.newTab().setText("24 Abril"));
        tabLayout.addTab(tabLayout.newTab().setText("25 Abril"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        CollectionReference palestras = db.collection("Palestras");

        palestras.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot document : task.getResult()) {
                        Log.d("TAG", document.getId() + " => " + document.getData());
                        arrayPal.add(document);
                    }
                    CollectionReference workshops = db.collection("Workshops");

                    workshops.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (DocumentSnapshot document : task.getResult()) {
                                    Log.d("TAG", document.getId() + " => " + document.getData());
                                    arrayWork.add(document);
                                }

                                Log.d("TAG", "Activity - Palestras: " + arrayPal.size());
                                Log.d("TAG", "Activity - Workshops: " + arrayWork.size());


                                finalArray.addAll(arrayPal);
                                finalArray.addAll(arrayWork);

                                viewPager = (ViewPager) findViewById(R.id.pager);
                                adapter = new PageAdapter
                                        (getSupportFragmentManager(), tabLayout.getTabCount(), finalArray);
                                viewPager.setAdapter(adapter);
                                viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
                                pb.setVisibility(View.INVISIBLE);
                                vp.setVisibility(View.VISIBLE);
                                tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                                    @Override
                                    public void onTabSelected(TabLayout.Tab tab) {
                                        viewPager.setCurrentItem(tab.getPosition());
                                    }

                                    @Override
                                    public void onTabUnselected(TabLayout.Tab tab) {

                                    }

                                    @Override
                                    public void onTabReselected(TabLayout.Tab tab) {

                                    }
                                });
                            } else {
                                Log.d("TAG", "Error getting documents: ", task.getException());
                            }
                        }
                    });
                } else {
                    Log.d("TAG", "Error getting documents: ", task.getException());
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.filter_menu, menu);
        itemP = (MenuItem) menu.findItem(R.id.item1);
        itemW = (MenuItem) menu.findItem(R.id.item2);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            //Palestras
            case R.id.item1:
                //Verificar se está checked
                if(itemP.isChecked()){
                    //Se estiver, tirar
                    itemP.setChecked(false);
                    removePal();
                    adapter.notifyDataSetChanged();
                    viewPager.setAdapter(adapter);
                }else {
                    //se não estiver, por
                    itemP.setChecked(true);
                    addPal();
                    adapter.notifyDataSetChanged();
                    viewPager.setAdapter(adapter);
                }
                return true;
            //Workshops
            case R.id.item2:
                //Verificar se está checked
                //MenuItem item2 = (MenuItem) findViewById(R.id.item2);
                if(itemW.isChecked()){
                    //Se estiver, tirar
                    itemW.setChecked(false);
                    removeWork();
                    adapter.notifyDataSetChanged();
                    viewPager.setAdapter(adapter);
                }else {
                    //se não estiver, por
                    itemW.setChecked(true);
                    addWork();
                    adapter.notifyDataSetChanged();
                    viewPager.setAdapter(adapter);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void addPal(){
        finalArray.addAll(arrayPal);
    }

    public void removePal(){

        for (Iterator<DocumentSnapshot> fait = finalArray.iterator(); fait.hasNext(); ) {
            DocumentSnapshot doc = fait.next();
            for (Iterator<DocumentSnapshot> apit = arrayPal.iterator(); apit.hasNext(); ){
                DocumentSnapshot palestra = apit.next();
                if(doc.getId().equals(palestra.getId())){
                    fait.remove();
                }
            }
        }

    }

    public void addWork(){
        finalArray.addAll(arrayWork);
    }

    public void removeWork(){

        for (Iterator<DocumentSnapshot> fait = finalArray.iterator(); fait.hasNext(); ) {
            DocumentSnapshot doc = fait.next();
            for (Iterator<DocumentSnapshot> awit = arrayWork.iterator(); awit.hasNext(); ){
                DocumentSnapshot workshop = awit.next();
                if(doc.getId().equals(workshop.getId())){
                    fait.remove();
                }
            }
        }
    }



}
