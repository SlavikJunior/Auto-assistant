package com.example.autoassistant;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SelectionModeActivity extends AppCompatActivity {

    private EditText edt_mass, edt_rad;
    private Button btn1, btn2, btn3, btn4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection_mode);

        edt_mass = findViewById(R.id.edt_mass);
        edt_rad = findViewById(R.id.edt_rad);
        btn1 = findViewById(R.id.btn1);
        btn2 = findViewById(R.id.btn2);
        btn3 = findViewById(R.id.btn3);
        btn4 = findViewById(R.id.btn4);

    }

    public void onBtnRoll(View view) {

        Intent intent = new Intent(getApplicationContext(), RollActivity.class);
        startActivity(intent);

        }

    public void onBtnSpeed(View view) {

        String mass = edt_mass.getText().toString();
        String rad = edt_rad.getText().toString();

        if (mass.trim().isEmpty() || rad.trim().isEmpty()) {

            Toast.makeText(this, R.string.toast_forward, Toast.LENGTH_SHORT).show();

        } else {

            Intent intent = new Intent(getApplicationContext(), SelectionModeActivity.class);
            startActivity(intent);

        }

    }

    public void onBtnForce(View view) {

        String mass = edt_mass.getText().toString();
        String rad = edt_rad.getText().toString();

        if (mass.trim().isEmpty() || rad.trim().isEmpty()) {

            Toast.makeText(this, R.string.toast_forward, Toast.LENGTH_SHORT).show();

        } else {

            Intent intent = new Intent(getApplicationContext(), SelectionModeActivity.class);
            startActivity(intent);

        }

    }

    public void onBtnOther(View view) {

        String mass = edt_mass.getText().toString();
        String rad = edt_rad.getText().toString();

        if (mass.trim().isEmpty() || rad.trim().isEmpty()) {

            Toast.makeText(this, R.string.toast_forward, Toast.LENGTH_SHORT).show();

        } else {

            Intent intent = new Intent(getApplicationContext(), SelectionModeActivity.class);
            startActivity(intent);

        }

    }
}