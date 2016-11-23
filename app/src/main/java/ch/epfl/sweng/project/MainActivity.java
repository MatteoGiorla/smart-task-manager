package ch.epfl.sweng.project;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import ch.epfl.sweng.project.authentication.LoginActivity;
import ch.epfl.sweng.project.data.UserProvider;
import ch.epfl.sweng.project.synchronization.UserAllOnCompleteListener;


/**
 * MainActivity
 */
public final class MainActivity extends AppCompatActivity {

    public static final String USER_KEY = "ch.epfl.sweng.MainActivity.CURRENT_USER";

    private final int newTaskRequestCode = 1;
    private TaskFragment fragment;
    private Context mContext;

    private Intent intent;
    private static User currentUser;

    // Will be used later on
    private String userLocation;
    private int userTimeAtDisposal;

    public static Map<Integer, String> DURATION_MAP;
    public static Map<String, Integer> REVERSE_DURATION;
    public static Map<Integer, String> START_DURATION_MAP;
    public static Map<String, Integer> REVERSE_START_DURATION;
    public static Map<Integer, String> ENERGY_MAP;
    public static Map<String, Integer> REVERSE_ENERGY;


    /**
     * Override the onCreate method to create a TaskFragment
     *
     * @param savedInstanceState If the activity is being re-initialized after previously
     *                           being shut down then this Bundle contains the data it most
     *                           recently supplied in onSaveInstanceState(Bundle).
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize Facebook SDK, in order to logout correctly
        FacebookSdk.sdkInitialize(getApplicationContext());

        setContentView(R.layout.activity_main);

        //If we are not in test mode
        //We get the user that was loaded in SynchronisationActivity
        switch (UserProvider.mProvider) {
            case UserProvider.FIREBASE_PROVIDER:
                intent = getIntent();
                checkIntent();
                currentUser = intent.getParcelableExtra(UserAllOnCompleteListener.CURRENT_USER_KEY);
                checkIntentExtra();
                break;

            case UserProvider.TEST_PROVIDER:
                currentUser = new User(User.DEFAULT_EMAIL);
                break;

            default:
                throw new IllegalStateException("UserProvider not in FIREBASE_PROVIDER nor in TEST_PROVIDER");
        }

        mContext = getApplicationContext();

        createUtilityMaps();

        //Add the user to TaskFragment
        fragment = new TaskFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(USER_KEY, currentUser);
        fragment.setArguments(bundle);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.tasks_container, fragment)
                    .commit();
        }

        //Default values
        userLocation = getResources().getString(R.string.everywhere_location);
        userTimeAtDisposal = 60; //1 hour
        initializeAdapters();
    }

    /**
     * Inflate the main_menu layout
     *
     * @param menu The options menu in which you place your items
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menu_item_logout:
                FirebaseAuth.getInstance().signOut();
                if (Profile.getCurrentProfile() != null) {
                    LoginManager.getInstance().logOut(); // log out the facebook button
                }
                Intent intent = new Intent(this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Method called when add_task_button is clicked.
     * It start a NewTaskActivity with startActivityForResult
     *
     * @param v Required argument
     */
    public void openNewTaskActivity(View v) {
        Intent intent = new Intent(this, NewTaskActivity.class);
        intent.putParcelableArrayListExtra(TaskFragment.TASKS_LIST_KEY, (ArrayList<Task>) fragment.getTaskList());
        startActivityForResult(intent, newTaskRequestCode);
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
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == newTaskRequestCode) {
            if (resultCode == RESULT_OK) {
                // Get result from the result intent.
                Task newTask = data.getParcelableExtra(NewTaskActivity.RETURNED_TASK);
                // Add element to the listTask
                fragment.addTask(newTask);
            }
        }
    }

    /**
     * Set the adapter for the images button inside MainActivity layout,
     * so the spinners attach to them dropdown when we click
     * on the image.
     */
    private void initializeAdapters() {
        Spinner mLocation = (Spinner) findViewById(R.id.location_user);
        Spinner mDuration = (Spinner) findViewById(R.id.time_user);

        String[] locationListForAdapter = getLocationTable();
        for (int i = 0; i < locationListForAdapter.length; i++) {
            if (locationListForAdapter[i].equals(getString(R.string.everywhere_location))) {
                locationListForAdapter[i] = getString(R.string.elsewhere_location);
            }
        }
        ArrayAdapter<String> locationAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, locationListForAdapter);

        ArrayAdapter<String> durationAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, getStartDurationTable());

        ArrayAdapter<String> energyAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, getEnergyTable());


        mLocation.setAdapter(locationAdapter);
        mDuration.setAdapter(durationAdapter);

        setListeners(mLocation, mDuration, locationAdapter, durationAdapter);
    }

    /**
     * Set the Listeners in order to have the spinners dropdown when we click
     * on an image button inside the MainActivity layout.
     *
     * @param location        Spinner for the user locations
     * @param duration        Spinner for the time at disposal of the user
     * @param locationAdapter The adapter of location
     * @param durationAdapter The adapter of duration
     */
    private void setListeners(Spinner location, Spinner duration,
                              final ArrayAdapter<String> locationAdapter,
                              final ArrayAdapter<String> durationAdapter) {
        location.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (getString(R.string.elsewhere_location).equals(locationAdapter.getItem(position))) {
                    userLocation = getString(R.string.everywhere_location);
                } else {
                    userLocation = locationAdapter.getItem(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        duration.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                userTimeAtDisposal = REVERSE_START_DURATION.get(durationAdapter.getItem(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void createUtilityMaps() {
        DURATION_MAP = new LinkedHashMap<>();
        DURATION_MAP.put(5, mContext.getResources().getString(R.string.duration5m));
        DURATION_MAP.put(15, mContext.getResources().getString(R.string.duration15m));
        DURATION_MAP.put(30, mContext.getResources().getString(R.string.duration30m));
        DURATION_MAP.put(60, mContext.getResources().getString(R.string.duration1h));
        DURATION_MAP.put(120, mContext.getResources().getString(R.string.duration2h));
        DURATION_MAP.put(240, mContext.getResources().getString(R.string.duration4h));
        DURATION_MAP.put(480, mContext.getResources().getString(R.string.duration1d));
        DURATION_MAP.put(960, mContext.getResources().getString(R.string.duration2d));
        DURATION_MAP.put(1920, mContext.getResources().getString(R.string.duration4d));
        DURATION_MAP.put(2400, mContext.getResources().getString(R.string.duration1w));
        DURATION_MAP.put(4800, mContext.getResources().getString(R.string.duration2w));
        DURATION_MAP.put(9600, mContext.getResources().getString(R.string.duration1m));
        DURATION_MAP = Collections.unmodifiableMap(DURATION_MAP);

        REVERSE_DURATION = new LinkedHashMap<>();
        REVERSE_DURATION.put(mContext.getResources().getString(R.string.duration5m), 5);
        REVERSE_DURATION.put(mContext.getResources().getString(R.string.duration15m), 15);
        REVERSE_DURATION.put(mContext.getResources().getString(R.string.duration30m), 30);
        REVERSE_DURATION.put(mContext.getResources().getString(R.string.duration1h), 60);
        REVERSE_DURATION.put(mContext.getResources().getString(R.string.duration2h), 120);
        REVERSE_DURATION.put(mContext.getResources().getString(R.string.duration4h), 240);
        REVERSE_DURATION.put(mContext.getResources().getString(R.string.duration1d), 480);
        REVERSE_DURATION.put(mContext.getResources().getString(R.string.duration2d), 960);
        REVERSE_DURATION.put(mContext.getResources().getString(R.string.duration4d), 1920);
        REVERSE_DURATION.put(mContext.getResources().getString(R.string.duration1w), 2400);
        REVERSE_DURATION.put(mContext.getResources().getString(R.string.duration2w), 4800);
        REVERSE_DURATION.put(mContext.getResources().getString(R.string.duration1m), 9600);
        REVERSE_DURATION = Collections.unmodifiableMap(REVERSE_DURATION);

        START_DURATION_MAP = new LinkedHashMap<>();
        START_DURATION_MAP.put(5, mContext.getResources().getString(R.string.duration5m));
        START_DURATION_MAP.put(15, mContext.getResources().getString(R.string.duration15m));
        START_DURATION_MAP.put(30, mContext.getResources().getString(R.string.duration30m));
        START_DURATION_MAP.put(60, mContext.getResources().getString(R.string.duration1hstartTime));
        START_DURATION_MAP = Collections.unmodifiableMap(START_DURATION_MAP);

        REVERSE_START_DURATION = new LinkedHashMap<>();
        REVERSE_START_DURATION.put(mContext.getResources().getString(R.string.duration5m), 5);
        REVERSE_START_DURATION.put(mContext.getResources().getString(R.string.duration15m), 15);
        REVERSE_START_DURATION.put(mContext.getResources().getString(R.string.duration30m), 30);
        REVERSE_START_DURATION.put(mContext.getResources().getString(R.string.duration1hstartTime), 60);
        REVERSE_START_DURATION = Collections.unmodifiableMap(REVERSE_START_DURATION);

        ENERGY_MAP = new LinkedHashMap<>();
        ENERGY_MAP.put(0, mContext.getResources().getString(R.string.low_energy));
        ENERGY_MAP.put(1, mContext.getResources().getString(R.string.normal_energy));
        ENERGY_MAP.put(2, mContext.getResources().getString(R.string.high_energy));
        ENERGY_MAP = Collections.unmodifiableMap(ENERGY_MAP);

        REVERSE_ENERGY = new LinkedHashMap<>();
        REVERSE_ENERGY.put(mContext.getResources().getString(R.string.low_energy), 0);
        REVERSE_ENERGY.put(mContext.getResources().getString(R.string.normal_energy), 1);
        REVERSE_ENERGY.put(mContext.getResources().getString(R.string.high_energy), 2);
        REVERSE_ENERGY = Collections.unmodifiableMap(REVERSE_ENERGY);
    }

    /**
     * Construct the table with the favorite locations of the
     * currentUser.
     *
     * @return String[] The array containing the locations of the current user.
     */
    public static String[] getLocationTable() {
        return currentUser.getListNamesLocations().toArray(new String[currentUser.getListLocations().size()]);
    }

    /**
     * Construct the table from which the user can set the energy
     * AVAILABLE to do a task.
     * It is also used to know the energy of the user
     * in order to sort the list accordingly.
     *
     * @return String[] The array containing the energies.
     */
    public static String[] getEnergyTable() {
        return ENERGY_MAP.values().toArray(new String[ENERGY_MAP.values().size()]);
    }

    /**
     * Construct the table from which the user can set the time
     * REQUIRED to do a task.
     *
     * @return String[] The array containing the durations.
     */
    public static String[] getDurationTable() {
        return DURATION_MAP.values().toArray(new String[DURATION_MAP.values().size()]);
    }

    /**
     * Construct the table from which the user can set the minimal time
     * REQUIRED before working on the task.
     *
     * @return String[] the array containing the start durations.
     */
    public static String[] getStartDurationTable() {
        return START_DURATION_MAP.values().toArray(new String[START_DURATION_MAP.values().size()]);
    }

    /**
     * Check the validity of the intent
     */
    private void checkIntent() {
        if (intent == null) {
            throw new IllegalArgumentException("No intent was passed to MainActivity");
        }
    }

    /**
     * Check extra passed with the intent
     */
    private void checkIntentExtra() {
        if (currentUser == null/* || taskList == null*/) {
            throw new IllegalArgumentException("User passed with the intent is null");
        }
    }
}