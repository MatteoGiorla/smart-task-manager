package ch.epfl.sweng.project;


import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anything;

@RunWith(AndroidJUnit4.class)
public class TaskInformationTest extends SuperTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(
            MainActivity.class);
    private Task task;

    @Before
    public void addTheTask() {
        String taskName = "Task to be displayed";
        String taskDescription = "Description";
        task = new Task(taskName, taskDescription);
        createATask(taskName, taskDescription);
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
                .atPosition(0)
                .check(matches(hasDescendant(withText(task.getDescription()))));
    }

   /* @Test
    public void testAuthorIsDisplayed() {
        onData(anything())
                .inAdapterView(withId(R.id.list_view_information))
                .atPosition(1)
                .check(matches(hasDescendant(withText(task.listOfContributorsToString()))));
    }*/

    @Test
    public void testLocationIsDisplayed() {
        onData(anything())
                .inAdapterView(withId(R.id.list_view_information))
                .atPosition(2)
                .check(matches(hasDescendant(withText(task.getLocation().getName()))));
    }

    @Test
    public void testDueDateIsDisplayed() {
        onData(anything())
                .inAdapterView(withId(R.id.list_view_information))
                .atPosition(3)
                .check(matches(hasDescendant(withText(task.dueDateToString()))));
    }


    @Test
    public void testDurationIsDisplayed() {
        onData(anything())
                .inAdapterView(withId(R.id.list_view_information))
                .atPosition(4)
                .check(matches(hasDescendant(withText(String.valueOf(task.getDuration())))));
    }

    @Test
    public void testEnergyIsDisplayed() {
        onData(anything())
                .inAdapterView(withId(R.id.list_view_information))
                .atPosition(5)
                .check(matches(hasDescendant(withText(String.valueOf(task.getEnergy())))));
    }
}
