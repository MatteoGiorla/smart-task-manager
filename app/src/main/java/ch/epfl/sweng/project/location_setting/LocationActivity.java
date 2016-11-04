package ch.epfl.sweng.project.location_setting;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import java.util.ArrayList;

import ch.epfl.sweng.project.Location;
import ch.epfl.sweng.project.R;

public abstract class LocationActivity extends AppCompatActivity {
    public static final int REQUEST_PLACE_PICKER = 1;

    Intent intent;
    ImageButton doneLocationButton;
    private EditText nameTextEdit;
    TextInputLayout textInputLayoutName;
    ArrayList<Location> locationList;
    String name;
    double longitude = 0;
    double latitude = 0;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_location);
        Button chooseLocationButton = (Button) findViewById(R.id.choose_location);
        PlacePicker.IntentBuilder intentBuilder = new PlacePicker.IntentBuilder();
        try {
            final Intent intent = intentBuilder.build(this);
            chooseLocationButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivityForResult(intent, REQUEST_PLACE_PICKER);
                }
            });

        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
            // TODO handle exception!
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
            // TODO handle exception!
        }

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

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Location Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

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
                Place place = PlacePicker.getPlace(data, this);
                String toast = String.format("Place: %s", place.getName());
                longitude = place.getLatLng().longitude;
                latitude = place.getLatLng().latitude;
                Toast.makeText(this, toast, Toast.LENGTH_LONG).show();
            }
        }
    }
}
