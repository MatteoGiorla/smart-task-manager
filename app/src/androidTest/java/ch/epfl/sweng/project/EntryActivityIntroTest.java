package ch.epfl.sweng.project;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import ch.epfl.sweng.project.data.UserProvider;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

public class EntryActivityIntroTest {

    private SharedPreferences prefs;

    @BeforeClass
    public static void setUserProvider() {
        UserProvider.setProvider(UserProvider.TEST_PROVIDER);
    }

    @Rule
    public ActivityTestRule<EntryActivity> mActivityRule = new ActivityTestRule<EntryActivity>(
            EntryActivity.class){
        //Override to be able to change the SharedPreferences effectively
        @Override
        protected void beforeActivityLaunched(){
            Context actualContext = InstrumentationRegistry.getTargetContext();
            prefs = actualContext.getSharedPreferences(actualContext.getString(R.string.application_prefs_name), Context.MODE_PRIVATE);
            prefs.edit().putBoolean(actualContext.getString(R.string.first_launch), true).apply();
            prefs.edit().putBoolean(actualContext.getString(R.string.new_user), false).apply();
            super.beforeActivityLaunched();
        }
    };

    @Test
    public void inFirstLaunchCaseLaunchIntroActivity(){
        SuperTest.waitForrActivity();
        onView(withId(R.id.next)).check(matches(isDisplayed()));
    }
}