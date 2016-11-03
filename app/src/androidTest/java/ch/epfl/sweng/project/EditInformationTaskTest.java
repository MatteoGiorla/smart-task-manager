package ch.epfl.sweng.project;


import android.support.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anything;

public class EditInformationTaskTest extends SuperTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(
            MainActivity.class);

    @Before
    public void addTheTask() {
        String taskName = "Task to be displayed";
        String taskDescription = "Description to be displayed";
        createATask(taskName, taskDescription);
    }

    @Before
    public void openInformationTaskActivity() {
        onData(anything())
                .inAdapterView(withId(R.id.list_view_tasks))
                .atPosition(0)
                .perform(click());
    }

    @Test
    public void canEditDescription() {
        String mEditedDescription = "Edited task's description";

        onView(withId(R.id.edit_task_button)).perform(click());

        onView(withId(R.id.description_task)).perform(clearText());
        onView(withId(R.id.description_task)).perform(typeText(mEditedDescription));
        pressBack();
        onView(withId(R.id.edit_done_button_toolbar)).perform(click());

        //Check that the new description is displayed in the task's information list
        onData(anything())
                .inAdapterView(withId(R.id.list_view_information))
                .atPosition(0)
                .check(matches(hasDescendant(withText(mEditedDescription))));

        pressBack();

        emptyDatabase(1);
    }

    @Test
    public void canEditName() {
        String mEditedName = "Edited task's name";

        onView(withId(R.id.edit_task_button)).perform(click());
        onView(withId(R.id.title_task)).perform(clearText());
        onView(withId(R.id.title_task)).perform(typeText(mEditedName));
        onView(withId(R.id.edit_done_button_toolbar)).perform(click());

        //Check that the new description is displayed in the task's information list
        onView(withId(R.id.title_task_information_activity))
                .check(matches(withText(mEditedName)));

        pressBack();

        emptyDatabase(1);
    }
}
