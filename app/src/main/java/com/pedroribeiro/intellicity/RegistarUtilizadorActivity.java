package com.pedroribeiro.intellicity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class RegistarUtilizadorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registar_utilizador);
    }



    // após login terá de ir para a página do mapa
    public void registar(View v){
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
