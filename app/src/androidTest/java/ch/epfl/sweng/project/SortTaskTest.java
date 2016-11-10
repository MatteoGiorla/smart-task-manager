package ch.epfl.sweng.project;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.List;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anything;

@RunWith(AndroidJUnit4.class)
public final class SortTaskTest extends SuperTest{

    private List<String> taskNames;

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(
            MainActivity.class);

    @Test
    public void testStaticSort() {
        addNewTask();

        //Open task information
        onData(anything())
                .inAdapterView(withId(R.id.list_view_tasks))
                .atPosition(0)
                .perform(click());

        //Come back on the main screen
        //(we have to do that for now because we don't wait that
        //all the task are recover from firebase.
        pressBack();

        for(int i = 0; i < taskNames.size(); i++) {
            onData(anything())
                    .inAdapterView(withId(R.id.list_view_tasks))
                    .atPosition(i)
                    .check(matches(hasDescendant(withText(taskNames.get(i)))));
        }
    }



    private void addNewTask() {
        //String taskMonth = "A month";
        String taskHighEnergy = "High energy";
        String taskNormalEnergy = "Normal energy";
        String taskLowEnergy = "Low energy";

        taskNames = Arrays.asList(taskHighEnergy, taskNormalEnergy, taskLowEnergy);

        addTaskWithName(taskLowEnergy, R.id.energy_low);
        addTaskWithName(taskHighEnergy, R.id.energy_high);
        addTaskWithName(taskNormalEnergy, R.id.energy_normal);
    }

    private void addTaskWithName(String taskName, int radioButtonId) {
        onView(withId(R.id.add_task_button)).perform(click());
        onView(withId(R.id.title_task)).perform(typeText(taskName));
        pressBack();
        onView(withId(radioButtonId)).perform(click());
        onView(withId(R.id.edit_done_button_toolbar)).perform(click());
    }
}
