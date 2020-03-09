package com.pedroribeiro.intellicity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toast.makeText(MainActivity.this, getResources().getString(R.string.bemvindo), Toast.LENGTH_SHORT).show();
    }

    public void guardar(View v){
        EditText edittitulo = (EditText) findViewById(R.id.edittitulo);
        EditText editdescricao = (EditText) findViewById(R.id.editdescricao);
        EditText editdata = (EditText) findViewById(R.id.editdata);
        EditText editlocalizacao = (EditText) findViewById(R.id.editlocalizacao);
        String titulo = edittitulo.getText().toString();
        String descricao = editdescricao.getText().toString();
        String data = editdata.getText().toString();
        String localizacao = editlocalizacao.getText().toString();
        if(titulo.equals("")){
            Toast.makeText(MainActivity.this, "Preencha o campo titulo", Toast.LENGTH_SHORT).show();
        }else if(descricao.equals("")){
            Toast.makeText(MainActivity.this, "Preencha o campo descricao", Toast.LENGTH_SHORT).show();
        }else if(data.equals("")){
            Toast.makeText(MainActivity.this, "Preencha o campo data", Toast.LENGTH_SHORT).show();
        }else if(localizacao.equals("")){
            Toast.makeText(MainActivity.this, "Preencha o campo localizacao", Toast.LENGTH_SHORT).show();
        }else{
            TextView textotitulo = (TextView) findViewById(R.id.txttitulo);
            TextView textodescricao = (TextView) findViewById(R.id.txtdescricao);
            TextView textodata = (TextView) findViewById(R.id.txtdata);
            TextView textolocalizacao = (TextView) findViewById(R.id.txtlocalizacao);
            textotitulo.setText(titulo);
            textodescricao.setText(descricao);
            textodata.setText(data);
            textolocalizacao.setText(localizacao);
        }
    }

    public void limpar(View v){
        EditText edittitulo = (EditText) findViewById(R.id.edittitulo);
        EditText editdescricao = (EditText) findViewById(R.id.editdescricao);
        EditText editdata = (EditText) findViewById(R.id.editdata);
        EditText editlocalizacao = (EditText) findViewById(R.id.editlocalizacao);

        edittitulo.setText("");
        editdescricao.setText("");
        editdata.setText("");
        editlocalizacao.setText("");

        TextView textotitulo = (TextView) findViewById(R.id.txttitulo);
        TextView textodescricao = (TextView) findViewById(R.id.txtdescricao);
        TextView textodata = (TextView) findViewById(R.id.txtdata);
        TextView textolocalizacao = (TextView) findViewById(R.id.txtlocalizacao);

        textotitulo.setText("");
        textodescricao.setText("");
        textodata.setText("");
        textolocalizacao.setText("");
    }
}
