package ch.epfl.sweng.project.location_setting;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;

import ch.epfl.sweng.project.Location;
import ch.epfl.sweng.project.MainActivity;
import ch.epfl.sweng.project.R;
import ch.epfl.sweng.project.User;
import ch.epfl.sweng.project.Utils;
import ch.epfl.sweng.project.authentication.LoginActivity;

public class LocationSettingActivity extends AppCompatActivity {

    private static final String TAG = "LocationSettingActivity";
    private final int newLocationRequestCode = 1;
    private LocationFragment fragment;
    Intent intent;
    ImageButton doneLocationSettingButton;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_setting);
        prefs = getApplicationContext().getSharedPreferences("ch.epfl.sweng", MODE_PRIVATE);
        fragment = new LocationFragment();

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.locations_container, fragment)
                    .commit();
        }

        doneLocationSettingButton = (ImageButton) findViewById(R.id.location_setting_done_button_toolbar);

        doneLocationSettingButton.setOnClickListener(new LocationSettingActivity.OnDoneButtonClickListener());

    }

    /**
     * Method called when add_location_button is clicked.
     * It start a NewLocationActivity with startActivityForResult
     *
     * @param v Required argument
     */
    public void openNewLocationActivity(View v) {
        Intent intent = new Intent(this, NewLocationActivity.class);
        intent.putParcelableArrayListExtra(LocationFragment.LOCATIONS_LIST_KEY, (ArrayList<Location>) fragment.getLocationList());
        startActivityForResult(intent, newLocationRequestCode);
    }

    /**
     * Method called when an activity launch inside MainActivity,
     * is finished. This method is triggered only if we use
     * startActivityForResult.
     *
     * @param requestCode The integer request code supplied to startActivityForResult
     *                    used as an identifier.
     * @param resultCode  The integer result code returned by the child activity
     * @param data        An intent which can return result data to the caller.
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == newLocationRequestCode) {
            if (resultCode == RESULT_OK) {
                // Get result from the result intent.
                Location newLocation = data.getParcelableExtra(NewLocationActivity.RETURNED_LOCATION);
                // Add element to the listLocation
                fragment.addLocation(newLocation);
            }
        }
    }

    void resultActivity() {
        if(prefs.getBoolean("FIRST_LOGIN", true)){
            Bundle extras = getIntent().getExtras();
            User user = new User(extras.getString(LoginActivity.USER_EMAIL_KEY), fragment.getLocationList());
            Utils.addUser(user);
            prefs.edit().putBoolean("FIRST_LOGIN", false).apply();
        }else{
            //TODO Update the user when acessing Location Settings from the MainActivity
        }



        //TODO : stocker user en local cf Mikael
    }

    private class OnDoneButtonClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            resultActivity();
            intent = new Intent(LocationSettingActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
    }
}












