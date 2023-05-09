package com.example.autoassistant.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.autoassistant.R;

import java.text.DecimalFormat;

public class RollActivity extends AppCompatActivity {

    private TextView tv_rice, tv_roll;
    private SwitchCompat switcher;
    private ImageButton btn_back;
    private ImageView im_rice, im_roll;
    private EditText edtStart;
    float x = 0;
    private SensorManager sm;
    private Sensor s;
    private SensorEventListener sv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_roll);

        edtStart = findViewById(R.id.edtStart);
        im_rice = findViewById(R.id.im_rice);
        im_roll = findViewById(R.id.im_roll);
        tv_rice = findViewById(R.id.tv_rice);
        tv_roll = findViewById(R.id.tv_roll);
        switcher = findViewById(R.id.switcher);

        switcher.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    edtStart.setVisibility(View.VISIBLE);
                    Toast.makeText(getApplicationContext(), R.string.toast_edt_start, Toast.LENGTH_SHORT).show();
                }
                else edtStart.setVisibility(View.INVISIBLE);
            }
        });

        sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        if (sm != null) s = sm.getDefaultSensor(Sensor.TYPE_GAME_ROTATION_VECTOR);

        sv = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {

                float[] rotationMatrix = new float[16];
                SensorManager.getRotationMatrixFromVector(
                        rotationMatrix, event.values);
                float[] remappedRotationMatrix = new float[16];
                SensorManager.remapCoordinateSystem(rotationMatrix,
                        SensorManager.AXIS_X,
                        SensorManager.AXIS_Z,
                        remappedRotationMatrix);

                float[] orientations = new float[3];
                SensorManager.getOrientation(remappedRotationMatrix, orientations);
                for (int i = 0; i < 3; i++) {
                    orientations[i] = (float) (Math.toDegrees(orientations[i]));
                }

                DecimalFormat decimalFormat = new DecimalFormat("#.#");
                tv_roll.setText(decimalFormat.format(orientations[2]));
                tv_rice.setText(decimalFormat.format(orientations[1]));

                im_roll.setRotation(-orientations[2]);
                im_rice.setRotation(orientations[1]);

                try {

                        x = Float.parseFloat(edtStart.getText().toString());

                        tv_roll.setText(decimalFormat.format(orientations[2]));
                        tv_rice.setText(decimalFormat.format(orientations[1] - x));

                        im_roll.setRotation(-orientations[2]);
                        im_rice.setRotation(orientations[1] - x);

                } catch (NumberFormatException exception) {
                }

            }


            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
                //Do something
            }
        };

    }

    @Override
    protected void onResume() {
        super.onResume();
        sm.registerListener(sv, s, SensorManager.SENSOR_DELAY_GAME);
    }

    public void btnBack(View view) {
        try {
            sm.unregisterListener(sv);
            Intent intent = new Intent(getApplicationContext(), SelectionModeActivity.class);
            startActivity(intent);
        } catch (NullPointerException nullPointerException) {

        }

    }

}

