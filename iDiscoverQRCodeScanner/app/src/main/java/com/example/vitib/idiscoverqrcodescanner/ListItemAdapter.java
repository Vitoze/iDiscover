package com.example.vitib.idiscoverqrcodescanner;

import android.content.ClipData;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ListItemAdapter extends ArrayAdapter<User> {

    private Context context;
    private ArrayList<User> lista;

    public ListItemAdapter(Context context, ArrayList<User> lista) {
        super(context, 0 , lista);
        this.context = context;
        this.lista = lista;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        User userposition = this.lista.get(position);
        convertView = LayoutInflater.from(this.context).inflate(R.layout.item_subscription, null);

        TextView username = (TextView) convertView.findViewById(R.id.s_username);
        username.setText(userposition.getUsername());
        TextView userage = (TextView) convertView.findViewById(R.id.s_userage);
        userage.setText(userposition.getUserage());
        TextView usermail = (TextView) convertView.findViewById(R.id.s_usermail);
        usermail.setText(userposition.getUsermail());

        return convertView;
    }
}
