package ch.epfl.sweng.project.location_setting;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.project.Location;
import ch.epfl.sweng.project.NewTaskActivity;
import ch.epfl.sweng.project.R;
import ch.epfl.sweng.project.Task;
import ch.epfl.sweng.project.TaskFragment;
import ch.epfl.sweng.project.authentication.LoginActivity;

import static android.app.Activity.RESULT_OK;

public class LocationSettingActivity extends AppCompatActivity {

   // List<Location> locationsList = new ArrayList<>();
    private final int newLocationRequestCode = 1;
    private LocationFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_setting);

        fragment = new LocationFragment();

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.locations_container, fragment)
                    .commit();
        }

       // populateListView();
    }

    /**
     * Method called when add_task_button is clicked.
     * It start a NewTaskActivity with startActivityForResult
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
                // Add element to the listTask
                fragment.addLocation(newLocation);
            }
        }
    }

/*
    private void populateListView() {
        if (true) { //TODO : New user
            locationsList.add(0, new Location("Home", 0, 0));
            locationsList.add(1, new Location("Office", 0, 0));
            locationsList.add(2, new Location("School", 0, 0));

            List<String> listOfLocationNames = new ArrayList<>();

            for (int i = 0; i < locationsList.size(); i++) {
                listOfLocationNames.add(locationsList.get(i).getName());
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.list_item_location, R.id.list_entry_name, listOfLocationNames);

            ListView list = (ListView) findViewById(R.id.locations_list_view);
            list.setAdapter(adapter);
        } else {

        }
    }*/
}












