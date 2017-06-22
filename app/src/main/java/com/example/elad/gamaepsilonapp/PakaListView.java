package com.example.elad.gamaepsilonapp;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.List;

/**
 * Created by elad on 15/06/2017.
 */

public class PakaListView extends ArrayAdapter<Paka> {

    private Activity context;
    private List<Paka> pakaList;

    public PakaListView (Activity context, List<Paka> pakaList){
        super(context, R.layout.paka_list_view, pakaList);

        this.context = context;
        this.pakaList = pakaList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.paka_list_view, null, true);
        TextView itemView = (TextView)listViewItem.findViewById(R.id.listView);
        Paka paka = pakaList.get(position);
        itemView.setText(paka.getPakaNum());
        return listViewItem;
    }
}
