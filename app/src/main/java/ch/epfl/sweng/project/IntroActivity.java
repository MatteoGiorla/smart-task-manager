package ch.epfl.sweng.project;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.WindowManager;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;

public class IntroActivity extends AppIntro {
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }

        addSlide(AppIntroFragment.newInstance(getString(R.string.tutorial_slide_1_title), getString(R.string.tutorial_slide_1_description), R.drawable.logo_white_middle_det, getColor(R.color.tutorial_background)));
        addSlide(AppIntroFragment.newInstance(getString(R.string.tutorial_slide_2_title), getString(R.string.tutorial_slide_2_description), R.drawable.nexus5_black_top_high, getColor(R.color.tutorial_background)));
        addSlide(AppIntroFragment.newInstance(getString(R.string.tutorial_slide_3_title), getString(R.string.tutorial_slide_3_description), R.drawable.nexus5_black_top_high, getColor(R.color.tutorial_background)));
        addSlide(AppIntroFragment.newInstance(getString(R.string.tutorial_slide_4_title), getString(R.string.tutorial_slide_4_description), R.drawable.nexus5_black_top_high, getColor(R.color.tutorial_background)));
        addSlide(AppIntroFragment.newInstance(getString(R.string.tutorial_slide_5_title), getString(R.string.tutorial_slide_5_description), R.drawable.description_36dp, getColor(R.color.tutorial_background)));
        addSlide(AppIntroFragment.newInstance(getString(R.string.tutorial_slide_6_title), getString(R.string.tutorial_slide_6_description), R.drawable.description_36dp, getColor(R.color.tutorial_background)));
        addSlide(AppIntroFragment.newInstance(getString(R.string.tutorial_slide_7_title), getString(R.string.tutorial_slide_7_description), R.drawable.description_36dp, getColor(R.color.tutorial_background)));

        setDoneText(getString(R.string.done_button));
        setSkipText(getString(R.string.skip_button));

        showSkipButton(false);
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        goToEntryActivity();
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        goToEntryActivity();
    }

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);
        // Do something when the slide changes.
    }

    private void goToEntryActivity() {
        SharedPreferences prefs = getApplicationContext().getSharedPreferences(getString(R.string.application_prefs_name), MODE_PRIVATE);
        prefs.edit().putBoolean(getString(R.string.first_launch), false).apply();
        Intent intent = new Intent(IntroActivity.this, EntryActivity.class);
        startActivity(intent);
    }
}