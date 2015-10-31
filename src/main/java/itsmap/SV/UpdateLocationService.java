package itsmap.SV;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import org.json.JSONException;
import org.json.JSONObject;

import dk.iha.itsmap.e15.grp11.SV.R;

/*
To keep the service running, and restarting if killed, we used the following link:
http://stackoverflow.com/questions/24077901/how-to-create-an-always-running-background-service

To retrieve location updates from the device we used the following guides:
http://developer.android.com/training/location/index.html
https://developers.google.com/android/guides/api-client#Starting
https://github.com/googlesamples/android-play-location/blob/master/LocationUpdates/app/src/main/java/com/google/android/gms/location/sample/locationupdates/MainActivity.java

To use shared preferences we used:
http://developer.android.com/guide/topics/data/data-storage.html#pref
http://developer.android.com/training/basics/data-storage/shared-preferences.html

To set notification icon we used:
http://developer.android.com/guide/topics/ui/notifiers/notifications.html
http://developer.android.com/training/notify-user/build-notification.html
http://stackoverflow.com/questions/5338501/android-keep-notification-steady-at-notification-bar
 */

public class UpdateLocationService extends Service implements ConnectionCallbacks, OnConnectionFailedListener, LocationListener {

    protected GoogleApiClient mGoogleApiClient;
    protected LocationRequest mLocationRequest;

    private String url = "http://rbks.dk/itsmap/dbcontact.php";

    private SharedPreferences sharedPreferences;

    private String nameOfDeviceOwner;

    private Notification mNotification;
    private NotificationManager mNotificationManager;
    private int notificationId = 001;

    public UpdateLocationService() {
    }


    @Override
    public void onCreate() {
        super.onCreate();
        // Builds the google api client which in turn creates the Location Request
        buildGoogleApiClient();
        // Builds the notification
        buildNotification();
        // Instantiates the NotificationManager
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // Inistantiates sharedpreferences
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        // Sets the name of the user
        nameOfDeviceOwner = sharedPreferences.getString("name", "nameNotFound");
    }

    /*
    Is called every time the service is started
    */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (isOnline()) {
            if (intent != null) {
                if (intent.getAction().equals("start")) {
                    Log.d("onStartCommand", "intent was start");
                    if (!mGoogleApiClient.isConnected()) {
                        mGoogleApiClient.connect();
                    }
                    // Setting the notification
                    mNotificationManager.notify(notificationId, mNotification);
                    // If the services was called from the switch, we should update the tracking status
                    int progress = intent.getIntExtra("progress", 0);
                    if (progress > 0) {
                        updateTrackingStatus(progress);
                    }
                } else if (intent.getAction().equals("stop")) {
                    Log.d("onStartCommand", "LocationS stopped");
                    // Before closing down we update the server with 0 meaning no tracking - on response the service closes
                    updateTrackingStatus(0);
                } else if (intent.getAction().equals("trackingStatusUpdate")) {
                    // Update the tracking status when the seekbar has changed
                    int progress = intent.getIntExtra("progress", 0);
                    updateTrackingStatus(progress);
                }
            }
        }
        return START_STICKY;
    }

    /*
    Builds the Google API client
     */
    protected synchronized void buildGoogleApiClient() {
        Log.d("buildAPI", "Building API");
        mGoogleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
        createLocationRequest();
    }

    /*
    Creates the location request with the desired parameters
     */
    protected void createLocationRequest() {
        Log.d("CreateLocation", "Creating request");
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000 * 60 * 5); // Preferred interval is 5 minutes
        mLocationRequest.setFastestInterval(1000 * 60 * 2); // Fastest interval is 2 minutes
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY); // We prefer high accuracy
    }

    /*
    When we are connected to the Google API, we can start updating the location
    */
    @Override
    public void onConnected(Bundle connectionHint) {
        startLocationUpdates();
    }

    /*
    Starts the location updates trough the fused location API
     */
    protected void startLocationUpdates() {
        Log.d("startLocation", "Starting LocationServices");
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    /*
    Whenever the location changes we receive the new location
     */
    @Override
    public void onLocationChanged(Location location) {
        Log.d("Location", "Location fetched on device");
        sendLocation(location);
    }

    /*
    Handles what to do when the connection is suspended; We connect again
     */
    @Override
    public void onConnectionSuspended(int cause) {
        mGoogleApiClient.connect();
    }

    /*
    Handles what to do if the connection fails
     */
    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.d("Lost connection", "Error");
    }

    /*
    Sends location info to the server side
    Inspired by the following links and a previous project in another course (Pervasive Positioning):
    https://developer.android.com/training/volley/index.html
    http://stackoverflow.com/questions/17136769/how-to-parse-jsonarray-in-android
     */
    private void sendLocation(Location location) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("action", "save");
            jsonObject.put("name", nameOfDeviceOwner);
            jsonObject.put("timestamp", location.getTime());
            jsonObject.put("latitude", location.getLatitude());
            jsonObject.put("longitude", location.getLongitude());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Here we could also use a stringRequest, but the server is optimized to use JsonObjects
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("sendLocation returned", response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError v) {
                Log.d("sendLocation error", v.toString());
            }
        });
        ServerCom.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }

    private void updateTrackingStatus(int progress) {
        final int thisProgress = progress;
        Log.d("updateTrackingStatus", "Updating tracking status");
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("action", "status");
            jsonObject.put("name", nameOfDeviceOwner);
            jsonObject.put("tracking", progress);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("UpdateTrackingStatus", response.toString());
                if (thisProgress == 0) {
                    stopAndClose();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError v) {
                Log.d("UpdateTrackingStatus", v.toString());
            }
        });
        ServerCom.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }

    private void buildNotification() {
        Log.d("buildNotification","Building notification");
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_location_on_white_24dp)
                .setContentTitle(getResources().getString(R.string.trackNotifyTitle))
                .setContentText(getResources().getString(R.string.trackNotifyTxt));

        Intent goToMainIntent = new Intent(this, HomeScreen.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, goToMainIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(pendingIntent);

        mNotification = mBuilder.build();
        mNotification.flags |= Notification.FLAG_NO_CLEAR;
    }

    public void stopAndClose() {
        // No need for further updates
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        // Remove icon
        mNotificationManager.cancel(notificationId);
        // Stop service
        stopSelf();
    }

    private boolean isOnline() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }

    /*
    Restarts the service if it is killed by using the AlarmManager and the user has permitted tracking
     */
    @Override
    public void onTaskRemoved(Intent intent) {
        boolean isTracking = sharedPreferences.getBoolean("tracking", true);
        if (isTracking) {
            Log.d("onTaskRemoved", "restarting service");
            Intent restartThisService = new Intent(getApplicationContext(), this.getClass());
            restartThisService.setAction("start");
            restartThisService.setPackage(getPackageName());
            PendingIntent restartService = PendingIntent.getService(getApplicationContext(), 1, restartThisService, PendingIntent.FLAG_ONE_SHOT);

            AlarmManager alarmManager = (AlarmManager)getApplicationContext().getSystemService(Context.ALARM_SERVICE);
            alarmManager.set(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime() + 100, restartService);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
