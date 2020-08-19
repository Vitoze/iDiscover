package ua.cm.idiscover.BeginMenu;

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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

import ua.cm.idiscover.R;

import static android.content.Context.DOWNLOAD_SERVICE;

/**
 * Created by Ana on 03/04/2018.
 */

public class CustomAdapter extends BaseAdapter {

    private Context context;

    private LayoutInflater layoutInflater;
    private ArrayList<Event> arrayList;

    // Create a storage reference from our app
    FirebaseStorage storage = FirebaseStorage.getInstance();

    StorageReference storageRef = storage.getReference();

    // Create a reference to a file from a Google Cloud Storage URI
    StorageReference gsReference = storage.getReferenceFromUrl("gs://idiscover-ua2018.appspot.com/Presentation.pdf");



    private class ViewHolder{
        TextView tx1;
        TextView tx2;
        TextView tx3;
        TextView tx4;
        ImageButton btn_d;
        ImageButton btn_s;
        ImageButton btn_t;
    }

    public CustomAdapter(Context context, ArrayList<Event> objects) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        arrayList = objects;
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

        //Distinguir palestras de Workshops
        //Se for palestra, por a vermelho
        if(arrayList.get(position).getType().trim().equals("Palestra")){
            holder.tx1.setTextColor(Color.rgb(128,0,0));
        }else {
            //Se for workshop por a azul
            if(arrayList.get(position).getType().trim().equals("Workshop")){
                holder.tx1.setTextColor(Color.rgb(25,25,112));
            }
            //Se for coffe break por a verde
            else{
                holder.tx1.setTextColor(Color.rgb(0,100,0));
            }
        }

        //Verificar se é workshop para por botão
        if(arrayList.get(position).getType().equals("Workshop")){
            holder.btn_s.setVisibility(View.VISIBLE);
        }else {
            holder.btn_s.setVisibility(View.INVISIBLE);
        }

        //Verificar se tem apresentações ou não
        if(arrayList.get(position).getPresentation() != null){
            holder.btn_d.setVisibility(View.VISIBLE);
        }else {
            holder.btn_d.setVisibility(View.INVISIBLE);
        }

        holder.btn_t.setVisibility(View.GONE);

        final ImageButton im = holder.btn_d;
        //OnClickListener
        im.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("BTN", "Msg: " + im.getContentDescription());
                //final long downloadId;
                gsReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        // Got the download URL
                        Log.d("URI", "Url: " + uri);

                        DownloadManager dMgr = (DownloadManager) context.getSystemService(DOWNLOAD_SERVICE);
                        DownloadManager.Request dmReq = new DownloadManager.Request(uri);
                        dmReq.setTitle("Presentation Download");
                        dmReq.setDescription("Evento CM");
                        dmReq.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "Presentation.pdf");
                        dmReq.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);

                        final long downloadId = dMgr.enqueue(dmReq);

                        IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
                        BroadcastReceiver mReceiver = new BroadcastReceiver() {
                            @Override
                            public void onReceive(Context context, Intent intent) {
                                Bundle extras = intent.getExtras();
                                long doneDownloadId =
                                        extras.getLong(DownloadManager.EXTRA_DOWNLOAD_ID);
                                if(downloadId == doneDownloadId) {
                                    Toast.makeText(context, "Download finished!", Toast.LENGTH_SHORT).show();
                                    Log.v("Download", "Our download has completed.");
                                    context.unregisterReceiver(this);
                                }
                            }

                        };
                        context.registerReceiver(mReceiver, filter);


                        Toast.makeText(context, "Download started...", Toast.LENGTH_SHORT).show();
                        //tv.setText("Download started... (" + downloadId + ")");



                    }



                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle any errors
                        Log.d("ERROR", "Error: " + exception.getMessage());
                    }
                });

            }
        });

        final ImageButton btn_w = holder.btn_s;

        //OnClickListener
        btn_w.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("BTN", "Msg: " + btn_w.getContentDescription().toString());

                FirebaseFirestore db = FirebaseFirestore.getInstance();

                FirebaseAuth fauth = FirebaseAuth.getInstance();

                FirebaseUser user = fauth.getCurrentUser();

                CollectionReference col = db.collection("Workshops").document(btn_w.getContentDescription().toString()).collection("Inscritos");

                if(user.getEmail() != null){

                    HashMap<String, Long> inscricao = new HashMap<String, Long>();

                    inscricao.put("Data", System.currentTimeMillis());

                    col.document(user.getEmail())
                            .set(inscricao)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("INS", "Inscrição feita! :)");
                                    Toast.makeText(context, "Subscription Succeded!", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w("Error", "Error writing document", e);
                                    Toast.makeText(context, "A problem occured during your subscription.", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });

        return convertView;

    }
}
