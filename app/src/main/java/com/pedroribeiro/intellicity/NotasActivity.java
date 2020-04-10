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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.pedroribeiro.intellicity.db.Contrato;
import com.pedroribeiro.intellicity.db.DB;
import com.pedroribeiro.intellicity.entities.Report;
import com.pedroribeiro.intellicity.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

public class NotasActivity extends AppCompatActivity {

    EditText edittitulo;
    EditText editdescricao;
    EditText editdata;
    EditText editlocalizacao;

    private int REQUEST_CODE_OP_1 = 1;

    DB mDbHelper;
    SQLiteDatabase db;
    Cursor c, c_pessoas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notas);

        edittitulo = (EditText) findViewById(R.id.edittitulo);
        editdescricao = (EditText) findViewById(R.id.editdescricao);
        editdata = (EditText) findViewById(R.id.editdata);
        editlocalizacao = (EditText) findViewById(R.id.editlocalizacao);

        mDbHelper = new DB(this);
        db = mDbHelper.getReadableDatabase();

    }

    public void guardar(View v){
        String titulo = edittitulo.getText().toString();
        String descricao = editdescricao.getText().toString();
        String data = editdata.getText().toString();
        String localizacao = editlocalizacao.getText().toString();
        if(titulo.equals("")){
            Toast.makeText(NotasActivity.this, "Preencha o campo titulo", Toast.LENGTH_SHORT).show();
        }else if(descricao.equals("")){
            Toast.makeText(NotasActivity.this, "Preencha o campo descricao", Toast.LENGTH_SHORT).show();
        }else if(data.equals("")){
            Toast.makeText(NotasActivity.this, "Preencha o campo data", Toast.LENGTH_SHORT).show();
        }else if(localizacao.equals("")){
            Toast.makeText(NotasActivity.this, "Preencha o campo localizacao", Toast.LENGTH_SHORT).show();
        }else{
            //refresh();

            Intent i = new Intent(NotasActivity.this, Second.class);
            i.putExtra(Utils.PARAM_TITULO, edittitulo.getText().toString());
            i.putExtra(Utils.PARAM_DESCRICAO, editdescricao.getText().toString());
            i.putExtra(Utils.PARAM_DATA, editdata.getText().toString());
            i.putExtra(Utils.PARAM_LOCALIZACAO, editlocalizacao.getText().toString());

            ContentValues cv1 = new ContentValues();
            cv1.put(Contrato.Notas.COLUMN_TITULO, edittitulo.getText().toString());
            cv1.put(Contrato.Notas.COLUMN_DESCRICAO, editdescricao.getText().toString());
            cv1.put(Contrato.Notas.COLUMN_DATA, editdata.getText().toString());
            cv1.put(Contrato.Notas.COLUMN_LOCALIZACAO, editlocalizacao.getText().toString());
            cv1.put(Contrato.Notas.COLUMN_ID_UTILIZADOR,"1");
            db.insert(Contrato.Notas.TABLE_NAME, null, cv1);
            startActivityForResult(i, REQUEST_CODE_OP_1);

            edittitulo.setText("");
            editdescricao.setText("");
            editdata.setText("");
            editlocalizacao.setText("");
        }
    }

    public void limpar(View v){
        edittitulo.setText("");
        editdescricao.setText("");
        editdata.setText("");
        editlocalizacao.setText("");
    }

    public void consultar(View v){
        Intent i = new Intent(NotasActivity.this, Second.class);
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
                Toast.makeText(NotasActivity.this, data.getStringExtra(Utils.PARAM_OUTPUT), Toast.LENGTH_SHORT).show();
            }
        }
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
                //Toast.makeText(NotasActivity.this, "Voltar", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(NotasActivity.this, MainActivity.class);
                startActivity(i);
                return true;

            case R.id.opcao2:
                Toast.makeText(NotasActivity.this, "Sobre", Toast.LENGTH_SHORT).show();
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