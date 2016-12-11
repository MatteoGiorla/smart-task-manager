package ch.epfl.sweng.project;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import ch.epfl.sweng.project.data.UserProvider;
import ch.epfl.sweng.project.settings.SettingsAboutActivity;
import ch.epfl.sweng.project.settings.SettingsActivity;
import ch.epfl.sweng.project.settings.SettingsSuggestActivity;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static org.hamcrest.CoreMatchers.containsString;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

import static junit.framework.Assert.assertEquals;

public class SettingsSuggestTest {

    private SharedPreferences prefs;

    @BeforeClass
    public static void setUserProvider() {
        UserProvider.setProvider(Utils.TEST_PROVIDER);
    }

    @Rule
    public ActivityTestRule<SettingsSuggestActivity> mActivityRule = new ActivityTestRule<SettingsSuggestActivity>(
            SettingsSuggestActivity.class){
        //Override to be able to change the SharedPreferences effectively
        @Override
        protected void beforeActivityLaunched() {
            Context actualContext = InstrumentationRegistry.getTargetContext();
            prefs = actualContext.getSharedPreferences(actualContext.getString(R.string.application_prefs_name), Context.MODE_PRIVATE);
            prefs.edit().putBoolean(actualContext.getString(R.string.first_launch), false).apply();
            prefs.edit().putBoolean(actualContext.getString(R.string.new_user), false).apply();
            super.beforeActivityLaunched();
        }
    };

    @Test
    public void CheckIfNotFilledNameErrorDisplayed() {
      /*  SuperTest.waitForActivity();
        onView(withId(R.id.settings_suggest_button)).perform(click());

        String errorMessage = getInstrumentation()
                .getTargetContext()
                .getResources()
                .getText(R.string.settings_suggest_field_mandatory)
                .toString();

        //Check that the error message is displayed
        onView(withId(R.id.location_name_layout))
                .check(matches(TestErrorTextEdit
                        .withErrorText(containsString(errorMessage)))); */
    }
}
