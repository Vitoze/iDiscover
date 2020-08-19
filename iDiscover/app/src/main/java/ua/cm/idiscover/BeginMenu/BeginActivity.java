package ua.cm.idiscover.BeginMenu;

import android.Manifest;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.widget.Button;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.zxing.integration.android.IntentIntegrator;

import java.util.List;

import ua.cm.idiscover.Main.SignInActivity;
import ua.cm.idiscover.Main.SignUpActivity;
import ua.cm.idiscover.R;
import ua.cm.idiscover.SlideBar.ProfileActivity;
import ua.cm.idiscover.SlideBar.SettingsActivity;
import ua.cm.idiscover.SlideBar.TicketActivity;
import ua.cm.idiscover.SlideBar.WorkshopsActivity;

/**
 * Created by vitib on 16/03/2018.
 */

public class BeginActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    Button btnSchedule, btnMap, btnForum, btnAbout;
    DrawerLayout mDrawerLayout;
    ActionBarDrawerToggle mToggle;
    NavigationView nView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_begin);

        initComponents();
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        nView.setNavigationItemSelectedListener(this);



        //click Subscription
        /*btnSubscription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BeginActivity.this, SubscriptionActivity.class);
                startActivity(intent);
            }
        });*/

        //click Schedule
        btnSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BeginActivity.this, ScheduleActivity.class);
                startActivity(intent);
            }
        });

        //click Map
        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*
                // Map point based on latitude/longitude
                Uri location = Uri.parse("geo:40.630449,-8.657184?z=14"); // z param is zoom level
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, location);
                // Verify it resolves
                PackageManager packageManager = getPackageManager();
                List<ResolveInfo> activities = packageManager.queryIntentActivities(mapIntent, 0);
                boolean isIntentSafe = activities.size() > 0;

                // Start an activity if it's safe
                if (isIntentSafe) {
                    startActivity(mapIntent);
                }
                else{
                    Context context = getApplicationContext();
                    CharSequence text = "There is no application installed on the device to open the map!";
                    int duration = Toast.LENGTH_LONG;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }*/
                Intent intent = new Intent(BeginActivity.this, MapActivity.class);
                startActivity(intent);
            }
        });

        //click Forum
        btnForum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.println(Log.INFO,"Entering Forum ", "Entering forum");

                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

                if(firebaseAuth.getCurrentUser() != null){
                    // User is signed in
                    Log.println(Log.INFO,"User: ", "User: " + firebaseAuth.getCurrentUser().getEmail());
                } else {
                    // User is signed out
                    Log.println(Log.INFO,"User is logged out: ","User is logged out" );
                }

                Intent intent = new Intent(BeginActivity.this, ForumActivity.class);
                startActivity(intent);
            }
        });

        //click About
        btnAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BeginActivity.this, AboutActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(mToggle.onOptionsItemSelected(item)){
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initComponents() {
        //btnSubscription = (Button) findViewById(R.id.btnSubscription);
        btnSchedule = (Button) findViewById(R.id.btnSchedule);
        btnMap = (Button) findViewById(R.id.btnMap);
        btnForum = (Button) findViewById(R.id.btnForum);
        btnAbout = (Button) findViewById(R.id.btnAbout);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
        nView = (NavigationView) findViewById(R.id.navigation_view);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.profile) {

            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

            if(firebaseAuth.getCurrentUser() != null){
                // User is signed in
                Log.println(Log.INFO,"User: ", "User: " + firebaseAuth.getCurrentUser().getEmail());
                Intent intent = new Intent(this, ProfileActivity.class);
                intent.putExtra("user", firebaseAuth.getCurrentUser().getEmail());
                startActivity(intent);
            } else {
                // User is signed out
                Log.println(Log.INFO,"User is logged out: ","User is logged out" );
            }

        }
        else if(id == R.id.ticket) {
            Intent intent = new Intent(this, TicketActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.workshops) {
            Intent intent = new Intent(this, WorkshopsActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        }else if(id == R.id.logout) {

            //logout
            FirebaseAuth.getInstance().signOut();

            Intent intent = new Intent(this, SignInActivity.class);
            startActivity(intent);
        }

        return false;
    }
}
