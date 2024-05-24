package com.example.task_91p;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class RemoveItemActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_remove_item);

//        get item from intent
        LostFoundItem item = getIntent().getParcelableExtra("item");

//        log item values
        Log.d("DEBUG_LOGS", "item:" + item.getId() + ", "+ item.getDescription() );



//        set item details to components
        TextView removeItemDescriptionTextView = findViewById(R.id.removeItemDescriptionTextView);
        TextView removeItemFoundTextView = findViewById(R.id.removeItemFoundTextView);
        TextView removeItemLocationTextView = findViewById(R.id.removeItemLocationTextView);
        removeItemDescriptionTextView.setText(item.getDescription());
        removeItemFoundTextView.setText(item.getDate());
        removeItemLocationTextView.setText(item.getLat().toString() + ", " + item.getLon().toString());

//        remove button
        Button removeItemButton = findViewById(R.id.removeItemButton);
        removeItemButton.setOnClickListener(v -> {
            DataManager dm = new DataManager(this);
            dm.deleteItem(item);
            finish();
        });

    }
}