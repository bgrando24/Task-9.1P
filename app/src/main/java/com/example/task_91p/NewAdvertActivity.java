package com.example.task_91p;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.snackbar.Snackbar;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class NewAdvertActivity extends AppCompatActivity {

    private void getLocationPermission() {
        // Check if the ACCESS_FINE_LOCATION permission has been granted
        if (ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // If not, request the permission
            ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION}, 1);
        }
    }

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
        PlacesClient placesClient = Places.createClient(this);


//        adding address autocomplete feature
//        https://developers.google.com/maps/documentation/places/android-sdk/autocomplete#option_2_use_an_intent_to_launch_the_autocomplete_activity
        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG);

        addressInputEditText.setOnClickListener(v -> {
            Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
                    .build(this);
            startAutocomplete.launch(intent);
        });



//        get current location
        getCurrentLocationButton.setOnClickListener(v -> {
            // define the data types to return
            List<Place.Field> placeFields = Collections.singletonList(Place.Field.NAME);

// create request
            FindCurrentPlaceRequest request = FindCurrentPlaceRequest.newInstance(placeFields);

// call findCurrentPlace and handle the response - first checking user permission
            if (ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                Task<FindCurrentPlaceResponse> placeResponse = placesClient.findCurrentPlace(request);
                ((Task<?>) placeResponse).addOnCompleteListener(task -> {

                    if (task.isSuccessful()){
                        FindCurrentPlaceResponse response = (FindCurrentPlaceResponse) task.getResult();
//                        find most likely place
                        PlaceLikelihood mostLikelyPlace = Collections.max(response.getPlaceLikelihoods(), Comparator.comparing(PlaceLikelihood::getLikelihood));
                        Log.i(TAG, String.format("Most likely place is '%s' with likelihood: %f", mostLikelyPlace.getPlace(), mostLikelyPlace.getLikelihood()));
//
//                        for (PlaceLikelihood placeLikelihood : response.getPlaceLikelihoods()) {
//                            Log.i(TAG, String.format("Place '%s' has likelihood: %f",
//                                    placeLikelihood.getPlace().getName(),
//                                    placeLikelihood.getLikelihood()));
//                        }

//                        get details of most likely place
                        locationName = mostLikelyPlace.getPlace().getName();

                        if (mostLikelyPlace.getPlace().getLatLng() != null) {
                            lat[0] = mostLikelyPlace.getPlace().getLatLng().latitude;
                            lon[0] = mostLikelyPlace.getPlace().getLatLng().longitude;
                        } else {
                            Log.e(TAG, "LatLng is null for the place: " + mostLikelyPlace.getPlace().getName());
                        }

                        addressLocationTextView.setText(locationName);

                    } else {
                        Exception exception = task.getException();
                        if (exception instanceof ApiException) {
                            ApiException apiException = (ApiException) exception;
                            Log.e(TAG, "Place not found: " + apiException.getStatusCode());
                        }
                    }
                });
            } else {
                // A local method to request required permissions;
                // See https://developer.android.com/training/permissions/requesting
                getLocationPermission();
            }

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