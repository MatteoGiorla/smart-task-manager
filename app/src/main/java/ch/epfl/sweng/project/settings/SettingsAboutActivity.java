package ch.epfl.sweng.project.settings;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import ch.epfl.sweng.project.R;

public class SettingsAboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_about);

        // TODO demander à Matteo comment ça se passe pour le return arrow!!
        //setToolBar();

        setTeamNames();

        setAppVersion();

    }

    /**
     * Set the tool bar with the return arrow on top left.
     */
    private void setToolBar() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.settings_toolbar);
        initializeToolbar(mToolbar);
        mToolbar.setNavigationOnClickListener(new SettingsAboutActivity.OnReturnArrowClickListener());
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
     * get the emoji corresponding to the unicode
     *
     * @param unicode hexadecimal representing an emoji
     * @return the emoji as a String
     */
    private String getEmojiByUnicode(int unicode){
        return new String(Character.toChars(unicode));
    }

    /**
     * get the App version and write it in the corresponding TextView
     */
    private void setAppVersion(){
        TextView version = (TextView) findViewById(R.id.settings_about_text_version);
        try {
            PackageInfo _info = getPackageManager().getPackageInfo(getPackageName(), 0);
            version.setText(_info.versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            version.setText("");
        }
    }

    /**
     * set the team name for each corresponding TextView
     */
    private void setTeamNames() {
        TextView nameMikael = (TextView) findViewById(R.id.settings_about_text_mikael);
        TextView nameCharles = (TextView) findViewById(R.id.settings_about_text_charles);
        TextView nameMatteo = (TextView) findViewById(R.id.settings_about_text_matteo);
        TextView nameCedric = (TextView) findViewById(R.id.settings_about_text_cedric);
        TextView nameBastian = (TextView) findViewById(R.id.settings_about_text_bastian);
        TextView nameIlkan = (TextView) findViewById(R.id.settings_about_text_ilkan);

        nameMikael.setText(String.format(getEmojiByUnicode(0x1F680) + getString(R.string.settings_about_mikael)));
        nameCharles.setText(String.format(getEmojiByUnicode(0x1F682)+ getString(R.string.settings_about_charles)));
        nameMatteo.setText(String.format(getEmojiByUnicode(0x1F34E)+ getString(R.string.settings_about_matteo)));
        nameCedric.setText(String.format(getEmojiByUnicode(0x1F3B8)+ getString(R.string.settings_about_cedric)));
        nameBastian.setText(String.format(getEmojiByUnicode(0x1F355)+ getString(R.string.settings_about_bastian)));
        nameIlkan.setText(String.format(getEmojiByUnicode(0x1F6F0)+ getString(R.string.settings_about_ilkan)));
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
