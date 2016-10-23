package ch.epfl.sweng.project;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import ch.epfl.sweng.project.authentication.LoginActivity;

/**
 * Activity launched at opening an app. This activity decides
 * whether the log in activity (if the user isn't logged in)
 * or the tasks list (if he is) should be displayed.
 *
 * source : http://stackoverflow.com/questions/17474793/conditionally-set-first-activity-in-android
 */
public class EntryActivity extends Activity{

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // launch a different activity
        Intent launchIntent = new Intent();
        Class<?> launchActivity;
        try
        {
            String className = getScreenClassName();
            launchActivity = Class.forName(className);
        }
        catch (ClassNotFoundException e)
        {
            launchActivity = MainActivity.class;
        }
        launchIntent.setClass(getApplicationContext(), launchActivity);
        startActivity(launchIntent);

        finish();
    }

    /** return Class name of Activity to show **/
    private String getScreenClassName()
    {
        String activity;
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null) {
            // if the user is already logged in the MainActivity with the tasks list is displayed
            activity = MainActivity.class.getName();
        } else {
            // else, if the user isn't logged in, the LoginActivity will be displayed
            activity = LoginActivity.class.getName();
        }
        return activity;
    }
}


