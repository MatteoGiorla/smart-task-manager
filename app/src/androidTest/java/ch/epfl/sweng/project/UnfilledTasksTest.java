package ch.epfl.sweng.project;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.project.data.TaskProvider;
import ch.epfl.sweng.project.data.UserProvider;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.uiautomator.UiDevice.getInstance;
import static junit.framework.Assert.fail;

@RunWith(AndroidJUnit4.class)
public class UnfilledTasksTest {

    private UiDevice mUiDevice;
    private static final String LEFT_ARROW_CLASSN = "android.widget.ImageButton";

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(
            MainActivity.class);

    @BeforeClass
    public static void setTaskProvider() {
        TaskProvider.setProvider(TaskProvider.TEST_PROVIDER);
    }

    @BeforeClass
    public static void setUserProvider() {
        UserProvider.setProvider(UserProvider.TEST_PROVIDER);
    }

    @Before
    public void setup(){
        mUiDevice = getInstance(getInstrumentation());
    }

    @Test
    public void MainActivityToUnfilledRoundTrip(){
        onView(withId(R.id.unfilled_task_button)).perform(click());
        onView(withId(R.id.unfilled_tasks_toolbar)).check(matches(isDisplayed()));

        UiObject returnButton = mUiDevice.findObject(new UiSelector().className(LEFT_ARROW_CLASSN).index(0));
        try{
            returnButton.clickAndWaitForNewWindow();
        }catch(UiObjectNotFoundException u){
            fail("Could not find the return arrow with UiAutomator actions");
        }

        onView(withId(R.id.add_task_button)).check(matches(isDisplayed()));

    }
}
