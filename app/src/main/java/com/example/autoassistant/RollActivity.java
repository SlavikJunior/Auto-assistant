package com.example.autoassistant;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;

public class RollActivity extends AppCompatActivity {

    private TextView txtX, txtZ;
    private Button btn;

    private EditText edtStart;

    private String x_value;

    int x = 0;

    private SensorManager sm;
    private Sensor s;
    private SensorEventListener sv;

    private ImageView imX, imZ, imXBack, imZBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roll);

        edtStart = findViewById(R.id.edtStart);

        imX = findViewById(R.id.imX);
        imZ = findViewById(R.id.imZ);

        imXBack = findViewById(R.id.imXBack);
        imZBack = findViewById(R.id.imZBack);

        txtX = findViewById(R.id.txtX);
        txtZ = findViewById(R.id.txtZ);

        imX.setImageResource(R.drawable.car_side);
        imXBack.setImageResource(R.drawable.back);

        imZ.setImageResource(R.drawable.car_front);
        imZBack.setImageResource(R.drawable.back);

        btn = findViewById(R.id.btn);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SelectionModeActivity.class);
                startActivity(intent);
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
                        SensorManager.AXIS_Y,
                        SensorManager.AXIS_Z,
                        remappedRotationMatrix);

                float[] orientations = new float[3];
                SensorManager.getOrientation(remappedRotationMatrix, orientations);
                for (int i = 0; i < 3; i++) {
                    orientations[i] = (float) (Math.toDegrees(orientations[i]));
                }

                DecimalFormat decimalFormat = new DecimalFormat( "#.#" );
                txtZ.setText(decimalFormat.format(orientations[1]));
                txtX.setText(decimalFormat.format(orientations[2]));

                imZ.setRotation(-orientations[1]);
                imX.setRotation(orientations[2]);

                try {

                    x = Integer.parseInt(edtStart.getText().toString());

                    txtZ.setText(String.valueOf((int) orientations[1]));
                    txtX.setText(String.valueOf((int) orientations[2] + x));

                    imZ.setRotation(-orientations[1]);
                    imX.setRotation(orientations[2] + x);

                } catch (NumberFormatException exception) {
                    edtStart.setError("Введите число");
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

    @Override
    protected void onPause() {
        super.onPause();
        sm.unregisterListener(sv);
    }

}

