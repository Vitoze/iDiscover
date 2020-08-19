package ua.cm.idiscover.BeginMenu;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.firestore.DocumentSnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import ua.cm.idiscover.R;

public class Fragment24 extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_fragment24, container, false);
    }

    public void onViewCreated(View view, Bundle savedInstanceState){

        Bundle b = getArguments();
        //ArrayList<Map<String, Object>> content = (ArrayList<Map<String, Object>>) b.get("content");
        ArrayList<DocumentSnapshot> content = (ArrayList<DocumentSnapshot>) b.get("content");
        Log.d("FRAG", "Fragment2: " + content);

        ArrayList<Event> objects = new ArrayList<Event>();

        //for(Map<String, Object> c: content){
        for(DocumentSnapshot c: content){

            String pattern = "HH:mm";
            SimpleDateFormat format = new SimpleDateFormat(pattern);
            Date date = new Date();
            try {
                //date = format.parse(c.get("Hora").toString());
                date = format.parse(c.getData().get("Hora").toString());
                Log.d("FRAG", "Horas: " + date);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            //if (c.get("Oradores") == null){
            if (c.getData().get("Oradores") == null){
                //if(c.get("Apresentação") == null){
                if(c.getData().get("Apresentação") == null){
                    //objects.add(new Event(c.get("Nome").toString(), null, date, c.get("Sala").toString(), null));
                    objects.add(new Event(c.getId().toString(), c.getData().get("Tipo").toString(), c.get("Nome").toString(), null, date, c.get("Sala").toString(), null));
                }else {
                    objects.add(new Event(c.getId().toString(), c.getData().get("Tipo").toString(), c.get("Nome").toString(), null, date, c.get("Sala").toString(), c.get("Apresentação").toString()));
                }
            } else{
                if(c.get("Apresentação") == null){
                    objects.add(new Event(c.getId().toString(), c.getData().get("Tipo").toString(), c.get("Nome").toString(), c.get("Oradores").toString(), date, c.get("Sala").toString(), null));
                }else {
                    objects.add(new Event(c.getId().toString(), c.getData().get("Tipo").toString(), c.get("Nome").toString(), c.get("Oradores").toString(), date, c.get("Sala").toString(), c.get("Apresentação").toString()));
                }
            }
        }

        //Ordenar eventos por hora
        int i = 0;
        boolean change = true;
        while(change){
            change = false;
            for (i=0; i<objects.size()-1; i++){
                if(objects.get(i).getTime().after(objects.get(i+1).getTime())){
                    Event after = objects.get(i);
                    objects.set(i, objects.get(i+1));
                    objects.set(i+1, after);
                    change = true;
                }
            }
        }
        ListView listView = (ListView) getView().findViewById(R.id.listView);
        CustomAdapter customAdapter = new CustomAdapter(getContext(), objects);
        listView.setAdapter(customAdapter);

    }

}
