package com.pedroribeiro.intellicity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.pedroribeiro.intellicity.adapters.CustomArrayAdapter;
import com.pedroribeiro.intellicity.adapters.MyCursorAdapter;
import com.pedroribeiro.intellicity.adapters.ReportsListAdapter;
import com.pedroribeiro.intellicity.db.Contrato;
import com.pedroribeiro.intellicity.db.DB;
import com.pedroribeiro.intellicity.entities.Nota;
import com.pedroribeiro.intellicity.entities.Report;
import com.pedroribeiro.intellicity.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class Second extends AppCompatActivity {

    ArrayList<Nota> arrayNota;

    private int REQUEST_CODE_OP_1 = 1;

    DB mDbHelper;
    SQLiteDatabase db;
    Cursor c, c_pessoas;
    ListView lista;
    //SimpleCursorAdapter adapter;
    MyCursorAdapter madapter;

    //recyclerView para listar os reports da BD
    List<Report> reports_detalhe_List;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter rvAdapter;
    private RecyclerView.LayoutManager layoutManager;
    ReportsListAdapter.ItemClickListener itemClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        mDbHelper = new DB(this);
        db = mDbHelper.getReadableDatabase();

        reports_detalhe_List = ((List<Report>) getIntent().getExtras().getSerializable("REPORTS_LIST"));

        itemClickListener = ((view, position) -> {
            Report reporte = this.reports_detalhe_List.get(position);
            SelectOption_showReport(reporte);
        });

        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        // specify an adapter (see also next example)
        rvAdapter = new ReportsListAdapter(this, reports_detalhe_List,itemClickListener);
        recyclerView.setAdapter(rvAdapter);
        //https://stackoverflow.com/questions/28296708/get-clicked-item-and-its-position-in-recyclerview
    }
    //funcional e já não é usado
    public void invokeWS_2(View v){
        String url = "https://intellicity.000webhostapp.com/myslim_commov1920/api/reports_detalhe";
        JsonArrayRequest jsObjRequest = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for(int i = 0; i < response.length(); i++){
                                JSONObject obj = response.getJSONObject(i);
                                int id = obj.getInt("id");
                                String nome = obj.getString("nome");
                                int utilizador_id = obj.getInt("utilizador_id");
                                String titulo = obj.getString("titulo");
                                String descricao = obj.getString("descricao");
                                String data = obj.getString("data");
                                String localizacao = obj.getString("localizacao");
                                String fotografia = obj.getString("fotografia");
                                double latitude = obj.getDouble("latitude");
                                double longitude = obj.getDouble("longitude");

                                Report report = new Report(id, utilizador_id, titulo, descricao, data, localizacao, fotografia, latitude, longitude);
                                reports_detalhe_List.add(report);

                            }
                            for(int i = 0; i < reports_detalhe_List.size(); i++){
                                rvAdapter = new ReportsListAdapter(reports_detalhe_List, itemClickListener);
                                rvAdapter.notifyDataSetChanged();
                                recyclerView.setAdapter(rvAdapter);
                            }
                        } catch (JSONException ex) { }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        ((TextView) findViewById(R.id.layout_linha_titulo)).setText(error.getMessage());
                    }
                });

        // Access the RequestQueue through your singleton class.
        MySingleton.getInstance(this).addToRequestQueue(jsObjRequest);
    }

    private void SelectOption_showReport(Report reporte) {
        //Toast.makeText(Second.this, reporte.toString(), Toast.LENGTH_SHORT).show();
        Toast.makeText(Second.this, "report_id: " + reporte.id + " || título: " + reporte.titulo, Toast.LENGTH_SHORT).show();
    }
    //já não é usado
    private void preencheLista() {
        c = db.query(false, Contrato.Notas.TABLE_NAME, Contrato.Notas.PROJECTION,
                null, null, null, null, null, null);
        /*
        adapter = new SimpleCursorAdapter(
                this,
                android.R.layout.two_line_list_item,
                c,
                new String[] {Contrato.Notas.COLUMN_TITULO, Contrato.Notas.COLUMN_LOCALIZACAO},
                new int[] {android.R.id.text1, android.R.id.text2},
                SimpleCursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        */
        madapter = new MyCursorAdapter(Second.this, c);
        lista.setAdapter(madapter);

    }

    //este funciona e só retorna 1 nome acho eu
    /*
    public void invokeWS(View v){
        String url = "https://intellicity.000webhostapp.com/myslim_commov1920/api/reports/2"; //2 = iduser
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            ((TextView) findViewById(R.id.data)).setText(response.getString("titulo"));
                        } catch (JSONException ex) {
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        ((TextView) findViewById(R.id.data)).setText(error.getMessage());
                    }
                });
        // Access the RequestQueue through your singleton class.
        MySingleton.getInstance(this).addToRequestQueue(jsObjRequest);
    }*/

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuinfo) {
        super.onCreateContextMenu(menu, v, menuinfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_cont_1, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        // Handle item selection
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        //int index = info.position;
        //Nota n = arrayNota.get(index);
        int itemPosition = info.position;
        c.moveToPosition(itemPosition);
        int id_nota = c.getInt(c.getColumnIndex(Contrato.Notas._ID));
        String titulo_nota = c.getString(c.getColumnIndex(Contrato.Notas.COLUMN_TITULO));
        switch (item.getItemId()) {
            case R.id.edit:
                updateInDB(id_nota);
                Toast.makeText(Second.this, " Atualizado com sucesso! ", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.remove:
                //Toast.makeText(Second.this, String.valueOf(itemPosition), Toast.LENGTH_SHORT).show();
                //Toast.makeText(Second.this, id_nota + "/" + titulo_nota, Toast.LENGTH_SHORT).show();
                //pedir confirmação
                //apagar da bd
                deleteFromDB(id_nota);
                //refresh da lista
                Toast.makeText(Second.this, " Removido com sucesso! ", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void updateInDB(int id_nota) {
        ContentValues cv = new ContentValues();
        cv.put(Contrato.Notas.COLUMN_TITULO,"Placas de sinalização caídas");
        db.update(Contrato.Notas.TABLE_NAME, cv, Contrato.Notas._ID + " = ?", new String[]{id_nota+""});
        refresh();
    }

    private void deleteFromDB(int id_nota) {
        db.delete(Contrato.Notas.TABLE_NAME, Contrato.Notas._ID + " = ?", new String[]{id_nota+""});
        refresh();
    }

    private void refresh() {
        getCursor();
        madapter.swapCursor(c);
    }

    private void getCursor(){
        String sql = "Select " +
                Contrato.Notas.COLUMN_TITULO + "," +
                Contrato.Notas.COLUMN_DESCRICAO + "," +
                Contrato.Notas.COLUMN_DATA + "," +
                Contrato.Notas.COLUMN_LOCALIZACAO + "," +
                Contrato.Notas._ID + "," +
                Contrato.Notas.COLUMN_ID_UTILIZADOR + " FROM " +
                Contrato.Notas.TABLE_NAME;

        c = db.rawQuery(sql, null);
    }

    /* toolbar superior*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_ativ_1, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.opcao1:
                //Toast.makeText(Second.this, "Voltar", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(Second.this, NotasActivity.class);
                //i.putExtra("z", "VenhoDaMain");
                //i.putExtra("y", "VenhoDaSecond");
                //i.putExtra("x", "VenhoDoMap");
                i.putExtra("z", "VenhoDaMainSecondteste");
                startActivity(i);
                return true;

            case R.id.opcao2:
                Toast.makeText(Second.this, "Sobre", Toast.LENGTH_SHORT).show();
                return true;
                /*
            case R.id.opcao3:
                Toast.makeText(NotasActivity.this, "Opcao3", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.opcao4:
                Toast.makeText(NotasActivity.this, "Opcao4", Toast.LENGTH_SHORT).show();
                return true;
                 */

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
