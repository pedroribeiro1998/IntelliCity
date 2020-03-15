package com.pedroribeiro.intellicity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.pedroribeiro.intellicity.adapters.CustomArrayAdapter;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);



        //Toast.makeText(Second.this,titulo,Toast.LENGTH_SHORT).show();
        //Toast.makeText(Second.this,descricao,Toast.LENGTH_SHORT).show();
        //Toast.makeText(Second.this,data,Toast.LENGTH_SHORT).show();
        //Toast.makeText(Second.this,localizacao,Toast.LENGTH_SHORT).show();

        registerForContextMenu((ListView) findViewById(R.id.lista));
        arrayNota = new ArrayList<>();


        fillLista();
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
        int index = info.position;
        Nota n = arrayNota.get(index);
        switch (item.getItemId()) {
            case R.id.edit:
                Toast.makeText(Second.this, "Editar " + n.getTitulo(), Toast.LENGTH_SHORT).show();
                /*
                edittitulo = (TextView) findViewById(R.id.edittitulo);
                editdescricao = (TextView) findViewById(R.id.editdescricao);
                editdata = (TextView) findViewById(R.id.editdata);
                editlocalizacao = (TextView) findViewById(R.id.editlocalizacao);

                Intent i = new Intent(Second.this, MainActivity.class);
                i.putExtra(Utils.PARAM_TITULO, edittitulo.getText().toString());
                i.putExtra(Utils.PARAM_DESCRICAO, editdescricao.getText().toString());
                i.putExtra(Utils.PARAM_DATA, editdata.getText().toString());
                i.putExtra(Utils.PARAM_LOCALIZACAO, editlocalizacao.getText().toString());
                startActivityForResult(i, REQUEST_CODE_OP_1);
                */
                return true;
            case R.id.remove:
                arrayNota.remove(index);
                Toast.makeText(Second.this, "Remover " + n.getTitulo(), Toast.LENGTH_SHORT).show();

                fillLista();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
}
