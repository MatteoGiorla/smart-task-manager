package ch.epfl.sweng.project;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

public class EditLocationActivity extends AppCompatActivity {

    public static final int REQUEST_PLACE_PICKER = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_location);
        Button chooseLocationButton = (Button) findViewById(R.id.choose_location_button);
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
    }

    Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_PLACE_PICKER) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                String toast = String.format("Place: %s", place.getName());
                Toast.makeText(this, toast, Toast.LENGTH_LONG).show();
            }
        }
    }

}
