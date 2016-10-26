package ch.epfl.sweng.project;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import ch.epfl.sweng.project.data.DatabaseContract;

import static android.support.test.InstrumentationRegistry.getTargetContext;
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

public class EditInformationTaskTest {

    @Before
    public void addTheTask() {
        String taskName = "Task to be displayed";
        String taskDescription = "This task will be used to test the \"display task's information activity\"";
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
    public void tearDown() {
        emptyDatabase();
    }

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(
            MainActivity.class);

    private void createATask(String taskTitle, String taskDescription){
        onView(withId(R.id.add_task_button)).perform(click());
        onView(withId(R.id.title_task)).perform(typeText(taskTitle));
        onView(withId(R.id.description_task)).perform(typeText(taskDescription));
        onView(withId(R.id.edit_done_button_toolbar)).perform(click());
    }

    @Test
    public void canEditDescription() {
        String mEditedDescription = "Edited task's description";

        onView(withId(R.id.edit_task_button)).perform(click());

        onView(withId(R.id.description_task)).perform(clearText());
        onView(withId(R.id.description_task)).perform(typeText(mEditedDescription));
        onView(withId(R.id.edit_done_button_toolbar)).perform(click());

        //Check that the new description is displayed in the task's information list
        onData(anything())
                .inAdapterView(withId(R.id.list_view_information))
                .atPosition(0)
                .check(matches(hasDescendant(withText(mEditedDescription))));

        pressBack();
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
    }



    private void emptyDatabase() {
        SQLiteDatabase myDb = getTargetContext()
                .openOrCreateDatabase(DatabaseContract.DATABASE_NAME, Context.MODE_PRIVATE, null);
        myDb.delete(DatabaseContract.TaskEntry.TABLE_NAME, null, null);
    }

}
