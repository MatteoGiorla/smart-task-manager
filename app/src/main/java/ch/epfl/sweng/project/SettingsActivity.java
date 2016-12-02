package ch.epfl.sweng.project;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;

import ch.epfl.sweng.project.authentication.LoginActivity;
import ch.epfl.sweng.project.information.TaskInformationActivity;
import ch.epfl.sweng.project.location_setting.LocationSettingActivity;

public class SettingsActivity extends AppCompatActivity {

    private static User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        setToolBar();

        TextView mTutorial = (TextView) findViewById(R.id.settings_text_tutorial);
        mTutorial.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(SettingsActivity.this, IntroActivity.class);
                        startActivity(intent);
                    }
                });

        TextView mLocations = (TextView) findViewById(R.id.settings_text_locations);
        mLocations.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(SettingsActivity.this, LocationSettingActivity.class);
                        startActivity(intent);
                    }
                });

        TextView mLogOut = (TextView) findViewById(R.id.settings_text_logout);
        mLogOut.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FirebaseAuth.getInstance().signOut();
                        if (Profile.getCurrentProfile() != null) {
                            LoginManager.getInstance().logOut();
                        }
                        Intent intent = new Intent(SettingsActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                });


    }

    /**
     * Set the tool bar with the return arrow on top left.
     */
    private void setToolBar() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.settings_toolbar);
        initializeToolbar(mToolbar);
        mToolbar.setNavigationOnClickListener(new SettingsActivity.OnReturnArrowClickListener());
    }

    /**
     * Start the toolbar and enable that back button on the toolbar.
     *
     * @param mToolbar the toolbar of the activity
     */
    private void initializeToolbar(Toolbar mToolbar) {
        setSupportActionBar(mToolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    /**
     * OnClickListener on the return arrow.
     */
    private class OnReturnArrowClickListener implements View.OnClickListener {

        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        @Override
        public void onClick(View v) {
            finish();
        }
    }
}
