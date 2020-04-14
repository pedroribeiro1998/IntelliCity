package com.pedroribeiro.intellicity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.text.Editable;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.pedroribeiro.intellicity.adapters.ReportsListAdapter;
import com.pedroribeiro.intellicity.db.Contrato;
import com.pedroribeiro.intellicity.db.DB;
import com.pedroribeiro.intellicity.entities.Report;
import com.pedroribeiro.intellicity.entities.Utilizador;
import com.pedroribeiro.intellicity.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NotasActivity extends AppCompatActivity {

    private static final String TAG = "";
    EditText edittitulo;
    EditText editdescricao;
    EditText editlocalizacao;
    EditText editcoordenadas;

    private int REQUEST_CODE_OP_1 = 1;

    //DB mDbHelper;
    //SQLiteDatabase db;
    //Cursor c, c_pessoas;

    //recyclerView para listar os reports da BD
    List<Report> reports_detalhe_List;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter rvAdapter;
    private RecyclerView.LayoutManager layoutManager;
    ReportsListAdapter.ItemClickListener itemClickListener;

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_TAKE_PHOTO = 1;
    String currentPhotoPath;
    ImageView imageView_teste;
    private Button takePictureButton;
    private ImageView imageView;
    Uri file;
    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;

    private Bitmap mImageBitmap;
    private String mCurrentPhotoPath;

    String latitude = "0";
    String longitude = "0";

    List<Utilizador> logged_user_List;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notas);

        edittitulo = (EditText) findViewById(R.id.edittitulo);
        editdescricao = (EditText) findViewById(R.id.editdescricao);
        editlocalizacao = (EditText) findViewById(R.id.editlocalizacao);
        takePictureButton = (Button) findViewById(R.id.button_image);
        imageView = (ImageView) findViewById(R.id.imageView);
        editcoordenadas = (EditText) findViewById(R.id.editcoordenadas);

        latitude = getIntent().getStringExtra("latitude");
        longitude = getIntent().getStringExtra("longitude");
        String coordinates = latitude + " | " + longitude;
        editcoordenadas.setText(coordinates);

        //mDbHelper = new DB(this);
        //db = mDbHelper.getReadableDatabase();

        reports_detalhe_List = new ArrayList<>();
        logged_user_List = ((List<Utilizador>) getIntent().getExtras().getSerializable("UTILIZADOR_LIST"));
    }

    // tratar da fotografia do report
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            } else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    private String setPhotoBase64_bit(Bitmap bitmapm) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmapm.compress(Bitmap.CompressFormat.JPEG, 50, baos);

            byte[] byteArrayImage = baos.toByteArray();
            String imagebase64string = Base64.encodeToString(byteArrayImage,Base64.DEFAULT);
            return imagebase64string;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            try {
                mImageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.parse(mCurrentPhotoPath));
                imageView.setImageBitmap(mImageBitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
            String urlPhoto = String.valueOf(Uri.parse(mCurrentPhotoPath));
            Utils.passarURLphoto = urlPhoto;
        }
    }

    public void dispatchTakePictureIntent(View view) {
        //estas políticas foram adicionadas antes de iniciar a camera para isto funcionar em android versão 6 e 7
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Log.i(TAG, "IOException");
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  // prefix
                ".jpg",         // suffix
                storageDir      // directory
        );
        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }
    // tratar da fotografia do report


    // validar input de strings
    public static boolean isNullOrEmpty(String str) {
        if(str != null && !str.trim().isEmpty())
            return false;
        return true;
    }

    // Post Request to save report on server database and create image on server directory
    public void guardar(View v){
        //String url = "http://localhost:8088/myslim_commov1920/api/reports/registoReport";
        String url = "https://intellicity.000webhostapp.com/myslim_commov1920/api/reports/registoReport";

        //logged_user_List = ((List<Utilizador>) getIntent().getExtras().getSerializable("UTILIZADOR_LIST"));
        Utilizador us = logged_user_List.get(0);
        int id = us.id;

        String titulo = edittitulo.getText().toString();
        String descricao = editdescricao.getText().toString();
        String localizacao = editlocalizacao.getText().toString();


        String fotografia = setPhotoBase64_bit(mImageBitmap);
        //Log.d("fotografia",fotografia);

        latitude = getIntent().getStringExtra("latitude");
        longitude = getIntent().getStringExtra("longitude");


        if(isNullOrEmpty(titulo)) {
            Toast.makeText(NotasActivity.this, "Tem de preencher o titulo!", Toast.LENGTH_SHORT).show();
        }else if(isNullOrEmpty(descricao)) {
            Toast.makeText(NotasActivity.this, "Tem de preencher o descricao!", Toast.LENGTH_SHORT).show();
        }else if(isNullOrEmpty(localizacao)) {
            Toast.makeText(NotasActivity.this, "Tem de preencher a localizacao!", Toast.LENGTH_SHORT).show();
        }else if(isNullOrEmpty(fotografia)) {
            Toast.makeText(NotasActivity.this, "Tem de tirar uma fotografia!", Toast.LENGTH_SHORT).show();
        }else {
            Map<String, String> jsonParams = new HashMap<String, String>();
            jsonParams.put( "utilizador_id" , String.valueOf(id));
            jsonParams.put( "titulo" , titulo);
            jsonParams.put( "descricao" , descricao);
            jsonParams.put( "localizacao" , localizacao);
            jsonParams.put( "fotografia" , fotografia);
            jsonParams.put( "latitude" , String.valueOf(latitude));
            jsonParams.put( "longitude" , String.valueOf(longitude));

            JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, url,
                    new JSONObject(jsonParams),
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                if(response.getBoolean("status")){ //status = true ?
                                    Toast.makeText(NotasActivity.this, response.getString("MSG"), Toast.LENGTH_SHORT).show();
                                    //Toast.makeText(NotasActivity.this, response.getString("data"), Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(NotasActivity.this, MapActivity.class);
                                    i.putExtra("UTILIZADOR_LIST", (Serializable) logged_user_List);
                                    startActivity(i);
                                } else{
                                    Toast.makeText(NotasActivity.this, response.getString("MSG"), Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException ex) { }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(NotasActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
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
    }

    public void backToMap(View v){
        //logged_user_List = ((List<Utilizador>) getIntent().getExtras().getSerializable("UTILIZADOR_LIST"));

        edittitulo.setText("");
        editdescricao.setText("");
        editlocalizacao.setText("");

        Intent i = new Intent(NotasActivity.this, MapActivity.class);
        i.putExtra("UTILIZADOR_LIST", (Serializable) logged_user_List);
        startActivity(i);
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

                                Report report = new Report(id, nome, utilizador_id, titulo, descricao, data, localizacao, fotografia, latitude, longitude);
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
        Intent intent = new Intent(NotasActivity.this, Second.class);
        intent.putExtra("REPORTS_LIST", (Serializable) reports_detalhe_List);
        intent.putExtra("UTILIZADOR_LIST", (Serializable) logged_user_List);
        this.startActivity(intent);
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
                Toast.makeText(NotasActivity.this, "Sessão terminada! Obrigado.", Toast.LENGTH_SHORT).show();
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
    /* toolbar superior*/
}