package ch.epfl.sweng.project.location_setting;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.GeoDataApi;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlacePicker;

import java.util.ArrayList;

import ch.epfl.sweng.project.Location;
import ch.epfl.sweng.project.R;

public abstract class LocationActivity extends AppCompatActivity {
    private static final int REQUEST_PLACE_PICKER = 1;
    private static final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;

    Intent intent;
    private ImageButton doneLocationButton;
    private EditText nameTextEdit;
    private TextInputLayout textInputLayoutName;
    ArrayList<Location> locationList;
    String name;
    double longitude = 0;
    double latitude = 0;
    private final String TAG = "Google Autocomplete";
    private GoogleApiClient mGoogleApiClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        setContentView(R.layout.activity_edit_location);
        Button chooseLocationButton = (Button) findViewById(R.id.choose_location);

        // Initialize Place Autocomplete
        createPlaceAutocomplete(chooseLocationButton);

        //Initialize the toolbar
        Toolbar mToolbar = (Toolbar) findViewById(R.id.locationToolbar);
        initializeToolbar(mToolbar);


        //Check the validity of the intent
        intent = getIntent();
        checkIntent();
        locationList = intent.getParcelableArrayListExtra(LocationFragment.LOCATIONS_LIST_KEY);
        checkLocationList();

        textInputLayoutName = (TextInputLayout) findViewById(R.id.location_name_layout);

        nameTextEdit = (EditText) findViewById(R.id.locationName);

        doneLocationButton = (ImageButton) findViewById(R.id.location_done_button_toolbar);

        //Create a listener to check that the user is writing a valid input.
        nameTextEdit.addTextChangedListener(new LocationTextWatcher());

        mToolbar.setNavigationOnClickListener(new LocationActivity.ReturnArrowListener());

        doneLocationButton.setOnClickListener(new OnDoneButtonClickListener() {

        });
    }

    /**
     * Check if the location name written is unique or not.
     *
     * @param name The new name of the location
     * @return true if the name is already used or false otherwise.
     */
    abstract boolean nameIsNotUnique(String name);

    private class OnDoneButtonClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            name = nameTextEdit.getText().toString();
            if (name.isEmpty()) {
                textInputLayoutName.setErrorEnabled(true);
                textInputLayoutName.setError(getResources().getText(R.string.error_location_name_empty));
            } else if (!name.isEmpty() && !nameIsNotUnique(name)) {
                resultActivity();
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                setResult(RESULT_OK, intent);
                finish();
            }
        }
    }

    /**
     * Start the toolbar and enable that back button on the toolbar.
     *
     * @param mToolbar the toolbar of the activity
     */
    private void initializeToolbar(Toolbar mToolbar) {
        setSupportActionBar(mToolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    /**
     * Class that implements OnClickListener.
     * It represents a OnClickListener on the return arrow.
     */
    private class ReturnArrowListener implements View.OnClickListener {

        /**
         * Called when the return arrow has been clicked.
         *
         * @param v The view that was clicked, the return arrow.
         */
        @Override
        public void onClick(View v) {
            finish();
        }
    }

    protected abstract void resultActivity();

    /**
     * Check that the intent is valid
     */
    private void checkIntent() {
        if (intent == null) {
            throw new IllegalArgumentException("No intent was passed to LocationActivity !");

        }
    }

    private void checkLocationList() {
        if (locationList == null) {
            throw new IllegalArgumentException("Error on locationList passed with the intent");
        }
    }

    /**
     * Creation of the Place Picker
     * @param chooseLocationButton Button which "switch on" Place Picker
     */
    private void createPlaceAutocomplete(Button chooseLocationButton) {
        AutocompleteFilter.Builder a = new AutocompleteFilter.Builder();
        //GeoDataApi.getAutocompletePredictions();
        try {
            final Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                    .setFilter(a.setTypeFilter(AutocompleteFilter.TYPE_FILTER_REGIONS).build())
                    .build(this);
            chooseLocationButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
                }
            });
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
            Toast.makeText(this, R.string.warning_google_serv_error, Toast.LENGTH_LONG).show();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
            Toast.makeText(this, R.string.warning_google_serv_error, Toast.LENGTH_LONG).show();
        }
        /*
        PlacePicker.IntentBuilder intentBuilder = new PlacePicker.IntentBuilder();
        try {
            final Intent intent = intentBuilder.build(this);
            chooseLocationButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivityForResult(intent, REQUEST_PLACE_PICKER);
                }
            });

        } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
            // TODO handle exception!
        }*/
    }

    /**
     * Private class that implement TextWatcher.
     * This class is used to check on runtime if the inputs written by the user
     * are valid or not.
     */
    class LocationTextWatcher implements TextWatcher {

        /**
         * Check the input written by the user before it is changed.
         */
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            textInputLayoutName.setErrorEnabled(false);
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        /**
         * Check the input written by the user after it was changed.
         */
        @Override
        public void afterTextChanged(Editable s) {
            if (nameIsNotUnique(s.toString())) {
                doneLocationButton.setVisibility(View.INVISIBLE);
                textInputLayoutName.setErrorEnabled(true);
                textInputLayoutName.setError(getResources().getText(R.string.error_location_name_duplicated));
            } else if (s.toString().isEmpty()) {
                doneLocationButton.setVisibility(View.INVISIBLE);
                textInputLayoutName.setErrorEnabled(true);
                textInputLayoutName.setError(getResources().getText(R.string.error_location_name_empty));
            } else {
                doneLocationButton.setVisibility(View.VISIBLE);
                textInputLayoutName.setErrorEnabled(false);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_PLACE_PICKER) {
            if (resultCode == RESULT_OK) {
              /*  Place place = PlacePicker.getPlace(data, this);
                String toast = getString(R.string.info_place_fixed) + place.getName();
                longitude = place.getLatLng().longitude;
                latitude = place.getLatLng().latitude;
                Toast.makeText(this, toast, Toast.LENGTH_LONG).show();*/

                Place place = PlaceAutocomplete.getPlace(this, data);
                Log.i(TAG, "Place: " + place.getName());
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                // TODO: Handle the error.
                Log.i(TAG, status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
            }
        }

}