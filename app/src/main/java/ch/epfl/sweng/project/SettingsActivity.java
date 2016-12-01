package ch.epfl.sweng.project;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;

import ch.epfl.sweng.project.authentication.LoginActivity;
import ch.epfl.sweng.project.location_setting.LocationSettingActivity;
import ch.epfl.sweng.project.synchronization.UserAllOnCompleteListener;

public class SettingsActivity extends AppCompatActivity {

    private static User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        if(currentUser == null){
            currentUser = getIntent().getParcelableExtra(UserAllOnCompleteListener.CURRENT_USER_KEY);
        }

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
                        intent.putExtra(UserAllOnCompleteListener.CURRENT_USER_KEY, currentUser);
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
}
