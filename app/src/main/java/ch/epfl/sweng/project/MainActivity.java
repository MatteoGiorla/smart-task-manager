package ch.epfl.sweng.project;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

import ch.epfl.sweng.project.authentication.LoginActivity;


/**
 * MainActivity
 */
public final class MainActivity extends AppCompatActivity {

    private final int newTaskRequestCode = 1;
    private TaskFragment fragment;
    private static Context mContext;
    private static User currentUser;

    private Task.Energy userEnergy;
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

        mContext = getApplicationContext();
        fragment = new TaskFragment();

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.tasks_container, fragment)
                    .commit();
        }

        currentUser = fragment.getCurrentUser();

        //Default values
        userEnergy = Task.Energy.NORMAL;
        userLocation = getResources().getString(R.string.everywhere_location);
        userTimeAtDisposal = getResources().getString(R.string.duration1h);

        Spinner mLocation = (Spinner) findViewById(R.id.location_user);
        Spinner mDuration = (Spinner) findViewById(R.id.time_user);
        Spinner mVitality = (Spinner) findViewById(R.id.vitality_user);

        final ArrayAdapter<String> spinnerLocation = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, getLocationTable());

        final ArrayAdapter<StateDuration> spinnerDuration = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, getStateDurationTable());

        final ArrayAdapter<StateEnergy> spinnerVitality = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, createStateEnergyTable());


        mLocation.setAdapter(spinnerLocation);
        mDuration.setAdapter(spinnerDuration);
        mVitality.setAdapter(spinnerVitality);

        mLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                userLocation = spinnerLocation.getItem(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        mDuration.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                userTimeAtDisposal = spinnerDuration.getItem(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        mVitality.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String energy = spinnerVitality.getItem(position).toString();
                Log.d("TEST", energy);
                //userEnergy = Task.Energy.valueOf(energy);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
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

    public static String[] getLocationTable() {
        return currentUser.getListNamesLocations().toArray(new String[currentUser.getListLocations().size()]);
    }
    private StateEnergy[] createStateEnergyTable() {
        return new StateEnergy[] {
                new StateEnergy(Task.Energy.LOW, mContext),
                new StateEnergy(Task.Energy.NORMAL, mContext),
                new StateEnergy(Task.Energy.HIGH, mContext)
        };
    }
}