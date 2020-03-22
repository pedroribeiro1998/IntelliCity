package com.pedroribeiro.intellicity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.pedroribeiro.intellicity.adapters.CustomArrayAdapter;
import com.pedroribeiro.intellicity.adapters.MyCursorAdapter;
import com.pedroribeiro.intellicity.db.Contrato;
import com.pedroribeiro.intellicity.db.DB;
import com.pedroribeiro.intellicity.entities.Nota;
import com.pedroribeiro.intellicity.utils.Utils;

import java.util.ArrayList;

public class Second extends AppCompatActivity {

    ArrayList<Nota> arrayNota;

    TextView edittitulo;
    TextView editdescricao;
    TextView editdata;
    TextView editlocalizacao;

    private int REQUEST_CODE_OP_1 = 1;

    DB mDbHelper;
    SQLiteDatabase db;
    Cursor c, c_pessoas;
    ListView lista;
    //SimpleCursorAdapter adapter;
    MyCursorAdapter madapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        mDbHelper = new DB(this);
        db = mDbHelper.getReadableDatabase();

        lista = (ListView) findViewById(R.id.lista);
        preencheLista();

        //Toast.makeText(Second.this,titulo,Toast.LENGTH_SHORT).show();
        //Toast.makeText(Second.this,descricao,Toast.LENGTH_SHORT).show();
        //Toast.makeText(Second.this,data,Toast.LENGTH_SHORT).show();
        //Toast.makeText(Second.this,localizacao,Toast.LENGTH_SHORT).show();

        registerForContextMenu((ListView) findViewById(R.id.lista));
        //arrayNota = new ArrayList<>();


        //fillLista();
    }

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

    public void botao_xpto(View v){
        Intent output = new Intent();
        output.putExtra(Utils.PARAM_OUTPUT, "resultado teste");
        setResult(RESULT_OK, output);
        finish();
    }

    public  void fillLista(){
        /*
        ArrayList<String> arrayItems = new ArrayList<>();

        arrayItems.add("Segunda");
        arrayItems.add("Terca");
        arrayItems.add("Quarta");
        arrayItems.add("Quinta");
        arrayItems.add("Sexta");
        arrayItems.add("Sabado");
        arrayItems.add("Domingo");

        ArrayAdapter<String> itemsAdapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayItems);
        ((ListView) findViewById(R.id.lista)).setAdapter(itemsAdapter);
        */

        String titulo = getIntent().getStringExtra(Utils.PARAM_TITULO);
        String descricao = getIntent().getStringExtra(Utils.PARAM_DESCRICAO);
        String data = getIntent().getStringExtra(Utils.PARAM_DATA);
        String localizacao = getIntent().getStringExtra(Utils.PARAM_LOCALIZACAO);

        //ArrayList<Nota> arrayNota = new ArrayList<>();
        arrayNota.add(new Nota ("titulo1","descricao1", "data1", "localizacao1"));
        arrayNota.add(new Nota ("titulo2","descricao2", "data2", "localizacao2"));
        arrayNota.add(new Nota("titulo3","descricao3", "data3", "localizacao3"));
        arrayNota.add(new Nota(titulo,descricao,data,localizacao));

        CustomArrayAdapter itemsAdapter = new CustomArrayAdapter(this, arrayNota);
        ((ListView) findViewById(R.id.lista)).setAdapter(itemsAdapter);
    }

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
                //Toast.makeText(Second.this, " Atualizado com sucesso! ", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.remove:
                //Toast.makeText(Second.this, String.valueOf(itemPosition), Toast.LENGTH_SHORT).show();
                //Toast.makeText(Second.this, id_nota + "/" + titulo_nota, Toast.LENGTH_SHORT).show();
                //pedir confirmação
                //apagar da bd
                deleteFromDB(id_nota);
                //refresh da lista
                //Toast.makeText(Second.this, " Removido com sucesso! ", Toast.LENGTH_SHORT).show();
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

/*
    @Override
    protected void onDestroy(){
        super.onDestroy();

        if(!c.isClosed()){
            c.close();
            c = null;
        }
        if(!c_pessoas.isClosed()){
            c_pessoas.close();
            c_pessoas = null;
        }
        if(db.isOpen()){
            db.close();
            db = null;
        }
    }*/
}
