package com.example.task_91p;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

//        button components
        Button newAdvertButton = findViewById(R.id.newAdvertButton);
        Button showAllbutton = findViewById(R.id.showAllButton);
        Button showOnMapButton = findViewById(R.id.showOnMapButton);

//        set up navigation
        newAdvertButton.setOnClickListener(v -> {
            Intent newAdvertIntent = new Intent(MainActivity.this, NewAdvertActivity.class);
            startActivity(newAdvertIntent);
        });

        showAllbutton.setOnClickListener(v -> {
            Intent showAllIntent = new Intent(MainActivity.this, ShowAllActivity.class);
            startActivity(showAllIntent);
        });

        showOnMapButton.setOnClickListener(v -> {
            Intent showOnMapIntent = new Intent(MainActivity.this, MapsActivity.class);
            startActivity(showOnMapIntent);
        });
    }
}