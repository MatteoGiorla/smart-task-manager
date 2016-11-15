package ch.epfl.sweng.project;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import ch.epfl.sweng.project.authentication.LoginActivity;


/**
 * MainActivity
 */
public final class MainActivity extends AppCompatActivity {

    public static final String USER_KEY = "ch.epfl.sweng.MainActivity.CURRENT_USER";

    private final int newTaskRequestCode = 1;
    private TaskFragment fragment;
    private Context mContext;
    private static User currentUser;
    private Bundle savedInstanceState;
    private SynchronizedQueries synchronizedQueries;
    private DatabaseReference userRef;

    // Will be used later on
    private int userEnergy;
    private String userLocation;
    private int userTimeAtDisposal;

    public static Map<Integer, String> DURATION_MAP;
    public static Map<String, Integer> REVERSE_DURATION;
    public static Map<Integer, String> FRACTIONS_MAP;
    public static Map<String, Integer> REVERSE_FRACTIONS;
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

        this.savedInstanceState = savedInstanceState;

        // Initialize Facebook SDK, in order to logout correctly
        FacebookSdk.sdkInitialize(getApplicationContext());

        setContentView(R.layout.activity_main);

        String mail;
        try {
            mail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        } catch (NullPointerException e) {
            mail = User.DEFAULT_EMAIL;
        }
        //Create an instance of the current user
        currentUser = new User(mail);
        //Get reference of the database
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        //Get reference of the user
        userRef = mDatabase.child("users")
                .child(Utils.encodeMailAsFirebaseKey(currentUser.getEmail()))
                .child("listLocations").getRef();

        //This class allows us to get all the user's data before continuing executing the app
        synchronizedQueries = new SynchronizedQueries(userRef);
        final com.google.android.gms.tasks
                .Task<Map<DatabaseReference, DataSnapshot>> readFirebaseTask = synchronizedQueries.start();
        //Listener that listen when communications with firebase end
        readFirebaseTask.addOnCompleteListener(this, new AllOnCompleteListener());
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

    @Override
    public void onStop() {
        super.onStop();
        synchronizedQueries.stop();
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
        Spinner mEnergy = (Spinner) findViewById(R.id.vitality_user);

        String[] locationListForAdapter = getLocationTable();
        for(int i = 0; i < locationListForAdapter.length; i++){
            if(locationListForAdapter[i].equals(getString(R.string.everywhere_location))){
                locationListForAdapter[i] = getString(R.string.elsewhere_location);
            }
        }
        CustomSpinnerAdapter<String> locationAdapter = new CustomSpinnerAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, locationListForAdapter);

        CustomSpinnerAdapter<String> durationAdapter = new CustomSpinnerAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, getDurationAtDisposalTable());

        CustomSpinnerAdapter<String> energyAdapter = new CustomSpinnerAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, getEnergyTable());


        mLocation.setAdapter(locationAdapter);
        mDuration.setAdapter(durationAdapter);
        mEnergy.setAdapter(energyAdapter);

        setListeners(mLocation,mDuration,mEnergy,locationAdapter,durationAdapter,energyAdapter);
    }

    /**
     * Set the Listeners in order to have the spinners dropdown when we click
     * on an image button inside the MainActivity layout.
     * @param location Spinner for the user locations
     * @param duration Spinner for the time at disposal of the user
     * @param energy Spinner for the current energy of the user
     * @param locationAdapter The adapter of location
     * @param durationAdapter The adapter of duration
     * @param energyAdapter The adapter of energy
     */
    private void setListeners(Spinner location, Spinner duration, Spinner energy,
                              final CustomSpinnerAdapter<String> locationAdapter,
                              final CustomSpinnerAdapter<String> durationAdapter,
                              final CustomSpinnerAdapter<String> energyAdapter)
    {
        location.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(getString(R.string.elsewhere_location).equals(locationAdapter.getItem(position))){
                    userLocation = getString(R.string.everywhere_location);
                } else {
                    userLocation = locationAdapter.getItem(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        duration.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                userTimeAtDisposal = REVERSE_DURATION.get(durationAdapter.getItem(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        energy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                userEnergy = REVERSE_ENERGY.get(energyAdapter.getItem(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void createUtilityMaps(){
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

        FRACTIONS_MAP = new LinkedHashMap<>();
        FRACTIONS_MAP.put(5, mContext.getResources().getString(R.string.duration5m));
        FRACTIONS_MAP.put(15, mContext.getResources().getString(R.string.duration15m));
        FRACTIONS_MAP.put(30, mContext.getResources().getString(R.string.duration30m));
        FRACTIONS_MAP.put(60, mContext.getResources().getString(R.string.duration1h));
        FRACTIONS_MAP.put(120, mContext.getResources().getString(R.string.duration2h));
        FRACTIONS_MAP.put(240, mContext.getResources().getString(R.string.duration4h));
        FRACTIONS_MAP.put(480, mContext.getResources().getString(R.string.duration1d));
        FRACTIONS_MAP = Collections.unmodifiableMap(FRACTIONS_MAP);

        REVERSE_FRACTIONS = new LinkedHashMap<>();
        REVERSE_FRACTIONS.put(mContext.getResources().getString(R.string.duration5m), 5);
        REVERSE_FRACTIONS.put(mContext.getResources().getString(R.string.duration15m), 15);
        REVERSE_FRACTIONS.put(mContext.getResources().getString(R.string.duration30m), 30);
        REVERSE_FRACTIONS.put(mContext.getResources().getString(R.string.duration1h), 60);
        REVERSE_FRACTIONS.put(mContext.getResources().getString(R.string.duration2h), 120);
        REVERSE_FRACTIONS.put(mContext.getResources().getString(R.string.duration4h), 240);
        REVERSE_FRACTIONS.put(mContext.getResources().getString(R.string.duration1d), 480);
        REVERSE_FRACTIONS = Collections.unmodifiableMap(REVERSE_FRACTIONS);

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
     * Construct the table from which the user can set the time
     * available to do a task.
     * It is also used to know the time at disposal of the user
     * in order to sort the list accordingly.
     *
     * @return String[] The array containing the durations.
     */
    public static String[] getDurationAtDisposalTable() {
        return FRACTIONS_MAP.values().toArray(new String[FRACTIONS_MAP.values().size()]);
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
     * OnCompleteListener that execute the code after that the user's data are recovered.
     */
    private class AllOnCompleteListener implements OnCompleteListener<Map<DatabaseReference, DataSnapshot>> {
        @Override
        public void onComplete(@NonNull com.google.android.gms.tasks.Task<Map<DatabaseReference, DataSnapshot>> task) {
            if (task.isSuccessful()) {
                final Map<DatabaseReference, DataSnapshot> result = task.getResult();
                List<Location> listLocations = new ArrayList<>();
                //Construct each user's location
                for (DataSnapshot data : result.get(userRef).getChildren()) {
                    String name = (String) data.child("name").getValue();
                    Double latitude = data.child("latitude").getValue(Double.class);
                    Double longitude = data.child("longitude").getValue(Double.class);
                    //Create location
                    Location location = new Location(name, latitude, longitude);
                    //Add the location to the list
                    listLocations.add(location);
                }
                //Set the list with the user's location list
                currentUser.setListLocations(listLocations);

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
                userEnergy = Task.Energy.NORMAL.ordinal();
                userLocation = getResources().getString(R.string.everywhere_location);
                userTimeAtDisposal = 60; //1 hour
                initializeAdapters();

            } else {
                try {
                    task.getException();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}