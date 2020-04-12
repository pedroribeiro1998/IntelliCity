package com.pedroribeiro.intellicity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofenceStatusCodes;
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
import com.google.android.gms.maps.model.MarkerOptions;
import com.pedroribeiro.intellicity.entities.Report;
import com.pedroribeiro.intellicity.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
        Toast.makeText(this,"Latitude: " + String.valueOf(latLng.latitude)
                                        +  " longitude: " + String.valueOf(latLng.longitude),
                                        Toast.LENGTH_SHORT).show();
        mMap.addMarker(new MarkerOptions()
                            .position(latLng));

        Location l = new Location("");
        l.setLatitude(latLng.latitude);
        l.setLongitude(latLng.longitude);

        //startIntentService(l);

        Intent i = new Intent(this, NotasActivity.class);
        i.putExtra(Utils.LAT, latLng.latitude);
        i.putExtra(Utils.LONG, latLng.longitude);
        i.putExtra("latitude", String.valueOf(latLng.latitude));
        i.putExtra("longitude", String.valueOf(latLng.longitude));
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


    public void my_reports(View view) {
        Toast.makeText(MapActivity.this, "My Reports", Toast.LENGTH_SHORT).show();
    }

    //pedido a funcionar direito , a ideia é funcionar como uma leyer. Selecionado? mostra todos os reports em markers
    public void all_reports(View v){
        Toast.makeText(MapActivity.this, "All Reports", Toast.LENGTH_SHORT).show();

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

    /********************************** testes para onClick mostrar os markers todos*************************************************************/
    /*private void addImageMarker(MFBImageInfo mfbii, LatLngBounds.Builder llb) {
        Bitmap bmap = MFBImageInfo.getRoundedCornerBitmap(mfbii.bitmapFromThumb(), Color.LTGRAY, RadiusImage, BorderImage, DimensionImageOverlay, DimensionImageOverlay, ActFlightMap.this);

        Marker m;

        GoogleMap map = getMap();
        if (map != null && (m = map.addMarker(new MarkerOptions()
                .position(mfbii.Location.getLatLng())
                .title(mfbii.Comment)
                .icon(BitmapDescriptorFactory.fromBitmap(bmap)))) != null) {
            m_hmImages.put(m.getId(), mfbii);
            if (llb != null)
                llb.include(mfbii.Location.getLatLng());
        }
    }*/
    /***********************************************************************************************/
    /**/
     // Adds a marker for each event with a location.
     // @param map
    /*private void addAllMarkers(GoogleMap map){
        for (HabitEvent e:events){
            if ((e.getLat()!=null && e.getLong()!=null) && (e.getLat()!=0 && e.getLong()!=0)){
                map.addMarker(new MarkerOptions()
                        .position(new LatLng(e.getLat(), e.getLong()))
                        .title(e.getHabitType()));
            }
        }
    }*/

    /***********************************************************************************************/
    /*private void setMarkers(GoogleMap mMap, List<Report> rep) {
        if (mMap == null) {
            return;
        }
        mMap.clear();
        MarkerOptions options = new MarkerOptions().position(
                new LatLng(Prefs.LastLatitude.getDouble(), Prefs.LastLongitude.getDouble()))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.current_location));
        mMap.addMarker(options);
        for (Restaurant restaurant : restaurants) {
            MarkerOptions markerOptions = new MarkerOptions().position(
                    new LatLng(restaurant.mLatitude, restaurant.mLongitude))
                    .title(restaurant.mName)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.restaurant_pin));
            Marker marker = map.addMarker(markerOptions);
            marker.setTag(restaurant);
        }
        mMap.setOnInfoWindowClickListener(mInfoWindowClickListener);
    }*/


   // https://www.programcreek.com/java-api-examples/?class=com.google.android.gms.maps.GoogleMap&method=addMarker
    /********************************** testes para onClick mostrar os markers todos*************************************************************/

/*
ImageView timg = dialog_show.findViewById(R.id.photo_show_point_dialog);
                        if(p.getImagem().trim() != "") {
                            Picasso.with(getContext()).load(WS_Server.URL() + "upload/" + p.getImagem()).into(timg);
                        }else{
                            Picasso.with(getContext()).load(WS_Server.URL() + "upload/no_img.png").into(timg);
                        }
                    dialog_show.show();

https://intellicity.000webhostapp.com/myslim_commov1920/report_photos/fotox-6.jpeg
 */


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
