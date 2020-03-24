package com.pedroribeiro.intellicity;

import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.google.android.gms.wearable.DataMap.TAG;

public class FetchCoordinatesIntentService extends IntentService {
    /**
     * The receiver where results are forwarded from this service.
     */
    protected ResultReceiver mReceiver;

    /**
     * This constructor is required, and calls the super IntentService(String)
     * constructor with the name for a worker thread.
     */
    public FetchCoordinatesIntentService() {
        // Use the TAG to name the worker thread.
        super("FetchCoordinatesIntentService");
    }

    /**
     * Sends a resultCode and message to the receiver.
     */
    private void deliverResultToReceiver(int resultCode, ArrayList resultado) {
        Bundle bundle = new Bundle();
        if(resultado.size()>0) {
            bundle.putDouble(Constants.LATITUDE, Double.valueOf(resultado.get(0).toString()));
            bundle.putDouble(Constants.LONGITUDE, Double.valueOf(resultado.get(1).toString()));
        }
        mReceiver.send(resultCode, bundle);
    }

    /**
     * Tries to get the location address using a Geocoder. If successful, sends an address to a
     * result receiver. If unsuccessful, sends an error message instead.
     * Note: We define a {@link android.os.ResultReceiver} in * MainActivity to process content
     * sent from this service.
     *
     * This service calls this method from the default worker thread with the intent that started
     * the service. When this method returns, the service automatically stops.
     */
    @Override
    protected void onHandleIntent(Intent intent) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        String errorMessage = "";

        // Get the location passed to this service through an extra.
        //Location location = intent.getParcelableExtra(Constants.LOCATION_DATA_EXTRA);
        String myLocation = intent.getStringExtra(Constants.LOCATION_DATA_EXTRA);

        mReceiver = intent.getParcelableExtra(Constants.RECEIVER);

        // Address found using the Geocoder.
        List<Address> addresses = null;

        try {
            // nome da cidade que estamos a pesquisar
            addresses = geocoder.getFromLocationName(myLocation, 5);

        } catch (IOException ioException) {
            // Catch network or other I/O problems.
            errorMessage = getString(R.string.service_not_available);
            Log.e(TAG, errorMessage, ioException);
        } catch (IllegalArgumentException illegalArgumentException) {
            // Catch invalid latitude or longitude values.
            // Send message invalid parameters
            errorMessage = getString(R.string.invalid_parameters);
            Log.e(TAG, errorMessage + " !! ");
        }

        // Handle case where no address was found.
        if (addresses == null || addresses.size()  == 0) {
            if (errorMessage.isEmpty()) {
                errorMessage = getString(R.string.no_address_found);
                Log.e(TAG, errorMessage);
            }
            deliverResultToReceiver(Constants.FAILURE_RESULT, new ArrayList());
        } else {
            Address address = addresses.get(0);
            double longitude = address.getLongitude();
            double latitude = address.getLatitude();

            Log.i(TAG,"Endereço encontrado!");
            ArrayList resultado = new ArrayList();
            resultado.add(latitude);
            resultado.add(longitude);

            deliverResultToReceiver(Constants.SUCCESS_RESULT, resultado);
        }
    }

}

