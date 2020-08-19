package ua.cm.idiscover.SlideBar;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import ua.cm.idiscover.Main.SignInActivity;
import ua.cm.idiscover.R;

/**
 * Created by vitib on 19/03/2018.
 */

public class TicketActivity extends AppCompatActivity /*implements NavigationView.OnNavigationItemSelectedListener*/{

    ImageView qrImage;
    //TextView textAlert;
    DrawerLayout mDrawerLayout;
    ActionBarDrawerToggle mToggle;
    NavigationView nView;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth fauth = FirebaseAuth.getInstance();

    ProgressBar pb;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket);

        //textAlert = (TextView) findViewById(R.id.textAlert);
        qrImage = (ImageView) findViewById(R.id.qrImage);
        pb = (ProgressBar) findViewById(R.id.pbHeaderProgress);

        String mail = fauth.getCurrentUser().getEmail();

        //User is logged in
        if(mail != null){

            DocumentReference docRef = db.collection("Users").document(mail);

            //user exists in DB
            if(docRef !=null){

                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document != null) {
                                //Log.d("TAG", "DocumentSnapshot data: " + task.getResult().getData());
                                String code = document.getString("codigo");
                                //user has Code in DB
                                if(code != null){
                                    Log.d("TAG", "User QR Code: " + code);
                                    //generateQRCode(code);
                                    new genQRCode().execute(code);
                                } else {
                                    Toast.makeText(TicketActivity.this, "There is a problem with you ticket, please contact us.",
                                            Toast.LENGTH_LONG).show();
                                }

                            } else {
                                Log.d("TAG", "No such document");
                            }
                        } else {
                            Log.d("TAG", "get failed with ", task.getException());
                        }
                    }
                });

            }else {
                Log.d("TAG", "No such user!");
            }


        }else {
            Toast.makeText(TicketActivity.this, "You need to login first to have access to your ticket!",
                    Toast.LENGTH_LONG).show();
            Intent intent = new Intent(TicketActivity.this, SignInActivity.class);
            startActivity(intent);
        }


/*
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout);
        mToggle = new ActionBarDrawerToggle(this,L mDrawerLayout, R.string.open, R.string.close);
        nView = (NavigationView) findViewById(R.id.navigation_view);

        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        nView.setNavigationItemSelectedListener(this);*/
    }

    @Override
    protected void onStart() {
        super.onStart();
        /*if(getIntent().hasExtra("class")) {
            if(getIntent().getStringExtra("class") == "subscription") {
                generateQRCode(getIntent().getStringExtra("text"));
            }
        }*/
    }

    /*private void generateQRCode(String text) {
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();

        try{
            BitMatrix bitMatrix = multiFormatWriter.encode(text, BarcodeFormat.QR_CODE, 2000, 2000);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);

            //textAlert.setVisibility(View.INVISIBLE);
            qrImage.setImageBitmap(bitmap);
            //qrImage.setVisibility(View.VISIBLE);

        }catch (WriterException e){
            e.printStackTrace();
        }
    }*/

    class genQRCode extends AsyncTask<String, Integer, Bitmap> {
        Context mContext;
        int progress = -1;

        /*genQRCode(Context context) {
            mContext = context;
        }*/

        /*protected void setContext(Context context) {
            mContext = context;
            if (progress >= 0) {
                publishProgress(this.progress);
            }
        }*/

        protected void onPreExecute() {
            //progress = 0;
            pb.setVisibility(View.VISIBLE);
            pb.setIndeterminate(true);
            qrImage.setVisibility(View.INVISIBLE);
        }

        protected Bitmap doInBackground(String... text) {

            MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
            BitMatrix bitMatrix;
            BarcodeEncoder barcodeEncoder;
            Bitmap bitmap = null;
            try{
                bitMatrix = multiFormatWriter.encode(text[0], BarcodeFormat.QR_CODE, 2000, 2000);
                barcodeEncoder = new BarcodeEncoder();
                bitmap = barcodeEncoder.createBitmap(bitMatrix);

            }catch (WriterException e){
                e.printStackTrace();
            }
            Log.v("doInBackground", "generating QRCode...");
            return bitmap;
        }

        /*protected void onProgressUpdate(Integer... progress) {
            pb.setProgress(progress[0]);
            Log.v("onProgressUpdate", "Progress so far: " + progress[0]);
        }*/

        protected void onPostExecute(Bitmap result) {
            /*if (result != null) {
                downloadedImage = result;
                setImageInView();
            } else {
                Log.v("onPostExecute",
                        "Problem downloading image. Please try later.");
            }*/
            if(result != null){
                qrImage.setImageBitmap(result);
                pb.setVisibility(View.INVISIBLE);
                qrImage.setVisibility(View.VISIBLE);
            } else {
                Log.v("onPostExecute",
                        "Problem loading QRCode. Please try later.");
            }

        }

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
