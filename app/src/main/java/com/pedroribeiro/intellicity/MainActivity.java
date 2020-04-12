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
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.pedroribeiro.intellicity.db.Contrato;
import com.pedroribeiro.intellicity.db.DB;
import com.pedroribeiro.intellicity.entities.Report;
import com.pedroribeiro.intellicity.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    EditText editusername;
    EditText editpassword;

    List<Report> reports_detalhe_List;

    private int REQUEST_CODE_OP_1 = 1;

    //DB mDbHelper;
    //SQLiteDatabase db;
    //Cursor c, c_pessoas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editusername = (EditText) findViewById(R.id.username);
        editpassword = (EditText) findViewById(R.id.password);

        //mDbHelper = new DB(this);
        //db = mDbHelper.getReadableDatabase();

        reports_detalhe_List = new ArrayList<>();
    }

    public void registar_Utilizador(View view){
        Intent newActivityIntent = new Intent(this,RegistarUtilizadorActivity.class);
        startActivity(newActivityIntent);
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

    //a funcionar direito Post Request to list all reports
    public void consultar(View v){
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
                                String latitude = obj.getString("latitude");
                                String longitude = obj.getString("longitude");

                                Report report = new Report(id, utilizador_id, titulo, descricao, data, localizacao, fotografia, latitude, longitude);
                                reports_detalhe_List.add(report);
                            }
                            nextActivity(reports_detalhe_List);

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

    // enviar reports_detalhe_List para mostrar na activity
    private void nextActivity(List<Report> reports_detalhe_list) {
        Intent intent = new Intent(MainActivity.this, SecondFromMainActivity.class);
        intent.putExtra("REPORTS_LIST", (Serializable) reports_detalhe_List);
        this.startActivity(intent);
    }
}





