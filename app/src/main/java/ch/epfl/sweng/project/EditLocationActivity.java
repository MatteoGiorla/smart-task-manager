package ch.epfl.sweng.project;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.ui.PlacePicker;

public class EditLocationActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.choose_location);
        //Button chooseLocationButton = (Button) findViewById(R.id.choose_location);
        PlacePicker.IntentBuilder intentBuilder = new PlacePicker.IntentBuilder();
        try {
            Intent intent = intentBuilder.build(this);

        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
            // TODO
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
            // TODO
        }


    }
}
