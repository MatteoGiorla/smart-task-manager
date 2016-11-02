package ch.epfl.sweng.project.location_setting;


import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import java.util.ArrayList;

import ch.epfl.sweng.project.Location;
import ch.epfl.sweng.project.R;
import ch.epfl.sweng.project.TaskFragment;

import static android.app.Activity.RESULT_OK;

public abstract class LocationActivity extends AppCompatActivity {
    public static final int REQUEST_PLACE_PICKER = 1;

    Intent intent;
    ImageButton doneLocationButton;
    private EditText nameTextEdit;
    TextInputLayout textInputLayoutName;
    ArrayList<Location> locationList;
    String name;
    double longitude;
    double latitude;

    @Override
    public void onCreate(Bundle savedInstanceState) {
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

        nameTextEdit = (EditText)findViewById(R.id.locationName);

        doneLocationButton = (ImageButton) findViewById(R.id.location_done_button_toolbar);

        //Create a listener to check that the user is writing a valid input.
        nameTextEdit.addTextChangedListener(new LocationActivity.LocationTextWatcher());

        doneLocationButton.setOnClickListener(new LocationActivity.OnDoneButtonClickListener());
    }

    /**
     * Check if the location name written is unique or not.
     *
     * @param name The new name of the task
     * @return true if the name is already used or false otherwise.
     */
    boolean nameIsNotUnique(String name) {
        boolean result = false;
        for (int i = 0; i < locationList.size(); i++) {
            if (locationList.get(i).equals(name)) {
                result = true;
            }
        }
        return result;
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
            throw new IllegalArgumentException("Error on taskList passed with the intent");
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
