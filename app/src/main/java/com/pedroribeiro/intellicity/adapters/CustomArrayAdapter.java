package com.pedroribeiro.intellicity.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.pedroribeiro.intellicity.R;
import com.pedroribeiro.intellicity.entities.Nota;

import java.util.ArrayList;

public class CustomArrayAdapter extends ArrayAdapter<Nota> {
    public CustomArrayAdapter(Context context, ArrayList<Nota> notas){
        super(context, 0, notas);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        // Get the data item for this position
        Nota n = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.layout_linha, parent, false);
        }
        ((TextView) convertView.findViewById(R.id.layout_linha_titulo)).setText(n.getTitulo());
        ((TextView) convertView.findViewById(R.id.layout_linha_descricao)).setText(n.getDescricao());
        ((TextView) convertView.findViewById(R.id.layout_linha_data)).setText(n.getData());

        return convertView;
    }
}
