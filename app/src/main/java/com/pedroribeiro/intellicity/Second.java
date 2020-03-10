package com.pedroribeiro.intellicity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.pedroribeiro.intellicity.adapters.CustomArrayAdapter;
import com.pedroribeiro.intellicity.entities.Nota;
import com.pedroribeiro.intellicity.utils.Utils;

import java.util.ArrayList;

public class Second extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        /*
        String titulo = getIntent().getStringExtra(Utils.PARAM_TITULO);
        Toast.makeText(Second.this,titulo,Toast.LENGTH_SHORT).show();

        String descricao = getIntent().getStringExtra(Utils.PARAM_DESCRICAO);
        Toast.makeText(Second.this,descricao,Toast.LENGTH_SHORT).show();

        String data = getIntent().getStringExtra(Utils.PARAM_DATA);
        Toast.makeText(Second.this,data,Toast.LENGTH_SHORT).show();

        String localizacao = getIntent().getStringExtra(Utils.PARAM_LOCALIZACAO);
        Toast.makeText(Second.this,localizacao,Toast.LENGTH_SHORT).show();
        */

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
        ArrayList<Nota> arrayNota = new ArrayList<>();
        arrayNota.add(new Nota ("titulo1","descricao1", "data1", "localizacao1"));
        arrayNota.add(new Nota ("titulo2","descricao2", "data2", "localizacao2"));
        arrayNota.add(new Nota("titulo3","descricao3", "data3", "localizacao3"));

        CustomArrayAdapter itemsAdapter = new CustomArrayAdapter(this, arrayNota);
        ((ListView) findViewById(R.id.lista)).setAdapter(itemsAdapter);
    }
}
