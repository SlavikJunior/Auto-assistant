package com.example.autoassistant.view;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.autoassistant.R;

public class SelectionModeActivity extends AppCompatActivity {

    private EditText edt_mass, edt_rad;
    private Button btn1, btn2;
    private SharedPreferences sharedPreferences;
    private float car_mass, wheel_rad;
    private final String mass_key = "mass_key";
    private final String rad_key = "rad_key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        setContentView(R.layout.activity_selection_mode);

        sharedPreferences = getSharedPreferences("User", MODE_PRIVATE);
        edt_mass = findViewById(R.id.edt_mass);
        edt_rad = findViewById(R.id.edt_rad);
        btn1 = findViewById(R.id.btn1);
        btn2 = findViewById(R.id.btn2);

    }

    public void onBtnRoll(View view) {
        Intent intent = new Intent(getApplicationContext(), RollActivity.class);
        startActivity(intent);
    }

    public void onBtnSpeed(View view) {

        if (edt_mass.getText().length() == 0 || edt_rad.getText().length() == 0) {
            Toast.makeText(this, R.string.toast_forward, Toast.LENGTH_SHORT).show();

        } else if (car_mass > 0 && wheel_rad > 0){
            Intent intent = new Intent(getApplicationContext(), VelocityActivity.class);
            intent.putExtra("mass", car_mass);
            intent.putExtra("rad", wheel_rad);
            startActivity(intent);
        }

    }

    public void onBtnSave(View view) {
        try {
            car_mass = Float.parseFloat(edt_mass.getText().toString());
            wheel_rad = Float.parseFloat(edt_rad.getText().toString());
        } catch (NumberFormatException numberFormatException) {
            Toast.makeText(this, "А строки то пустые)", Toast.LENGTH_SHORT).show();
        }

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat("mass_key", car_mass);
        editor.putFloat("rad_key", wheel_rad);
        editor.apply();

        String mass = String.valueOf(sharedPreferences.getFloat("mass_key", 0.0F));
        edt_mass.setText(mass);
        String rad = String.valueOf(sharedPreferences.getFloat("rad_key", 0.0F));
        edt_rad.setText(rad);
    }

    public void onBtnUpdate(View view) {
        sharedPreferences.edit().clear().apply();
        edt_rad.getText().clear();
        edt_mass.getText().clear();
    }
}

