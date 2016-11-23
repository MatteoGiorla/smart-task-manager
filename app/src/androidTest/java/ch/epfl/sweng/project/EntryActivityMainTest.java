package ch.epfl.sweng.project;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;


import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import ch.epfl.sweng.project.data.UserProvider;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class EntryActivityMainTest {

    private SharedPreferences prefs;


    @BeforeClass
    public static void setUserProvider() {
        UserProvider.setProvider(UserProvider.TEST_PROVIDER);
    }

    @Rule
    public ActivityTestRule<EntryActivity> mActivityRule = new ActivityTestRule<EntryActivity>(
            EntryActivity.class) {
        //Override to be able to change the SharedPreferences effectively
        @Override
        protected void beforeActivityLaunched() {
            prefs = InstrumentationRegistry.getTargetContext().getSharedPreferences("ch.epfl.sweng", Context.MODE_PRIVATE);
            prefs.edit().putBoolean("FIRST_LOGIN", false).apply();
            super.beforeActivityLaunched();
        }
    };

    @Test
    public void inAlreadyLoggedCaseLaunchMainActivity() {
        SuperTest.waitForrActivity();
        onView(withId(R.id.add_task_button)).check(matches(isDisplayed()));
    }
}