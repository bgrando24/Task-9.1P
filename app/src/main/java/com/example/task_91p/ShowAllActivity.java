package com.example.task_91p;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ShowAllActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_show_all);

        List<LostFoundItem> items;
        try (DataManager dm = new DataManager(this)) {
            items = dm.getAllItems();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "An error occurred", Toast.LENGTH_LONG).show();
            return;
        }

        if (items.isEmpty()) {
            // no items found
            Toast.makeText(this, "No items found", Toast.LENGTH_LONG).show();
        } else {
            RecyclerView showAllRecyclerView = findViewById(R.id.showAllRecyclerView);
            showAllRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            showAllRecyclerView.setAdapter(new LostFoundItemAdapter(this, items, item -> {
                Intent removeItemIntent = new Intent(this, RemoveItemActivity.class);
                removeItemIntent.putExtra("item", item);
                startActivity(removeItemIntent);
            }));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        List<LostFoundItem> items;
        try (DataManager dm = new DataManager(this)) {
            items = dm.getAllItems();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "An error occurred", Toast.LENGTH_LONG).show();
            return;
        }

        RecyclerView showAllRecyclerView = findViewById(R.id.showAllRecyclerView);
        if (items.isEmpty()) {
            // no items found
            Toast.makeText(this, "No items found", Toast.LENGTH_LONG).show();
        } else {
            showAllRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            showAllRecyclerView.setAdapter(new LostFoundItemAdapter(this, items, item -> {
                Intent removeItemIntent = new Intent(this, RemoveItemActivity.class);
                removeItemIntent.putExtra("item", item);
                startActivity(removeItemIntent);
            }));
        }
    }
}