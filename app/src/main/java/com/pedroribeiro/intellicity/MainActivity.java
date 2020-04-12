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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.pedroribeiro.intellicity.db.Contrato;
import com.pedroribeiro.intellicity.db.DB;
import com.pedroribeiro.intellicity.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    EditText edittitulo;
    EditText editdescricao;
    EditText editdata;
    EditText editlocalizacao;

    TextView textotitulo;
    TextView textodescricao;
    TextView textodata;
    TextView textolocalizacao;

    EditText editusername;
    EditText editpassword;

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

        editusername = (EditText) findViewById(R.id.username);
        editpassword = (EditText) findViewById(R.id.password);

        mDbHelper = new DB(this);
        db = mDbHelper.getReadableDatabase();

        //Toast.makeText(MainActivity.this, getResources().getString(R.string.bemvindo), Toast.LENGTH_SHORT).show();
    }

    public void registar_Utilizador(View view){
        Intent newActivityIntent = new Intent(this,RegistarUtilizadorActivity.class);
        startActivity(newActivityIntent);
    }

    public void verNotas(View v){
        Intent i = new Intent(MainActivity.this, NotasActivity.class);
        i.putExtra("z", "VenhoDaMain");
        startActivity(i);
    }

    // validar input de strings
    public static boolean isNullOrEmpty(String str) {
        if(str != null && !str.trim().isEmpty())
            return false;
        return true;
    }

    // após login terá de ir para a página do mapa
    public void login(View v){
        String url = "https://intellicity.000webhostapp.com/myslim_commov1920/api/loginUser";

        String username = editusername.getText().toString();
        String password = editpassword.getText().toString();

        if(isNullOrEmpty(username)) {
            Toast.makeText(MainActivity.this, "Tem de preencher o username!", Toast.LENGTH_SHORT).show();
        }else if(isNullOrEmpty(password)) {
            Toast.makeText(MainActivity.this, "Tem de preencher a password!", Toast.LENGTH_SHORT).show();
        }else{
            Map<String, String> jsonParams = new HashMap<String, String>();
            jsonParams.put( "username" , username);
            jsonParams.put( "password" , password);

            JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, url,
                    new JSONObject(jsonParams),
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                if(response.getBoolean("status")){
                                    Toast.makeText(MainActivity.this, response.getString("MSG"), Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(MainActivity.this, MapActivity.class);
                                    startActivity(i);
                                } else{
                                    Toast.makeText(MainActivity.this, response.getString("MSG"), Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException ex) { }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError{
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("Content-Type", "application/json; charset=utf-8");
                    headers.put("User-agent", System.getProperty("http.agent"));
                    return headers;
                }
            };
            // Access the RequestQueue through your singleton class.
            MySingleton.getInstance(this).addToRequestQueue(postRequest);
        }


        /*
        i.putExtra(Utils.PARAM_TITULO, edittitulo.getText().toString());
        i.putExtra(Utils.PARAM_DESCRICAO, editdescricao.getText().toString());
        i.putExtra(Utils.PARAM_DATA, editdata.getText().toString());
        i.putExtra(Utils.PARAM_LOCALIZACAO, editlocalizacao.getText().toString());
        */
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





