package ch.epfl.sweng.project;


import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Collections;
import java.util.Date;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.facebook.FacebookSdk.getApplicationContext;
import static org.hamcrest.Matchers.anything;

@RunWith(AndroidJUnit4.class)
public class TaskInformationTest extends SuperTest {

    private Task task;


    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(
            MainActivity.class);

    @Before
    public void addTheTask() {
        String taskName = "Task to be displayed";
        String taskDescription = "Description to be displayed";

        createATask(taskName, taskDescription);
        String defaultLocation = "Everywhere";
        Date defaultDueDate = new Date();
        long defaultDuration = 5;
        String defaultEnergy = Task.Energy.NORMAL.toString();
        String contributor = User.DEFAULT_EMAIL;
        task = new Task(taskName, taskDescription, defaultLocation, defaultDueDate, defaultDuration, defaultEnergy, Collections.singletonList(contributor));
    }

    @Before
    public void openInformationTaskActivity() {
        onData(anything())
                .inAdapterView(withId(R.id.list_view_tasks))
                .atPosition(0)
                .perform(click());
    }

    @After
    public void goBackOnMainScreenAndEmptyDatabase() {
        pressBack();
        emptyDatabase(1);

    }

    @Test
    public void openTaskInformationActivity() {
        onView(withId(R.id.task_information_toolbar)).check(matches(isDisplayed()));
    }

    @Test
    public void testTitleIsDisplayed() {
        onView(withId(R.id.title_task_information_activity))
                .check(matches(withText(task.getName())));
    }

    @Test
    public void testDescriptionIsDisplayed() {
        onData(anything())
                .inAdapterView(withId(R.id.list_view_information))
                .atPosition(4)
                .check(matches(hasDescendant(withText(task.getDescription()))));
    }

    @Test
    public void testLocationIsDisplayed() {
        onData(anything())
                .inAdapterView(withId(R.id.list_view_information))
                .atPosition(2)
                .check(matches(hasDescendant(withText(task.getLocationName()))));
    }

   /* @Test
    public void testDueDateIsDisplayed() {
        onData(anything())
                .inAdapterView(withId(R.id.list_view_information))
                .atPosition(0)
                .check(matches(hasDescendant(withText(task.dueDateToString()))));
    }*/


    @Test
    public void testDurationIsDisplayed() {
        String duration_text = MainActivity.DURATION_MAP.get((int)task.getDurationInMinutes());
        onData(anything())
                .inAdapterView(withId(R.id.list_view_information))
                .atPosition(1)
                .check(matches(hasDescendant(withText(duration_text))));
    }

    @Test
    public void testEnergyIsDisplayed() {
        String energy_text = MainActivity.ENERGY_MAP.get(task.getEnergy().ordinal());
        onData(anything())
                .inAdapterView(withId(R.id.list_view_information))
                .atPosition(3)
                .check(matches(hasDescendant(withText(energy_text))));
    }
}
