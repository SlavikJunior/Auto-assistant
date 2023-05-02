package com.example.autoassistant;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.Serializable;

public class StartActivity extends AppCompatActivity implements Serializable {

    private EditText person_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        person_name = findViewById(R.id.person_name);

    }

    public void onBtnClick(View view) {

        String personName = person_name.getText().toString();

        if (personName.trim().isEmpty()) {

            Toast.makeText(this, R.string.toast_name, Toast.LENGTH_SHORT).show();

        } else {

            Intent intent = new Intent(getApplicationContext(), DescriptionActivity.class);
            intent.putExtra("name_of_person", personName);
            startActivity(intent);

        }

    }
}