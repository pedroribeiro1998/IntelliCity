package com.pedroribeiro.intellicity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.pedroribeiro.intellicity.entities.Report;
import com.pedroribeiro.intellicity.entities.Utilizador;
import com.pedroribeiro.intellicity.utils.Utils;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener,
GoogleMap.OnMapLongClickListener, LocationListener, GoogleApiClient.ConnectionCallbacks,
GoogleApiClient.OnConnectionFailedListener, ResultCallback<Status> {

    GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest;
    private AddressResultReceiver mResultReceiver;
    List<Geofence> mGeofenceList;
    PendingIntent mGeofencePendingIntent;

    List<Report> reports_detalhe_List;
    List<Report> others_reports_detalhe_List;
    List<Report> my_reports_detalhe_List;
    List<Report> pontos; //createMarkers

    List<Marker> markerListOthers;
    List<Marker> markerListMy;

    List<Utilizador> logged_user_List;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        mResultReceiver = new AddressResultReceiver(new Handler());
        
        // a partir da api 12
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this); // prepara o mapa e retorna para o método onMapReady

        mLocationRequest = new LocationRequest();
        // iniciar o serviço do google play
        buildGoogleApiClient();
        //createGeoFence();

        reports_detalhe_List = new ArrayList<>();
        others_reports_detalhe_List = new ArrayList<>();
        my_reports_detalhe_List = new ArrayList<>();

        markerListOthers = new ArrayList<Marker>();
        markerListMy = new ArrayList<Marker>();

        //logged_user_List = new ArrayList<>();
        logged_user_List = ((List<Utilizador>) getIntent().getExtras().getSerializable("UTILIZADOR_LIST"));


    }

    public void onResult(Status status) {
        if(status.isSuccess()){
            // Update state and save in shared preferences
            Toast.makeText(
                    this,
                    "geofence added",
                    Toast.LENGTH_SHORT
            ).show();
        }else{
            Toast.makeText(
                    this,
                    "geofence error",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }

    private GeofencingRequest getGeofencingRequest(){
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        builder.addGeofences(mGeofenceList);
        return builder.build();
    }

    private void createGeoFence() {
        mGeofenceList.add(new Geofence.Builder()
               // set the request id of the geofence. This is a string to indentify this geofence
               .setRequestId("GEOFENCE-1")
                .setCircularRegion(
                        41.1,
                        -8.14,
                        Constants.GEOFENCE_RADIUS_IN_METERS
                )
                .setExpirationDuration(Constants.GEOFENCE_EXPIRATION_IN_MILLISECONDS)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT)
                .build());
        addGeofence();
    }

    private void addGeofence() {
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                                                    != PackageManager.PERMISSION_GRANTED){
            //check permissions now
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
        }else{
            LocationServices.GeofencingApi.addGeofences(
                    mGoogleApiClient,
                    getGeofencingRequest(),
                    getGeofencePendingIntent()
            ).setResultCallback(this);
        }
    }

    private PendingIntent getGeofencePendingIntent(){
        // reuse the PendingIntent if we already have it
        if(mGeofencePendingIntent != null){
            return mGeofencePendingIntent;
        }
        Intent intent = new Intent(this, GeofenceTransitionsIntentService.class);
        // we use FLAG_UPDATE_CURRENT so that we get the same pending intent back wher
        // calling addGeofences() and removeGeofences()
        return PendingIntent.getService(this, 0, intent, PendingIntent.
                FLAG_UPDATE_CURRENT);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;
        LatLng home = new LatLng(41.63830638,-8.75326574);
        map.addMarker(new MarkerOptions()
                            .position(home)
                            .title("Esta é a minha casa!"));
        map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

        map.setOnMapClickListener(this);
        map.setOnMapLongClickListener(this);

        //map.moveCamera(CameraUpdateFactory.newLatLngZoom(home,18));
        focusMapa(home);
    }

    private void focusMapa(LatLng latLng){
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latLng)   // sets the center of the map to mountain view
                .zoom(19)       // sets the zoom
                .bearing(45)    // sets the orientation of the camera to east
                .tilt(70)       // sets the tilt of the camera to 60 degrees
                .build();       // creates a CameraPosition from the builder
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    @Override
    public void onMapClick(LatLng latLng) {
        Toast.makeText(this,"Latitude: " + String.valueOf(latLng.latitude)
                        +  " longitude: " + String.valueOf(latLng.longitude),
                Toast.LENGTH_SHORT).show();
        /*Intent i = new Intent(this, StreetViewActivity.class);
        i.putExtra(Utils.LAT, latLng.latitude);
        i.putExtra(Utils.LONG, latLng.longitude);
        startActivity(i);*/
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        //logged_user_List = ((List<Utilizador>) getIntent().getExtras().getSerializable("UTILIZADOR_LIST"));

        //Toast.makeText(this,"Latitude: " + String.valueOf(latLng.latitude)+  " longitude: " + String.valueOf(latLng.longitude),Toast.LENGTH_SHORT).show();
        mMap.addMarker(new MarkerOptions().position(latLng));

        Location l = new Location("");
        l.setLatitude(latLng.latitude);
        l.setLongitude(latLng.longitude);

        //startIntentService(l);

        Intent i = new Intent(this, NotasActivity.class);
        i.putExtra(Utils.LAT, latLng.latitude);
        i.putExtra(Utils.LONG, latLng.longitude);
        i.putExtra("latitude", String.valueOf(latLng.latitude));
        i.putExtra("longitude", String.valueOf(latLng.longitude));
        i.putExtra("UTILIZADOR_LIST", (Serializable) logged_user_List);
        startActivity(i);
    }

    private void createLocationRequest() {
        //mLocationRequest = LocationRequest.create();
        mLocationRequest.setInterval(10000); // 10000 milissegundos = 10 segundos
        mLocationRequest.setFastestInterval(10000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    protected synchronized void buildGoogleApiClient() {
        // construir o cliente da api do google que será iniciado no onStart()
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        createLocationRequest();
    }

    @Override
    protected void onStart() {
        super.onStart();
        // iniciar o serviço da google play
        mGoogleApiClient.connect();
    }

     @Override
     public void onConnected(Bundle connectionHint){
        // momento em que o serviço do google play é comectado ( depois de mGoogleApiClient.connect(); )
         startLocationUpdates();
         //startIntentCoordinatesService();
     }

    private void startLocationUpdates() {
        // pedido de sinal propriamente dito
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED){
            // check permissions now
            ActivityCompat.requestPermissions(this,
                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
        }else{
            //LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
            mMap.setMyLocationEnabled(true); //quadradinho para obter localização
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        //aqui podemos gravar para a bd, centrar mapa nesse ponto, etc
        Toast.makeText(this, "gps recebido -> lat: " + location.getLatitude() + " || long: " + location.getLongitude(), Toast.LENGTH_SHORT).show();
        //LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }

    /********************************** onClick mostrar os markers *************************************************************/

    public void my_reports(View view) {
        Toast.makeText(MapActivity.this, "My Reports", Toast.LENGTH_SHORT).show();
        //logged_user_List = ((List<Utilizador>) getIntent().getExtras().getSerializable("UTILIZADOR_LIST"));

        Utilizador us = logged_user_List.get(0);
        String nome = us.nome;
        int id = us.id;
        String username = us.username;

        String url = "https://intellicity.000webhostapp.com/myslim_commov1920/api/reports_detalhe/my/" + id;
        JsonArrayRequest jsObjRequest = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            if(my_reports_detalhe_List.size()==0) {
                                for (int i = 0; i < response.length(); i++) {
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
                                    my_reports_detalhe_List.add(report);
                                }
                                CreateMyMarkers(my_reports_detalhe_List);

                            }else{
                                for (Marker m : markerListMy) {
                                    for(Report p : my_reports_detalhe_List){
                                        if(m.getTitle().equals(p.titulo)){
                                            m.remove();
                                        }
                                    }
                                }
                                my_reports_detalhe_List.removeAll(my_reports_detalhe_List);
                            }

                            //nextActivity(reports_detalhe_List);

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

    private void CreateMyMarkers(List<Report> points) {
        Report pontos;
        this.pontos = points;
        for (Report p : points) {
            LatLng latLng = new LatLng(Double.parseDouble(p.getLatitude()), Double.parseDouble(p.getLongitude()));
            MarkerOptions markerOptions_my = new MarkerOptions();

            // Setting the position for the marker
            markerOptions_my.position(latLng);
            //markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));


            // Setting the title for the marker.
            // This will be displayed on taping the marker
            markerOptions_my.title(p.getTitulo());
            // Clears the previously touched position
            //map.clear();

            // Animating to the touched position
            mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));

            // Placing a marker on the touched position
            Marker m = mMap.addMarker(markerOptions_my);
            m.setTag(p);
            markerListMy.add(m);
            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    Report p = (Report) marker.getTag();
                    if (p != null) {
                        Dialog dialog = new Dialog(getContext());
                        dialog.setCancelable(true);
                        dialog.setContentView(R.layout.dialog_report_info_my);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                        Button gravar_alteracao_show_point_dialog = dialog.findViewById(R.id.gravar_alteracao_show_point_dialog);
                        Button updateReport = dialog.findViewById(R.id.updateReport);
                        Button deleteReport = dialog.findViewById(R.id.deleteReport);

                        EditText ttitle = dialog.findViewById(R.id.title_show_point_dialog);
                        ttitle.setText(p.getTitulo());

                        EditText tdescricao = dialog.findViewById(R.id.text_show_point_dialog);
                        tdescricao.setText(p.getDescricao());

                        EditText tlocalizacao = dialog.findViewById(R.id.localizacao_show_point_dialog);
                        tlocalizacao.setText(p.getLocalizacao());

                        TextView tdata = dialog.findViewById(R.id.data_show_point_dialog);
                        tdata.setText(p.getData());

                        TextView tusername = dialog.findViewById(R.id.username_show_point_dialog);
                        tusername.setText("Reportado por: " + p.getNome());

                        ImageView timg = dialog.findViewById(R.id.photo_show_point_dialog);
                        String imageUri = "https://intellicity.000webhostapp.com/myslim_commov1920/report_photos/" + p.getFotografia();
                        String imageError = "https://intellicity.000webhostapp.com/myslim_commov1920/report_photos/Xerror.png";
                        if (p.getFotografia().trim() != "") {
                            Picasso.with(getContext()).load(imageUri).into(timg);
                        } else {
                            Picasso.with(getContext()).load(imageError).into(timg);
                        }

                        int id_do_report = p.getId();

                        deleteReport.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String url = "https://intellicity.000webhostapp.com/myslim_commov1920/api/reports/deleteReport/" + id_do_report;

                                JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, url,
                                        new JSONObject(),
                                        new Response.Listener<JSONObject>() {
                                            @Override
                                            public void onResponse(JSONObject response) {
                                                try {
                                                    if(response.getBoolean("status")){
                                                        Toast.makeText(MapActivity.this, response.getString("MSG"), Toast.LENGTH_SHORT).show();
                                                        Intent i = new Intent(MapActivity.this, MapActivity.class);
                                                        i.putExtra("UTILIZADOR_LIST", (Serializable) logged_user_List);
                                                        startActivity(i);
                                                    } else{
                                                        Toast.makeText(MapActivity.this, response.getString("MSG"), Toast.LENGTH_SHORT).show();
                                                    }
                                                } catch (JSONException ex) { }
                                            }
                                        },
                                        new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                Toast.makeText(MapActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
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
                                MySingleton.getInstance(getContext()).addToRequestQueue(postRequest);
                            }
                        });

                        updateReport.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ttitle.setEnabled(true);
                                tdescricao.setEnabled(true);
                                tlocalizacao.setEnabled(true);
                                gravar_alteracao_show_point_dialog.setVisibility(View.VISIBLE);
                            }
                        });

                        gravar_alteracao_show_point_dialog.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String url = "https://intellicity.000webhostapp.com/myslim_commov1920/api/reports/updateReport/" + id_do_report;

                                String tituloUpdated = ttitle.getText().toString();
                                String descricaoUpdated = tdescricao.getText().toString();
                                String localizacaoUpdated = tlocalizacao.getText().toString();

                                if(isNullOrEmpty(tituloUpdated)) {
                                    Toast.makeText(MapActivity.this, "Tem de preencher o título!", Toast.LENGTH_SHORT).show();
                                }else if(isNullOrEmpty(descricaoUpdated)) {
                                    Toast.makeText(MapActivity.this, "Tem de preencher a descrição!", Toast.LENGTH_SHORT).show();
                                }else if(isNullOrEmpty(localizacaoUpdated)) {
                                    Toast.makeText(MapActivity.this, "Tem de preencher a localização!", Toast.LENGTH_SHORT).show();
                                }else{
                                    Map<String, String> jsonParams = new HashMap<String, String>();
                                    jsonParams.put( "titulo" , tituloUpdated);
                                    jsonParams.put( "descricao" , descricaoUpdated);
                                    jsonParams.put( "localizacao" , localizacaoUpdated);

                                    JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, url,
                                            new JSONObject(jsonParams),
                                            new Response.Listener<JSONObject>() {

                                                @Override
                                                public void onResponse(JSONObject response) {
                                                    try {
                                                        if(response.getBoolean("status")){

                                                            Toast.makeText(MapActivity.this, response.getString("MSG"), Toast.LENGTH_SHORT).show();
                                                            Intent i = new Intent(MapActivity.this, MapActivity.class);
                                                            i.putExtra("UTILIZADOR_LIST", (Serializable) logged_user_List);
                                                            startActivity(i);
                                                        } else{
                                                            Toast.makeText(MapActivity.this, response.getString("MSG"), Toast.LENGTH_SHORT).show();
                                                        }
                                                    } catch (JSONException ex) { }
                                                }
                                            },
                                            new Response.ErrorListener() {
                                                @Override
                                                public void onErrorResponse(VolleyError error) {
                                                    Toast.makeText(MapActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
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
                                    MySingleton.getInstance(getContext()).addToRequestQueue(postRequest);
                                    ttitle.setEnabled(false);
                                    tdescricao.setEnabled(false);
                                    tlocalizacao.setEnabled(false);
                                    gravar_alteracao_show_point_dialog.setVisibility(View.GONE);
                                }
                            }
                        });
                        dialog.show();
                    }
                    return false;
                }
            });
        }
    }

    // validar input de strings
    public static boolean isNullOrEmpty(String str) {
        if(str != null && !str.trim().isEmpty())
            return false;
        return true;
    }

    public void all_reports(View v){
        Toast.makeText(MapActivity.this, "All Reports", Toast.LENGTH_SHORT).show();
        //logged_user_List = ((List<Utilizador>) getIntent().getExtras().getSerializable("UTILIZADOR_LIST"));

        Utilizador us = logged_user_List.get(0);
        String nome = us.nome;
        int id = us.id;
        String username = us.username;

        String url = "https://intellicity.000webhostapp.com/myslim_commov1920/api/reports_detalhe/others/" + id;;
        JsonArrayRequest jsObjRequest = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            if(others_reports_detalhe_List.size()==0){
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
                                    others_reports_detalhe_List.add(report);
                                }
                                CreateOthersMarkers(others_reports_detalhe_List);
                            }else{
                                for (Marker m : markerListOthers) {
                                    for(Report p : others_reports_detalhe_List){
                                        if(m.getTitle().equals(p.titulo)){
                                            m.remove();
                                        }
                                    }
                                }
                                others_reports_detalhe_List.removeAll(others_reports_detalhe_List);
                            }

                            //nextActivity(reports_detalhe_List);


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

    //works very fine welelele e cria dialog para mostrar os dados do marker
    public void CreateOthersMarkers(List<Report> points) {
        Report pontos;
        this.pontos = points;
        for (Report p : points) {
            LatLng latLng = new LatLng(Double.parseDouble(p.getLatitude()), Double.parseDouble(p.getLongitude()));
            MarkerOptions markerOptions_others = new MarkerOptions();

            // Setting the position for the marker
            markerOptions_others.position(latLng);
            markerOptions_others.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));


            // Setting the title for the marker.
            // This will be displayed on taping the marker
            markerOptions_others.title(p.getTitulo());
            // Clears the previously touched position
            //map.clear();

            // Animating to the touched position
            mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));

            // Placing a marker on the touched position
            Marker m = mMap.addMarker(markerOptions_others);
            m.setTag(p);
            markerListOthers.add(m);
            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    Report p = (Report) marker.getTag();
                    if (p != null) {
                        Dialog dialog = new Dialog(getContext());
                        dialog.setCancelable(true);
                        dialog.setContentView(R.layout.dialog_report_info_others);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                        EditText ttitle = dialog.findViewById(R.id.title_show_point_dialog);
                        ttitle.setText(p.getTitulo());

                        EditText tdescricao = dialog.findViewById(R.id.text_show_point_dialog);
                        tdescricao.setText(p.getDescricao());

                        EditText tlocalizacao = dialog.findViewById(R.id.localizacao_show_point_dialog);
                        tlocalizacao.setText(p.getLocalizacao());

                        TextView tdata = dialog.findViewById(R.id.data_show_point_dialog);
                        tdata.setText(p.getData());

                        TextView tusername = dialog.findViewById(R.id.username_show_point_dialog);
                        tusername.setText("Reportado por: " + p.getNome());

                        ImageView timg = dialog.findViewById(R.id.photo_show_point_dialog);
                        String imageUri = "https://intellicity.000webhostapp.com/myslim_commov1920/report_photos/" + p.getFotografia();
                        String imageError = "https://intellicity.000webhostapp.com/myslim_commov1920/report_photos/Xerror.png";
                        if (p.getFotografia().trim() != "") {
                            Picasso.with(getContext()).load(imageUri).into(timg);
                        } else {
                            Picasso.with(getContext()).load(imageError).into(timg);
                        }
                        dialog.show();

                    }
                    return false;
                }
            });
        }
    }

    private Context getContext() {
        return this;
    }

    /********************************** onClick mostrar os markers *************************************************************/


    protected void startIntentService(Location location){
        Intent intent = new Intent(this, FetchAddressIntentService.class);
        intent.putExtra(Constants.RECEIVER, mResultReceiver);
        intent.putExtra(Constants.LOCATION_DATA_EXTRA, location);
        startService(intent);
    }

    protected void startIntentCoordinatesService(){
        Intent intent = new Intent(this, FetchCoordinatesIntentService.class);
        intent.putExtra(Constants.LOCATION_DATA_EXTRA, "Viana do Castelo, Portugal");
        intent.putExtra(Constants.RECEIVER, mResultReceiver);
        startService(intent);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {    }

    @Override
    public void onProviderEnabled(String provider) {    }

    @Override
    public void onProviderDisabled(String provider) {    }

    @Override
    public void onConnectionSuspended(int i) {    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
        if(requestCode == 0){
            if(grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_DENIED){
                if(requestCode == 0){
                    startLocationUpdates();
                }
            } else{
                // permission was denied or request was cancelled
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
                Toast.makeText(MapActivity.this, "Sessão terminada! Obrigado.", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(MapActivity.this, MainActivity.class);
                startActivity(i);
                return true;

            case R.id.opcao2:
                Toast.makeText(MapActivity.this, "Sobre", Toast.LENGTH_SHORT).show();
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

    @Override
    protected void onPause() {
        super.onPause();
        //parar
    }

    @Override
    protected void onResume() {
        super.onResume();
        //ativar
    }

    class AddressResultReceiver extends ResultReceiver{
        public AddressResultReceiver(Handler handler){
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData){
            if(resultData.containsKey(Constants.RESULT_DATA_KEY)){
                // display the address string
                // or an error message sent from the intent service
                String mAddressOutput = resultData.getString(Constants.RESULT_DATA_KEY);
                Toast.makeText(MapActivity.this, mAddressOutput, Toast.LENGTH_SHORT).show();

                // show a toast message if an address was found
                /*if(resultCode == Constants.SUCCESS_RESULT){
                    Toast.makeText(MapActivity.this, getString(R.string.address_found), Toast.LENGTH_SHORT).show();
                }*/
            }
            if(resultData.containsKey(Constants.LATITUDE)){
                LatLng l = new LatLng(
                        resultData.getDouble(Constants.LATITUDE),
                        resultData.getDouble(Constants.LONGITUDE)
                );
                focusMapa(l);
            }
        }
    }
}
