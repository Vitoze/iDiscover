package ua.cm.idiscover.BeginMenu;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ua.cm.idiscover.R;

/**
 * Created by Ana on 01/04/2018.
 */

public class PageAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;
    ArrayList<DocumentSnapshot> array = null;

    public PageAdapter(FragmentManager fm, int NumOfTabs, ArrayList<DocumentSnapshot> array) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
        this.array = array;
        Log.d("TAG", "Data: " + this.array.size());

    }

    @Override
    public Fragment getItem(int position) {

        int i = 0;
        /*ArrayList<Map<String, Object>> dia1 = new ArrayList<>();
        ArrayList<Map<String, Object>> dia2 = new ArrayList<>();
        ArrayList<Map<String, Object>> dia3 = new ArrayList<>();*/

        ArrayList<DocumentSnapshot> dia1 = new ArrayList<>();
        ArrayList<DocumentSnapshot> dia2 = new ArrayList<>();
        ArrayList<DocumentSnapshot> dia3 = new ArrayList<>();

        for (i=0; i<array.size(); i++){
            if(array.get(i).get("Dia").equals("23/04/2018")){
                //dia1.add(array.get(i).getData());
                dia1.add(array.get(i));
            } else {
                if(array.get(i).get("Dia").equals("24/04/2018")){
                    dia2.add(array.get(i));
                }else {
                    dia3.add(array.get(i));
                }
            }
        }
        Log.d("TAG", "Dia1: " + dia1);
        Log.d("TAG", "Dia2: " + dia2);
        Log.d("TAG", "Dia3: " + dia3);

        switch (position) {
            case 0:
                Fragment23 tab1 = new Fragment23();
                Bundle bundle = new Bundle();
                bundle.putSerializable("content", dia1);
                tab1.setArguments(bundle);
                return tab1;
            case 1:
                Fragment24 tab2 = new Fragment24();
                Bundle bundle2 = new Bundle();
                bundle2.putSerializable("content", dia2);
                tab2.setArguments(bundle2);
                return tab2;
            case 2:
                Fragment25 tab3 = new Fragment25();
                Bundle bundle3 = new Bundle();
                bundle3.putSerializable("content", dia3);
                tab3.setArguments(bundle3);
                return tab3;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }

}