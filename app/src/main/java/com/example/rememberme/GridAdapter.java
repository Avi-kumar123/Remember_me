package com.example.rememberme;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class GridAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<HashMap<String, String>> maplist ;
    LayoutInflater inflter;


    public GridAdapter(Context c,ArrayList<HashMap<String, String>> maplist) {
        this.mContext = c;
        this.maplist = maplist;
        this.inflter = (LayoutInflater.from(c));
    }
    public int getCount() {
        return maplist.size();
    }
    public Object getItem(int position) {
        return null;
    }
    public long getItemId(int position) {
        return 0;
    }
    public void clearAdapter(){
        maplist.clear();
    }

    public void addNewValues(ArrayList<HashMap<String, String>> maplist){
        this.maplist = maplist;
    }
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        // Inflate the layout for each list item
       // LayoutInflater _inflater = (LayoutInflater)getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
       /* if (v == null) {
            v = (RelativeLayout) inflater.inflate(resource, null);
        }*/
        v = inflter.inflate(R.layout.list_item, null);
        // Get the TextView and ImageView from CustomView for displaying item
        TextView txtview = v.findViewById(R.id.listitemTxt);
        TextView dateTxt =  v.findViewById(R.id.datetime);

        // Set the text and image for current item using data from map list
        txtview.setText(maplist.get(position).get("title").toString());
        dateTxt.setText(maplist.get(position).get("icon"));
        return v;
    }

    public void filter(String  charText){
        charText = charText.toLowerCase(Locale.getDefault());
        maplist.clear();
        if(charText.length()==0){
       //     maplist.addAll("");
        }
    }
}
