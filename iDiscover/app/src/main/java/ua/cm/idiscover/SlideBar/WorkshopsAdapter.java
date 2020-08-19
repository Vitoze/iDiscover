package ua.cm.idiscover.SlideBar;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

import ua.cm.idiscover.BeginMenu.Event;
import ua.cm.idiscover.R;

import static android.content.Context.DOWNLOAD_SERVICE;

/**
 * Created by Ana on 03/04/2018.
 */

public class WorkshopsAdapter extends BaseAdapter {

    private Context context;

    private LayoutInflater layoutInflater;
    private ArrayList<Event> arrayList;
    private Activity activity;

    private class ViewHolder{
        TextView tx1;
        TextView tx2;
        TextView tx3;
        TextView tx4;
        ImageButton btn_d;
        ImageButton btn_s;
        ImageButton btn_t;
    }

    public WorkshopsAdapter(Context context, Activity activity, ArrayList<Event> objects) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        arrayList = objects;
        this.activity=activity;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Event getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null) {
            holder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.s_list_item, null);
            holder.tx1 = (TextView) convertView.findViewById(R.id.textView9);
            holder.tx2 = (TextView) convertView.findViewById(R.id.textView10);
            holder.tx3 = (TextView) convertView.findViewById(R.id.textView14);
            holder.tx4 = (TextView) convertView.findViewById(R.id.textView15);
            holder.btn_d = (ImageButton) convertView.findViewById(R.id.imageButton);
            holder.btn_s = (ImageButton) convertView.findViewById(R.id.imageButton2);
            holder.btn_t = (ImageButton) convertView.findViewById(R.id.imageButton3);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        SimpleDateFormat fm = new SimpleDateFormat("HH:mm");
        holder.tx1.setText(arrayList.get(position).getName());
        holder.tx2.setText(fm.format(arrayList.get(position).getTime()));
        holder.tx3.setText(arrayList.get(position).getPerson());
        holder.tx4.setText(arrayList.get(position).getPlace());
        holder.btn_d.setContentDescription(arrayList.get(position).getPresentation());
        holder.btn_s.setContentDescription(arrayList.get(position).getId());
        holder.btn_t.setContentDescription(arrayList.get(position).getId());

        holder.btn_d.setVisibility(View.GONE);
        holder.btn_s.setVisibility(View.GONE);
        holder.btn_t.setVisibility(View.VISIBLE);


        final ImageButton im = holder.btn_t;
        //OnClickListener
        im.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("BTN", "Trash Button Clicked");

                FirebaseFirestore db = FirebaseFirestore.getInstance();

                FirebaseAuth fauth = FirebaseAuth.getInstance();

                final FirebaseUser user = fauth.getCurrentUser();

                db.collection("Workshops").document(im.getContentDescription().toString()).collection("Inscritos").document(user.getEmail())
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d("Removal", "DocumentSnapshot successfully deleted!");
                                Toast.makeText(context, "Subscription removed!", Toast.LENGTH_SHORT).show();
                                //((WorkshopsActivity)activity).refreshListviewAdapter();
                                ((WorkshopsActivity)activity).loadActivity();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("Removal", "Error deleting document", e);
                                Toast.makeText(context, "Error removing subscription", Toast.LENGTH_SHORT).show();
                            }
                        });


            }
        });


        return convertView;

    }
}
