package com.example.autoassistant;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;

public class DescriptionActivity extends AppCompatActivity implements Serializable {

    String nameOfPerson, descApp;
    TextView tvDescApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);

        tvDescApp = findViewById(R.id.tv_desc_app);
        tvDescApp.setMovementMethod(new ScrollingMovementMethod());

        Bundle arguments = getIntent().getExtras();
        nameOfPerson = arguments.get("name_of_person").toString();

        descApp = String.format(getString(R.string.description_of_app), nameOfPerson);

        tvDescApp.setText(descApp);

    }


    public void onForwardClick(View view) {

        Intent intent = new Intent(getApplicationContext(), SelectionModeActivity.class);
        startActivity(intent);

    }
}