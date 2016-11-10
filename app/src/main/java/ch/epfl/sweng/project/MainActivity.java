package ch.epfl.sweng.project;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import ch.epfl.sweng.project.authentication.LoginActivity;
import ch.epfl.sweng.project.data.UserHelper;
import ch.epfl.sweng.project.data.UserProvider;


/**
 * MainActivity
 */
public final class MainActivity extends AppCompatActivity {

    public static final String USER_KEY = "ch.epfl.sweng.MainActivity.CURRENT_USER";

    private final int newTaskRequestCode = 1;
    private TaskFragment fragment;
    private static Context mContext;
    private static User currentUser;

    // Will be used later on
    private String userEnergy;
    private String userLocation;
    private String userTimeAtDisposal;


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

        //Define the currentUser
        UserHelper userProvider = new UserProvider().getUserProvider();
        currentUser = userProvider.retrieveUserInformation();

        mContext = getApplicationContext();

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
        userEnergy = getResources().getString(R.string.normal_energy);
        userLocation = getResources().getString(R.string.everywhere_location);
        userTimeAtDisposal = getResources().getString(R.string.duration1h);
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

        CustomSpinnerAdapter<String> locationAdapter = new CustomSpinnerAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, getLocationTable());

        CustomSpinnerAdapter<StateDuration> durationAdapter = new CustomSpinnerAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, getStateDurationTable());

        CustomSpinnerAdapter<StateEnergy> energyAdapter = new CustomSpinnerAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, createStateEnergyTable());


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
                              final CustomSpinnerAdapter<StateDuration> durationAdapter,
                              final CustomSpinnerAdapter<StateEnergy> energyAdapter)
    {
        location.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                userLocation = locationAdapter.getItem(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        duration.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                userTimeAtDisposal = durationAdapter.getItem(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        energy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                userEnergy = energyAdapter.getItem(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    /**
     * Construct the table from which the user can set the time
     * available/required to do a task.
     * It is also used to know the time at disposal of the user
     * in order to sort the list accordingly.
     *
     * @return StateDuration[] The array containing the durations
     */
    public static StateDuration[] getStateDurationTable() {
        return new StateDuration[] {
                new StateDuration(5, mContext),
                new StateDuration(15, mContext),
                new StateDuration(30, mContext),
                new StateDuration(60, mContext),
                new StateDuration(120, mContext),
                new StateDuration(240, mContext),
                new StateDuration(1440, mContext),
                new StateDuration(2880, mContext),
                new StateDuration(5760, mContext),
                new StateDuration(10080, mContext),
                new StateDuration(20160, mContext),
                new StateDuration(43800, mContext)
        };
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
     * available/required to do a task.
     * It is also used to know the energy of the user
     * in order to sort the list accordingly.
     *
     * @return StateEnergy[] The array containing the energy
     */
    private StateEnergy[] createStateEnergyTable() {
        return new StateEnergy[] {
                new StateEnergy(Task.Energy.LOW, mContext),
                new StateEnergy(Task.Energy.NORMAL, mContext),
                new StateEnergy(Task.Energy.HIGH, mContext)
        };
    }
}