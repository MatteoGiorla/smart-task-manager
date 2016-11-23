package ch.epfl.sweng.project;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import ch.epfl.sweng.project.authentication.LoginActivity;
import ch.epfl.sweng.project.data.TaskProvider;
import ch.epfl.sweng.project.synchronization.SynchronizationActivity;

/**
 * Activity launched at opening an app. This activity decides
 * whether the log in activity (if the user isn't logged in)
 * or the tasks list (if he is) should be displayed.
 * <p>
 * source : http://stackoverflow.com/questions/17474793/conditionally-set-first-activity-in-android
 */
public class EntryActivity extends Activity {

    public static boolean isAlreadyPersistent = false;
    private static SharedPreferences prefs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Make the database persistent, must be called before anything is done in the database.
        if(!isAlreadyPersistent && TaskProvider.mProvider.equals(TaskProvider.FIREBASE_PROVIDER)) {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
            isAlreadyPersistent = true;
        }

        prefs = getApplicationContext().getSharedPreferences(getString(R.string.application_prefs_name), MODE_PRIVATE);

        // launch a different activity
        Intent launchIntent = new Intent();
        Class<?> launchActivity;
        try {
            String className = getScreenClassName();
            launchActivity = Class.forName(className);
        } catch (ClassNotFoundException e) {
            launchActivity = SynchronizationActivity.class;
        }
        launchIntent.setClass(getApplicationContext(), launchActivity);
        startActivity(launchIntent);

        finish();
    }

    /**
     * return Class name of Activity to show
     **/
    private String getScreenClassName() {
        String activity;
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // if the user is already logged in, the Synchronisation Activity is launched
            activity = SynchronizationActivity.class.getName();
        } else {
            // else, if the user isn't logged in, the LoginActivity will be displayed
            activity = LoginActivity.class.getName();
        }
        return activity;
    }
}


