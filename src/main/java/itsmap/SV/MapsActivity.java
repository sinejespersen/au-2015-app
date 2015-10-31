package itsmap.SV;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import dk.iha.itsmap.e15.grp11.SV.R;
/*
In this class the following links have been used:
https://developers.google.com/maps/documentation/android-api/start
http://developer.android.com/training/location/display-address.html
http://stackoverflow.com/questions/17136769/how-to-parse-jsonarray-in-android
https://developers.google.com/maps/documentation/android-api/marker
https://developers.google.com/maps/documentation/android-api/views
http://stackoverflow.com/questions/14157536/how-do-i-set-default-location-and-zoom-level-for-google-map-api-v2
http://stackoverflow.com/questions/11798620/saving-the-state-of-fragments
http://stackoverflow.com/questions/16236439/restoring-map-state-position-and-markers-of-google-maps-v2-on-rotate-and-on
http://stackoverflow.com/questions/13900322/badparcelableexception-in-google-maps-code/24651246#24651246
 */

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private String url = "http://rbks.dk/itsmap/dbcontact.php/getLocation";
    private LatLng startPos = new LatLng(56.172578, 10.1900209); // Middle of IT-byen
    private int zoomLevel = 16;
    private SharedPreferences sharedPreferences;
    private String nameOfDeviceOwner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        // Sets the name of the user
        nameOfDeviceOwner = sharedPreferences.getString("name", "nameNotFound");
    }

    @Override
    protected void onStop() {
        super.onStop();
        ServerCom.getInstance(this).getRequestQueue().cancelAll(this);
        Log.d("Maps onStop", "RequestQueue cancelled");
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        fetchAndSet(mMap);
    }

    // Inspired from the same sources as in UpdateLocationService
    private void fetchAndSet(final GoogleMap mMap) {
        if (isOnline()) {
            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    Log.d("getLocation response", response.toString());
                    try {
                        for (int i = 0; i < response.length(); i++) {
                            boolean allInfo = true;
                            JSONObject jsonObject = response.getJSONObject(i);
                            String name = jsonObject.getString("name");
                            if (name.equals(nameOfDeviceOwner)) {
                                name = "You";
                            }
                            float color = 240; // Default color of marker is blue, but will always be set to proper color
                            if (name.isEmpty()) {
                                allInfo = false;
                            }
                            LatLng pos;
                            double latitude = 0;
                            double longitude = 0;
                            try {
                                latitude = jsonObject.getDouble("latitude");
                                longitude = jsonObject.getDouble("longitude");
                            } catch (NumberFormatException e) {
                                Log.d("fetchAndSet response", "Not doubles");
                                allInfo = false;
                            }
                            int tracking = jsonObject.getInt("tracking");
                            Log.d("fetchAndSet color", Integer.toString(tracking));
                            switch (tracking) {
                                case 1:
                                    color = 0;
                                    break;
                                case 2:
                                    color = 45;
                                    break;
                                case 3:
                                    color = 98;
                                    break;
                            }
                            if (allInfo) {
                                pos = new LatLng(latitude, longitude);
                                mMap.addMarker(new MarkerOptions()
                                        .position(pos)
                                        .title(name)
                                        .icon(BitmapDescriptorFactory.defaultMarker(color)));
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(startPos, zoomLevel));

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError v) {
                    Log.d("getLocation error", v.toString());
                }
            });
            ServerCom.getInstance(this).addToRequestQueue(jsonArrayRequest);
        } else {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.toastOnMaps), Toast.LENGTH_LONG).show();
        }
    }

    private boolean isOnline() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }
}
