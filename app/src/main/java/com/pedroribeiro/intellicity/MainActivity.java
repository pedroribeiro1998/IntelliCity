package com.pedroribeiro.intellicity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.pedroribeiro.intellicity.utils.Utils;

public class MainActivity extends AppCompatActivity {

    EditText edittitulo;
    EditText editdescricao;
    EditText editdata;
    EditText editlocalizacao;

    TextView textotitulo;
    TextView textodescricao;
    TextView textodata;
    TextView textolocalizacao;

    private int REQUEST_CODE_OP_1 = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edittitulo = (EditText) findViewById(R.id.edittitulo);
        editdescricao = (EditText) findViewById(R.id.editdescricao);
        editdata = (EditText) findViewById(R.id.editdata);
        editlocalizacao = (EditText) findViewById(R.id.editlocalizacao);

        textotitulo = (TextView) findViewById(R.id.txttitulo);
        textodescricao = (TextView) findViewById(R.id.txtdescricao);
        textodata = (TextView) findViewById(R.id.txtdata);
        textolocalizacao = (TextView) findViewById(R.id.txtlocalizacao);


        Toast.makeText(MainActivity.this, getResources().getString(R.string.bemvindo), Toast.LENGTH_SHORT).show();
    }

    public void guardar(View v){
        //EditText edittitulo = (EditText) findViewById(R.id.edittitulo);
        //EditText editdescricao = (EditText) findViewById(R.id.editdescricao);
        //EditText editdata = (EditText) findViewById(R.id.editdata);
        //EditText editlocalizacao = (EditText) findViewById(R.id.editlocalizacao);
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
            //TextView textotitulo = (TextView) findViewById(R.id.txttitulo);
            //TextView textodescricao = (TextView) findViewById(R.id.txtdescricao);
            //TextView textodata = (TextView) findViewById(R.id.txtdata);
            //TextView textolocalizacao = (TextView) findViewById(R.id.txtlocalizacao);
            textotitulo.setText(titulo);
            textodescricao.setText(descricao);
            textodata.setText(data);
            textolocalizacao.setText(localizacao);
        }
    }

    public void limpar(View v){
        //EditText edittitulo = (EditText) findViewById(R.id.edittitulo);
        //EditText editdescricao = (EditText) findViewById(R.id.editdescricao);
        //EditText editdata = (EditText) findViewById(R.id.editdata);
        //EditText editlocalizacao = (EditText) findViewById(R.id.editlocalizacao);

        edittitulo.setText("");
        editdescricao.setText("");
        editdata.setText("");
        editlocalizacao.setText("");

        //TextView textotitulo = (TextView) findViewById(R.id.txttitulo);
        //TextView textodescricao = (TextView) findViewById(R.id.txtdescricao);
        //TextView textodata = (TextView) findViewById(R.id.txtdata);
        //TextView textolocalizacao = (TextView) findViewById(R.id.txtlocalizacao);

        textotitulo.setText("");
        textodescricao.setText("");
        textodata.setText("");
        textolocalizacao.setText("");
    }

    public void botao2(View v){
        Intent i = new Intent(MainActivity.this, Second.class);
        i.putExtra(Utils.PARAM_TITULO, edittitulo.getText().toString());
        i.putExtra(Utils.PARAM_DESCRICAO, editdescricao.getText().toString());
        i.putExtra(Utils.PARAM_DATA, editdata.getText().toString());
        i.putExtra(Utils.PARAM_LOCALIZACAO, editlocalizacao.getText().toString());
        //startActivity(i);
        startActivityForResult(i, REQUEST_CODE_OP_1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == REQUEST_CODE_OP_1) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                // The user picked a contact.
                // The Intent's data Uri identifies which contact was selected.
                // Do something with the contact here (bigger example below)
                Toast.makeText(MainActivity.this, data.getStringExtra(Utils.PARAM_OUTPUT), Toast.LENGTH_SHORT).show();
            }
        }
    }

}
