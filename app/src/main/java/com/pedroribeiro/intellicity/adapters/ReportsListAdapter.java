package com.pedroribeiro.intellicity.adapters;

import android.content.Context;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.pedroribeiro.intellicity.entities.Report;
import com.pedroribeiro.intellicity.R;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReportsListAdapter extends RecyclerView.Adapter<ReportsListAdapter.MyViewHolder> {
    Context context;
    List<Report> reports = new ArrayList<>();
    ItemClickListener itemClickListener;

    public ReportsListAdapter(Context context, List<Report> reports) {
        this.context = context;
        this.reports = reports;
    }

    public ReportsListAdapter(List<Report> reports, ItemClickListener itemClickListener) {
        this.reports = reports;
        this.itemClickListener = itemClickListener;
    }

    public ReportsListAdapter(Context context, List<Report> reports, ItemClickListener itemClickListener) {
        this.context = context;
        this.reports = reports;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public ReportsListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // create a new view
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_linha, parent, false);
        return new ReportsListAdapter.MyViewHolder(view, itemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        Report report = reports.get(position);
        holder.titulo.setText(report.getTitulo());
        holder.descricao.setText(report.getDescricao());
        holder.data.setText(report.getData());
        /*
        String date = report.getData();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/YYYY hh:mm");
        String String_data = sdf.format(date);
        holder.data.setText(String_data);
        */
    }

    @Override
    public int getItemCount() {
        //return mDataset.length;
        return reports.size();
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener {
        // each data item is just a string in this case
        public TextView textView, titulo, descricao, data;
        ItemClickListener itemClickListener;
        CardView cardView;
        public MyViewHolder(View itemView, ItemClickListener itemClickListener) {
            //super(v);
            //textView = v;
            super(itemView);
            titulo = itemView.findViewById(R.id.layout_linha_titulo);
            descricao = itemView.findViewById(R.id.layout_linha_descricao);
            data = itemView.findViewById(R.id.layout_linha_data);
            cardView = itemView.findViewById(R.id.layout_linha_card_view);

            this.itemClickListener = itemClickListener;
            cardView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onClick(View v) {
            itemClickListener.onItemClick(v, getAdapterPosition());
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
           /* super.onCreateContextMenu(menu, v, menuinfo);
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_cont_1, menu);*/
        }
    }

    public interface ItemClickListener{
        void onItemClick(View view, int position);
    }

    /*

     // Showing popup menu when tapping on 3 dots

    private void showPopupMenu(View view) {
        // inflate menu
        PopupMenu popup = new PopupMenu(mContext, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_album, popup.getMenu());
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener());
        popup.show();
    }
     */
}