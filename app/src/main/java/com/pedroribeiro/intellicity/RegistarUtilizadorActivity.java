package com.pedroribeiro.intellicity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegistarUtilizadorActivity extends AppCompatActivity {

    EditText editnome;
    EditText editusername;
    EditText editpassword;
    EditText editdt_nasc;
    EditText editmorada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registar_utilizador);

        editnome = (EditText) findViewById(R.id.nome);
        editusername = (EditText) findViewById(R.id.username);
        editpassword = (EditText) findViewById(R.id.password);
        editdt_nasc = (EditText) findViewById(R.id.dt_nasc);
        editmorada = (EditText) findViewById(R.id.morada);
    }



    // após login terá de ir para a página do mapa
    public void registar2(View v){
        Intent i = new Intent(RegistarUtilizadorActivity.this, MainActivity.class);
        /*
        i.putExtra(Utils.PARAM_TITULO, edittitulo.getText().toString());
        i.putExtra(Utils.PARAM_DESCRICAO, editdescricao.getText().toString());
        i.putExtra(Utils.PARAM_DATA, editdata.getText().toString());
        i.putExtra(Utils.PARAM_LOCALIZACAO, editlocalizacao.getText().toString());
        */
        Toast.makeText(RegistarUtilizadorActivity.this, "Utilizador registado com sucesso!", Toast.LENGTH_SHORT).show();
        startActivity(i);
        //startActivityForResult(i, REQUEST_CODE_OP_1);
    }

    // validar input de strings
    public static boolean isNullOrEmpty(String str) {
        if(str != null && !str.trim().isEmpty())
            return false;
        return true;
    }

    public void registar(View v){
        String url = "https://intellicity.000webhostapp.com/myslim_commov1920/api/registoUser";

        String nome = editnome.getText().toString();
        String username = editusername.getText().toString();
        String password = editpassword.getText().toString();
        String dt_nasc = editdt_nasc.getText().toString();
        String morada = editmorada.getText().toString();

        if(isNullOrEmpty(nome)) {
            Toast.makeText(RegistarUtilizadorActivity.this, "Tem de preencher o nome!", Toast.LENGTH_SHORT).show();
        }else if(isNullOrEmpty(username)) {
            Toast.makeText(RegistarUtilizadorActivity.this, "Tem de preencher o username!", Toast.LENGTH_SHORT).show();
        }else if(isNullOrEmpty(password)) {
            Toast.makeText(RegistarUtilizadorActivity.this, "Tem de preencher a password!", Toast.LENGTH_SHORT).show();
        }else if(isNullOrEmpty(dt_nasc)) {
            Toast.makeText(RegistarUtilizadorActivity.this, "Tem de preencher a data de nascimento!", Toast.LENGTH_SHORT).show();
        }else if(isNullOrEmpty(morada)) {
            Toast.makeText(RegistarUtilizadorActivity.this, "Tem de preencher a morada!", Toast.LENGTH_SHORT).show();
        }else {
            Map<String, String> jsonParams = new HashMap<String, String>();
            jsonParams.put( "nome" , nome);
            jsonParams.put( "username" , username);
            jsonParams.put( "password" , password);
            jsonParams.put( "data_nasc" , dt_nasc);
            jsonParams.put( "morada" , morada);

            JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, url,
                    new JSONObject(jsonParams),
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                if(response.getBoolean("status")){ //status = true ?
                                    Toast.makeText(RegistarUtilizadorActivity.this, response.getString("MSG"), Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(RegistarUtilizadorActivity.this, MainActivity.class);
                                    startActivity(i);
                                } else{
                                    Toast.makeText(RegistarUtilizadorActivity.this, response.getString("MSG"), Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException ex) { }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(RegistarUtilizadorActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
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
                Intent i = new Intent(RegistarUtilizadorActivity.this, MainActivity.class);
                startActivity(i);
                return true;

            case R.id.opcao2:
                Toast.makeText(RegistarUtilizadorActivity.this, "Sobre", Toast.LENGTH_SHORT).show();
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
    /* toolbar superior*/
}
