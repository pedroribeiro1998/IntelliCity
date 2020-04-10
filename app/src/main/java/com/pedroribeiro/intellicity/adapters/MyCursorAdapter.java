package com.pedroribeiro.intellicity.adapters;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.pedroribeiro.intellicity.R;
import com.pedroribeiro.intellicity.db.Contrato;
import com.pedroribeiro.intellicity.entities.Report;

import java.util.ArrayList;

public class MyCursorAdapter extends CursorAdapter {

    private Context mContext;
    private int id;
    private Cursor mCursor;

    //private ArrayList<Report> reports;

    public MyCursorAdapter(Context context, Cursor c){
        super(context, c, 0);
        mContext = context;
        mCursor = c;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.layout_linha, parent, false);
    }

    @Override
    public void bindView(View mView, Context context, Cursor cursor) {
        TextView text1 = (TextView) mView.findViewById(R.id.layout_linha_titulo);
        TextView text2 = (TextView) mView.findViewById(R.id.layout_linha_descricao);
        TextView text3 = (TextView) mView.findViewById(R.id.layout_linha_data);
        text1.setText(mCursor.getString(cursor.getColumnIndexOrThrow(Contrato.Notas.COLUMN_TITULO)));
        text2.setText(mCursor.getString(cursor.getColumnIndexOrThrow(Contrato.Notas.COLUMN_DESCRICAO)));
        text3.setText(mCursor.getString(cursor.getColumnIndexOrThrow(Contrato.Notas.COLUMN_DATA)));
    }
}
