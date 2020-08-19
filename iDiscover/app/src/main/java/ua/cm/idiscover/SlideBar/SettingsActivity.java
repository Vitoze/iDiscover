package ua.cm.idiscover.SlideBar;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import ua.cm.idiscover.Main.SignInActivity;
import ua.cm.idiscover.R;

/**
 * Created by vitib on 23/03/2018.
 */

public class SettingsActivity extends AppCompatActivity /*implements NavigationView.OnNavigationItemSelectedListener*/{

    DrawerLayout mDrawerLayout;
    ActionBarDrawerToggle mToggle;
    NavigationView nView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
/*
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
        nView = (NavigationView) findViewById(R.id.navigation_view);

        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        nView.setNavigationItemSelectedListener(this);*/
    }
/*
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.profile) {
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.ticket) {
            Intent intent = new Intent(this, TicketActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.schedule) {
            Intent intent = new Intent(this, ScheduleItemActivity.class);
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
            Intent intent = new Intent(this, SignInActivity.class);
            startActivity(intent);
        }

        return false;
    }*/
}
