package ch.epfl.sweng.project.location_setting;

import android.os.Build;
import android.support.annotation.RequiresApi;

import ch.epfl.sweng.project.Location;


/**
 * Class that represents the inflated activity_new_task
 */
public class NewLocationActivity extends LocationActivity {
    public static final String RETURNED_LOCATION = "ch.epfl.sweng.NewLocationActivity.NEW_LOCATION";

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void resultActivity() {
        Location newLocation = new Location(name, latitude, longitude);

        intent.putExtra(RETURNED_LOCATION, newLocation);
    }
}
