package com.example.autoassistant.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.autoassistant.R;
import com.example.autoassistant.viewmodel.MyDbManager;

public class SelectionModeActivity extends AppCompatActivity {

    private EditText edt_mass, edt_rad;
    private Button btn1, btn2;
    private MyDbManager myDbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        setContentView(R.layout.activity_selection_mode);

        edt_mass = findViewById(R.id.edt_mass);
        edt_rad = findViewById(R.id.edt_rad);
        btn1 = findViewById(R.id.btn1);
        btn2 = findViewById(R.id.btn2);
        myDbManager = new MyDbManager(this);

    }

    public void onClickSave(View view) {
        myDbManager.insertToDb(edt_mass.getText().toString(), edt_rad.getText().toString());
    }

    public void onBtnRoll(View view) {
        Intent intent = new Intent(getApplicationContext(), RollActivity.class);
        startActivity(intent);
        }

    public void onBtnSpeed(View view) {
        if (edt_mass.getText().length() == 0 || edt_rad.getText().length() == 0) {
            Toast.makeText(this, R.string.toast_forward, Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(getApplicationContext(), VelocityActivity.class);
            startActivity(intent);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        myDbManager.openDb();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        myDbManager.closeDb();
    }
}