package com.pedroribeiro.intellicity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.pedroribeiro.intellicity.db.Contrato;
import com.pedroribeiro.intellicity.db.DB;
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

    DB mDbHelper;
    SQLiteDatabase db;
    Cursor c, c_pessoas;

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

        mDbHelper = new DB(this);
        db = mDbHelper.getReadableDatabase();

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
            /*
            textotitulo.setText(titulo);
            textodescricao.setText(descricao);
            textodata.setText(data);
            textolocalizacao.setText(localizacao);
            */

            ContentValues cv = new ContentValues();
            cv.put(Contrato.Notas.COLUMN_TITULO,"Linhas do piso gastas");
            cv.put(Contrato.Notas.COLUMN_DESCRICAO,"Não se consegue ver a linha contínua");
            cv.put(Contrato.Notas.COLUMN_DATA,"12.03.2020");
            cv.put(Contrato.Notas.COLUMN_LOCALIZACAO,"Tregosa");
            cv.put(Contrato.Notas.COLUMN_ID_UTILIZADOR,"1");
            db.insert(Contrato.Notas.TABLE_NAME, null, cv);

            //refresh();

            Intent i = new Intent(MainActivity.this, Second.class);
            i.putExtra(Utils.PARAM_TITULO, edittitulo.getText().toString());
            i.putExtra(Utils.PARAM_DESCRICAO, editdescricao.getText().toString());
            i.putExtra(Utils.PARAM_DATA, editdata.getText().toString());
            i.putExtra(Utils.PARAM_LOCALIZACAO, editlocalizacao.getText().toString());
            startActivityForResult(i, REQUEST_CODE_OP_1);

            edittitulo.setText("");
            editdescricao.setText("");
            editdata.setText("");
            editlocalizacao.setText("");
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
        /*
        i.putExtra(Utils.PARAM_TITULO, edittitulo.getText().toString());
        i.putExtra(Utils.PARAM_DESCRICAO, editdescricao.getText().toString());
        i.putExtra(Utils.PARAM_DATA, editdata.getText().toString());
        i.putExtra(Utils.PARAM_LOCALIZACAO, editlocalizacao.getText().toString());
        */
        startActivity(i);
        //startActivityForResult(i, REQUEST_CODE_OP_1);
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
/* toolbar superior
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
                Toast.makeText(MainActivity.this, "Opcao1", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.opcao2:
                Toast.makeText(MainActivity.this, "Opcao2", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.opcao3:
                Toast.makeText(MainActivity.this, "Opcao3", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.opcao4:
                Toast.makeText(MainActivity.this, "Opcao4", Toast.LENGTH_SHORT).show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

 */
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
    }

 */

}





