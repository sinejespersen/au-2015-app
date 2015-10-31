package itsmap.SV;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import dk.iha.itsmap.e15.grp11.SV.R;

/*
Inspired by the following links:
http://developer.android.com/training/monitoring-device-state/connectivity-monitoring.html
http://developer.android.com/training/basics/data-storage/shared-preferences.html

To use shared preferences we used:
http://developer.android.com/guide/topics/data/data-storage.html#pref
http://developer.android.com/training/basics/data-storage/shared-preferences.html
http://stackoverflow.com/questions/23024831/android-shared-preferences-example
http://stackoverflow.com/questions/13558550/can-i-get-data-from-shared-preferences-inside-a-service

For the seekbar we used:
http://developer.android.com/reference/android/widget/SeekBar.html
http://stackoverflow.com/questions/15969877/save-seekbar-progress-in-preferences
http://stackoverflow.com/questions/19205547/implementing-simple-seekbar-in-android
http://stackoverflow.com/questions/8615681/how-to-change-seek-bar-progress-colour-according-to-its-progress
http://stackoverflow.com/questions/31590714/getcolorint-id-deprecated-on-android-6-0-marshmallow-api-23

For the switch we used:
http://developer.android.com/reference/android/widget/CompoundButton.OnCheckedChangeListener.html
http://stackoverflow.com/questions/11278507/android-widget-switch-on-off-event-listener

To check for connectivity:
http://stackoverflow.com/questions/1560788/how-to-check-internet-access-on-android-inetaddress-never-timeouts

Toast:
http://stackoverflow.com/questions/3500197/how-to-display-toast-in-android
 */
public class HomeScreen extends AppCompatActivity {
    private SeekBar mSeekBar;
    private int lastProgress;
    private Switch mSwitch;
    private SharedPreferences sharedPreferences;
    private TextView seekbarText;
    private TextView seekbarStatusText;
    private TextView switchText;
    private String seekbarStatusTextOnEnable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homescreen);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        // Sets the name of the device owner
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("name", "Rasmus");
        editor.commit();

        // Gets the shared preferences tracking boolean - if it is not yet created, true is returned
        boolean isTracking = sharedPreferences.getBoolean("tracking", true);

        // Setting up the seekbar and text
        mSeekBar = (SeekBar)findViewById(R.id.trackingSeekbar);
        int savedSeekBarStatus = sharedPreferences.getInt("seekBarStatus", 0);
        seekbarText = (TextView)findViewById(R.id.setStatus);
        lastProgress = savedSeekBarStatus;

        // Listener for changes in the bar
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    // So we don't call the progress all over with no reason
                    if (progress != lastProgress) {
                        lastProgress = progress;
                        Log.d("onProgrChan, progress:", Integer.toString(progress));
                        // Sets the seekbar and text
                        configureSeekbar(progress);
                        // Save in SharedPreferences
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putInt("seekBarStatus", progress);
                        editor.commit();
                        // We one up progress so it corresponds to the values in the database
                        progress += 1;
                        // Upload new status to the server if internet
                        if (isOnline()) {
                            Intent intent = new Intent(getApplicationContext(), UpdateLocationService.class);
                            intent.setAction("trackingStatusUpdate");
                            intent.putExtra("progress", progress);
                            startService(intent);
                        } else {
                            setToastOnNoNetwork(getResources().getString(R.string.toastOnSave));
                        }
                    }
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        // Setting up the switch and the text
        mSwitch = (Switch)findViewById(R.id.trackingSwitch);
        mSwitch.setChecked(isTracking);
        switchText = (TextView)findViewById(R.id.trackingText);
        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
           public void onCheckedChanged(CompoundButton view, boolean checkStatus) {
               SharedPreferences.Editor editor = sharedPreferences.edit();
               editor.putBoolean("tracking", checkStatus);
               editor.commit();

               //Enable or disable the seekbar
               mSeekBar.setEnabled(checkStatus);

               Intent intent = new Intent(getApplicationContext(), UpdateLocationService.class);
               if (checkStatus) {
                   intent.setAction("start");
                   // We one up progress so it corresponds to the values in the database
                   intent.putExtra("progress", lastProgress + 1);
                   mSwitch.setHighlightColor(ContextCompat.getColor(getApplicationContext(), R.color.grey));
                   configureSeekbar(lastProgress);
               } else {
                   intent.setAction("stop");
                   mSwitch.setHighlightColor(ContextCompat.getColor(getApplicationContext(), R.color.grey));
                   configureSeekbar(4); // 4 Means disable in our case
               }
               if (isOnline()) {
                   startService(intent);
               } else {
                   setToastOnNoNetwork(getResources().getString(R.string.toastOnSave));
               }
           }
        });

        // If the shared preferences tracking boolean is true
        if (isTracking) {
            Log.d("IsTracking", "Start UpdateLocationService");
            Intent intent = new Intent (this, UpdateLocationService.class);
            intent.setAction("start");
            if (isOnline()) {
                startService(intent);
            } else {
                setToastOnNoNetwork(getResources().getString(R.string.toastOnIni));
            }

            // Switch and seekbar
            mSwitch.setHighlightColor(ContextCompat.getColor(getApplicationContext(), R.color.green));
            configureSeekbar(lastProgress);

        } else {
            mSwitch.setHighlightColor(ContextCompat.getColor(getApplicationContext(), R.color.grey));
            configureSeekbar(4);
        }
    }
    public void goToTasks(View view){
        Intent intent = new Intent(this, Tasks.class);
        startActivity(intent);
    }

    public void goToProgram(View view){
        Intent intent = new Intent(this, Program.class);
        startActivity(intent);
    }

    public void goToMaps(View view) {
        if (isOnline()) {
            Intent intent = new Intent(this.getApplicationContext(), MapsActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.toastOnMaps), Toast.LENGTH_LONG).show();
        }
    }

    private int getProgressColor(int progress) {
        int color = 0;
        switch (progress) {
            case 0:
                color = ContextCompat.getColor(getApplicationContext(), R.color.red);
                break;
            case 1:
                color = ContextCompat.getColor(getApplicationContext(), R.color.orange);
                break;
            case 2:
                color = ContextCompat.getColor(getApplicationContext(), R.color.green);
                break;
            case 4:
                // We are not tracking
                color = ContextCompat.getColor(getApplicationContext(), R.color.grey);
                break;
        }
        Log.d("Color to return", Integer.toString(color));
        return color;
    }

    private String getStatusText(int progress) {
        String statusText = "";
        switch (progress) {
            case 0:
                statusText = getResources().getString(R.string.statusNotAvailab);
                break;
            case 1:
                statusText = getResources().getString(R.string.statusOccu);
                break;
            case 2:
                statusText = getResources().getString(R.string.statusFree);
                break;
            case 4:
                // We are not tracking
                statusText = "";
                break;
        }
        return statusText;
    }

    private void configureSeekbar(int progress) {
        if (progress == 4) {
            mSeekBar.setEnabled(false);
        }
        else {
            mSeekBar.setEnabled(true);
            mSeekBar.setProgress(progress);
        }
        int color = getProgressColor(progress);
        mSeekBar.getProgressDrawable().setColorFilter(color, PorterDuff.Mode.SRC_OVER);
        // Set the texts
        seekbarStatusText = (TextView)findViewById(R.id.statusText);
        seekbarStatusTextOnEnable = getStatusText(progress);
        seekbarStatusText.setText(seekbarStatusTextOnEnable);
    }

    private boolean isOnline() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }

    private void setToastOnNoNetwork(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_homescreen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
