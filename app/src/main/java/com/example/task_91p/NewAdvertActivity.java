package com.example.task_91p;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.snackbar.Snackbar;

import java.util.Arrays;
import java.util.List;

public class NewAdvertActivity extends AppCompatActivity {

    private final String TAG = "DEBUG_FLAG";

//        Component references
    EditText itemNameEditText;
    EditText itemPhoneEditText;
    EditText itemDescrtiptionMultiLine;
    EditText itemDate;
    Button addressInputEditText;
    Button getCurrentLocationButton;
    TextView addressLocationTextView;

//        for getting which radio button (post type) is selected
    RadioButton lostRadioButton;
    RadioButton foundRadioButton;

    //        location name and latitude/logitude values for address
    String locationName;
    final double[] lat = new double[1];
    final double[] lon = new double[1];


    private final ActivityResultLauncher<Intent> startAutocomplete = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent intent = result.getData();
                    if (intent != null) {
                        Place place = Autocomplete.getPlaceFromIntent(intent);
                        Log.i(TAG, "Place: " + place.getName() + ", " + place.getId() + ", " + place.getLatLng() + ", " + place.getAddress() + ", " + place.getAddressComponents());
                        //        set address text from autocomplete
                        addressLocationTextView.setText(place.getName());
                        locationName = place.getName();
                        lat[0] = place.getLatLng().latitude;
                        lon[0] = place.getLatLng().longitude;
                    } else {
                        Log.i(TAG, "Place: null");
                    }
                } else if (result.getResultCode() == Activity.RESULT_CANCELED) {
                    // The user canceled the operation.
                    Log.i(TAG, "User canceled autocomplete");
                }
                else {
                    Log.i(TAG, "Result: " + result);
                }
            });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_new_advert);

        // init component references
        itemNameEditText = findViewById(R.id.itemNameEditText);
        itemPhoneEditText = findViewById(R.id.itemPhoneEditText);
        itemDescrtiptionMultiLine = findViewById(R.id.itemDescriptionMultiLine);
        itemDate = findViewById(R.id.itemDate);
        addressInputEditText = findViewById(R.id.addressInputButton);
        getCurrentLocationButton = findViewById(R.id.getCurrentLocationButton);
        addressLocationTextView = findViewById(R.id.addressLocationTextView);
        lostRadioButton = findViewById(R.id.lostRadioButton);
        foundRadioButton = findViewById(R.id.foundRadioButton);


        // initialise places
        Places.initialize(getApplicationContext(), "AIzaSyAO5VGEnoRaWiqt6DSA4myicZe_GH0HNsQ");


//        adding address autocomplete feature
//        https://developers.google.com/maps/documentation/places/android-sdk/autocomplete#option_2_use_an_intent_to_launch_the_autocomplete_activity
        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG);

        addressInputEditText.setOnClickListener(v -> {
            Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
                    .build(this);
            startAutocomplete.launch(intent);
        });




//        submit button
        Button submitNewAdvertButton = findViewById(R.id.submitNewAdvertButton);
        submitNewAdvertButton.setOnClickListener(v -> {
            String itemName = itemNameEditText.getText().toString();
            String itemPhone = itemPhoneEditText.getText().toString();
            String itemDescription = itemDescrtiptionMultiLine.getText().toString();
            String submissionDate = itemDate.getText().toString();
            String itemAddress = addressInputEditText.getText().toString();
            String postType;

            if (lostRadioButton.isChecked()) {
                postType = "Lost";
            } else if (foundRadioButton.isChecked()) {
                postType = "Found";
            } else {
                Snackbar.make(v, "Select post type", Snackbar.LENGTH_LONG).show();
                return;
            }

            if (itemName.isEmpty()) {
                itemNameEditText.setError("Item name is required");
                return;
            }
            if (itemPhone.isEmpty()) {
                itemPhoneEditText.setError("Phone number is required");
                return;
            }
            if (itemDescription.isEmpty()) {
                itemDescrtiptionMultiLine.setError("Description is required");
                return;
            }
            if (submissionDate.isEmpty()) {
                itemDate.setError("Date is required");
                return;
            }
            if (itemAddress.isEmpty()) {
                addressInputEditText.setError("Location is required");
                return;
            }

//            all checks passed, insert data in db
            LostFoundItem lostFoundItem = new LostFoundItem(postType, itemName, itemPhone, itemDescription, submissionDate, locationName, lat[0], lon[0]);
            DataManager dm = new DataManager(this);
            boolean wasSuccessful = dm.addLostFoundItem(lostFoundItem);
            if (wasSuccessful) {
                Toast.makeText(this, "Advert submitted", Toast.LENGTH_LONG).show();
                Intent showAllIntent = new Intent(this, ShowAllActivity.class);
                startActivity(showAllIntent);
            } else {
                Toast.makeText(this, "Failed to submit advert", Toast.LENGTH_LONG).show();
            }
        });

    }
}