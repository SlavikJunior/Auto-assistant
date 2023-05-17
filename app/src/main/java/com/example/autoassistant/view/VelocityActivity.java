package com.example.autoassistant.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;
import com.example.autoassistant.R;
import com.example.autoassistant.model.LocInterfaceListener;
import com.example.autoassistant.model.MyLocListener;

public class VelocityActivity extends AppCompatActivity implements LocInterfaceListener {

    private float distance;
    private TextView tv_pulse, tv_braking_time, tv_braking_distance, tv_acceleration, tv_velocity, tv_distance, tv_traction_force, tv_angular_velocity, tv_rotation_frequency;
    private ImageButton btn_back, btnUpdate;

    private LocationManager locationManager;
    private Location lastLocation;
    private MyLocListener myLocListener;

    private SensorManager sm;
    private Sensor s;
    private SensorEventListener sv;

    private String mass, rad;
    private float carMass, wheelRad;

//    private SharedPreferences sharedPreferences;
//    private final String distanceKey = "key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        setContentView(R.layout.activity_velocity);

//        sharedPreferences = getSharedPreferences("USER", MODE_PRIVATE);
//        distance = sharedPreferences.getFloat(distanceKey, 0.0F);
//        tv_distance.setText(String.valueOf(distance));

        Bundle arguments = getIntent().getExtras();
        mass = arguments.get("mass").toString();
        rad = arguments.get("rad").toString();
        carMass = Float.parseFloat(mass);
        wheelRad = Float.parseFloat(rad);
        init();
        sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        s = sm.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        sv = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                float z = sensorEvent.values[2];
                tv_acceleration.setText(getString(R.string.tv_acceleration) + String.format("%.1f", z));
                float traction_force = z * carMass / 1000;
                tv_traction_force.setText(getString(R.string.tv_traction_force) + String.format("%.1f", traction_force));
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
            }
        };
    }

    private void init() {

        tv_pulse = findViewById(R.id.tv_pulse);
        tv_braking_distance = findViewById(R.id.tv_braking_distance);
        tv_braking_time = findViewById(R.id.tv_braking_time);
        tv_acceleration = findViewById(R.id.tv_acceleration);
        tv_velocity = findViewById(R.id.tv_velocity);
        tv_distance = findViewById(R.id.tv_distance);
        tv_traction_force = findViewById(R.id.tv_traction_force);
        tv_angular_velocity = findViewById(R.id.tv_angular_velocity);
        tv_rotation_frequency = findViewById(R.id.tv_rotation_frequency);
        btn_back = findViewById(R.id.btn_back);
        btnUpdate = findViewById(R.id.btn_update);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        myLocListener = new MyLocListener();
        myLocListener.setLocInterfaceListener(this);
        checkPermissions();
        boolean enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!enabled) {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100 && grantResults[0] == RESULT_OK)
        {
            checkPermissions();
        }

    }

    private void checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions(new String[] {android.Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 100);

        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, myLocListener );
        }
    }

    @Override
    public void OnLocationChanged(Location loc) {
        if (loc.hasSpeed() && lastLocation != null) {
               distance += lastLocation.distanceTo(loc) / 1000;
           }
           lastLocation = loc;

           tv_distance.setText(getString(R.string.tv_distance) + String.format("%.3f", distance));
           tv_velocity.setText(getString(R.string.tv_velocity) + String.valueOf(Math.round(loc.getSpeed() * 3.6)));
           tv_rotation_frequency.setText(getString(R.string.tv_rotation_frequency) + String.valueOf(Math.round(loc.getSpeed() * 60 / 6.2831 * wheelRad)));
           tv_angular_velocity.setText(getString(R.string.tv_angular_velocity) + String.format("%.1f", loc.getSpeed() * 3.6 / wheelRad));
           tv_braking_distance.setText(getString(R.string.tv_braking_distance) + String.valueOf(Math.round(loc.getSpeed() * 1.15 + loc.getSpeed() * loc.getSpeed() / 13.7292)));
           tv_braking_time.setText(getString(R.string.tv_braking_time) + String.valueOf(Math.round(loc.getSpeed() / 6.8646)));
           tv_pulse.setText(getString(R.string.tv_pulse) + String.format("%.2f",loc.getSpeed() * carMass * 0.0036));

       btnUpdate.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               openQuitDialog();
           }
       });
    }

    private void openQuitDialog() {
        AlertDialog.Builder quitDialog = new AlertDialog.Builder(VelocityActivity.this);
        quitDialog.setTitle(R.string.dialogTitle);

        quitDialog.setPositiveButton(R.string.positiveBtn, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                distance = 0;
            }
        });

        quitDialog.setNegativeButton(R.string.negativeBtn, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        quitDialog.show();
    }

    public void btnBack(View view) {
        sm.unregisterListener(sv);
        locationManager.removeUpdates(myLocListener);

//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putFloat(distanceKey, distance);

        Intent intent = new Intent(getApplicationContext(), SelectionModeActivity.class);
        startActivity(intent);

    }

    @Override
    protected void onResume() {
        super.onResume();
        sm.registerListener(sv, s, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle saveInstanceState ) {
        saveInstanceState.putFloat("distance", distance);
        saveInstanceState.putFloat("mass", carMass);
        saveInstanceState.putFloat("rad", wheelRad);
        super.onSaveInstanceState(saveInstanceState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        distance = savedInstanceState.getFloat("distance");
        carMass = savedInstanceState.getFloat("mass");
        wheelRad = savedInstanceState.getFloat("rad");
    }
}