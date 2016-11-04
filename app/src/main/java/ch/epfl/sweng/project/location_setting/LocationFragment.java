package ch.epfl.sweng.project.location_setting;


import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteException;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.project.Location;
import ch.epfl.sweng.project.R;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

/**
 * Class that represents the inflated fragment located in the activity_main
 */
public class LocationFragment extends Fragment {
    public static final String INDEX_LOCATION_TO_BE_EDITED_KEY = "ch.epfl.sweng.LocationFragment._INDEX_LOCATION_TO_BE_EDITED";
    public static final String LOCATIONS_LIST_KEY = "ch.epfl.sweng.LocationFragment.LOCATIONS_LIST";
    public static final String INDEX_LOCATION_TO_BE_DISPLAYED = "ch.epfl.sweng.LocationFragment.INDEX_LOCATION_TO_BE_DISPLAYED";
    private final int editLocationRequestCode = 2;
    private final int displayLocationRequestCode = 3;
    private LocationListAdapter mLocationAdapter;
    private ArrayList<Location> locationList;
    private static final int defaultLocationsSize = 5;
    private static final Location[] defaultLocations = new Location[defaultLocationsSize];
    private SharedPreferences prefs;

    /**
     * Method that adds a location in the locationList and in the database.
     *
     * @param location The location to be added
     * @throws IllegalArgumentException If the location to be added is null
     */
    public void addLocation(Location location) {
        if (location == null) {
            throw new IllegalArgumentException();
        }
        locationList.add(location);
        mLocationAdapter.notifyDataSetChanged();
    }

    private void addDefaultLocations(){
        defaultLocations[0] = new Location(getString(R.string.everywhere_location),0,0);
        defaultLocations[1] = new Location(getString(R.string.downtown_location),0,0);
        defaultLocations[2] = new Location(getString(R.string.home_location),0,0);
        defaultLocations[3] = new Location(getString(R.string.office_location),0,0);
        defaultLocations[4] = new Location(getString(R.string.school_location),0,0);
        for(Location l: defaultLocations){
            addLocation(l);
        }
    }

    /**
     * Override the onCreate method. It initialize the database, the list of location
     * and the custom made adapter.
     *
     * @param savedInstanceState If the fragment is being re-created from a previous saved state,
     *                           this is the state
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        locationList = new ArrayList<>();

        mLocationAdapter = new LocationListAdapter(
                getActivity(),
                R.layout.list_item_location,
                locationList
        );
        prefs = getContext().getSharedPreferences("ch.epfl.sweng", MODE_PRIVATE);
        if(prefs.getBoolean("FIRST_LOGIN", true)){
            addDefaultLocations();
        }
    }

    /**
     * Override the onCreateView method to initialize the adapter of
     * the ListView.
     *
     * @param inflater           The LayoutInflater object that can be used to inflate
     *                           any views in the fragment
     * @param container          Parent view that the fragment's UI should be attached to
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     *                           from a previous saved state as given here
     * @return the view of the list to be displayed
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_location_list, container, false);

        ListView listView = (ListView) rootView.findViewById(R.id.list_view_locations);
        listView.setAdapter(mLocationAdapter);

        registerForContextMenu(listView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(id != 0 && id != 1) { //prevent default locations from edit or delete
                    Intent intent = new Intent(getActivity(), EditLocationActivity.class);
                    intent.putExtra(INDEX_LOCATION_TO_BE_EDITED_KEY, position);
                    intent.putParcelableArrayListExtra(LOCATIONS_LIST_KEY, locationList);
                    startActivityForResult(intent, editLocationRequestCode);
                } else {
                    //TODO : optionally display a toast "You can't edit or delete the default locations"
                }
            }
        });

        return rootView;
    }

    /**
     * Override the onCreateContextMenu method.
     * This method creates a floating context menu.
     *
     * @param menu     The context menu that is being built.
     * @param v        The view for which the context menu is being built.
     * @param menuInfo Extra information about the item
     *                 for which the context menu should be shown
     */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        long selectedId = info.id;

        if(selectedId != 0 && selectedId != 1) { //prevent default locations from edit or delete
            MenuInflater menuInflater = getActivity().getMenuInflater();
            menuInflater.inflate(R.menu.floating_context_menu, menu);
        }
    }

    /**
     * Override the onContextItemSelected.
     * This method decides what to do depending of the context menu's item
     * selected by the user.
     *
     * @param item The context menu item that was selected
     * @return Return false to allow normal context menu processing to proceed,
     * true to consume it here
     */
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo itemInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.floating_delete:
                removeLocation(itemInfo);
                return true;
            case R.id.floating_edit:
                startEditLocationActivity(itemInfo);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    /**
     * Method called when an activity launch inside LocationSettingActivity,
     * is finished. This method is triggered only if we use
     * startActivityForResult.
     *
     * @param requestCode The integer request code supplied to startActivityForResult
     *                    used as an identifier.
     * @param resultCode  The integer result code returned by the child activity
     * @param data        An intent which can return result data to the caller.
     * @throws IllegalArgumentException if the returned extras from EditLocationActivity are
     *                                  invalid
     * @throws SQLiteException          if more that one row was changed when editing a location.
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Case when we returned from the EditLocationActivity
        if (requestCode == editLocationRequestCode && resultCode == RESULT_OK) {
            actionOnActivityResult(data);
        }
    }

    private void actionOnActivityResult(Intent data) {
        // Get result from the result intent.
        Location editedLocation = data.getParcelableExtra(EditLocationActivity.RETURNED_EDITED_LOCATION);
        int indexEditedLocation = data.getIntExtra(EditLocationActivity.RETURNED_INDEX_EDITED_LOCATION, -1);
        if (indexEditedLocation == -1 || editedLocation == null) {
            throw new IllegalArgumentException("Invalid extras returned from EditLocationActivity !");
        } else {
            locationList.set(indexEditedLocation, editedLocation);
            mLocationAdapter.notifyDataSetChanged();
            Toast.makeText(getActivity().getApplicationContext(),
                    editedLocation.getName() + " has been updated !",
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Start the EditLocationActivity for result when the user press the edit button.
     * The location index and the locationList are passed as extras to the intent.
     *
     * @param itemInfo Extra information about the item
     *                 for which the context menu should be shown
     */
    private void startEditLocationActivity(AdapterView.AdapterContextMenuInfo itemInfo) {
        int position = itemInfo.position;
        Intent intent = new Intent(getActivity(), EditLocationActivity.class);

        intent.putExtra(INDEX_LOCATION_TO_BE_EDITED_KEY, position);
        intent.putParcelableArrayListExtra(LOCATIONS_LIST_KEY, locationList);

        startActivityForResult(intent, editLocationRequestCode);
    }

    /**
     * Remove a location and the locationList.
     *
     * @param itemInfo Extra information about the item
     *                 for which the context menu should be shown
     * @throws SQLiteException if an error occurred
     */
    private void removeLocation(AdapterView.AdapterContextMenuInfo itemInfo) {
        int position = itemInfo.position;
        removeLocationAction(position);

    }

    private void removeLocationAction(int position) {
        Location locationToBeDeleted = locationList.get(position);
        String locationName = locationToBeDeleted.getName();

        locationList.remove(locationToBeDeleted);
        mLocationAdapter.notifyDataSetChanged();

        Context context = getActivity().getApplicationContext();
        String TOAST_MESSAGE = locationName + " deleted";
        int duration = Toast.LENGTH_SHORT;
        Toast.makeText(context, TOAST_MESSAGE, duration).show();
    }

    /**
     * Getter for the locationList
     *
     * @return an immutable copy of locationList
     */
    public List<Location> getLocationList() {
        return new ArrayList<>(locationList);
    }
}
