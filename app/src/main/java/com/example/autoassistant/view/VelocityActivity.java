package com.example.autoassistant.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;
import com.example.autoassistant.R;
import com.example.autoassistant.model.LocInterfaceListener;
import com.example.autoassistant.model.MyLocListener;
import com.example.autoassistant.viewmodel.MyDbManager;

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

    //private String mass, rad;
    private float carMass, wheelRad;
    private MyDbManager myDbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        setContentView(R.layout.activity_velocity);
//        Bundle arguments = getIntent().getExtras();
//        mass = arguments.get("mass").toString();
//        rad = arguments.get("rad").toString();
//        carMass = Float.parseFloat(mass);
//        wheelRad = Float.parseFloat(rad);
        init();
        myDbManager = new MyDbManager(this);
        myDbManager.getFromDb();
        myDbManager.getFromDb();
        sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        s = sm.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        sv = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                float z = sensorEvent.values[2];
                tv_acceleration.setText(getString(R.string.tv_acceleration) + String.format("%.1f", z));
                tv_traction_force.setText("Сила тяги\n (кн):\n" + String.format("%.2f", z * carMass / 1000));
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
           tv_rotation_frequency.setText(getString(R.string.tv_rotation_frequency) + String.valueOf(Math.round(loc.getSpeed() / 2 * 3.141592 * wheelRad * 60)));
           tv_angular_velocity.setText(getString(R.string.tv_angular_velocity) + String.valueOf(Math.round(loc.getSpeed() * 3.6 / wheelRad)));
           tv_braking_distance.setText(getString(R.string.tv_braking_distance) + String.valueOf(Math.round(loc.getSpeed() * 1.15 + loc.getSpeed() * loc.getSpeed() / 2 * 0.7 * 9.80665)));
           tv_braking_time.setText(getString(R.string.tv_braking_time) + String.valueOf(Math.round(loc.getSpeed() / 9.80665 * 0.7)));
           tv_pulse.setText(getString(R.string.tv_pulse) + String.valueOf(Math.round(loc.getSpeed() * carMass)));

       btnUpdate.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               distance = 0;
           }
       });
    }

    public void btnBack(View view) {
        sm.unregisterListener(sv);
        locationManager.removeUpdates(myLocListener);
        Intent intent = new Intent(getApplicationContext(), SelectionModeActivity.class);
        startActivity(intent);

    }

    @Override
    protected void onResume() {
        super.onResume();
        sm.registerListener(sv, s, SensorManager.SENSOR_DELAY_GAME);
        myDbManager.openDb();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Bundle bundle = new Bundle();
        bundle.putFloat("mass", carMass);
        bundle.putFloat("rad", wheelRad);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        carMass = savedInstanceState.getFloat("mass");
        wheelRad = savedInstanceState.getFloat("rad");
    }
}