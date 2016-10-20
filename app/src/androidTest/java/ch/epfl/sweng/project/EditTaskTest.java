package ch.epfl.sweng.project;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import ch.epfl.sweng.project.data.DatabaseContract;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.longClick;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.core.IsNot.not;

/**
 * Unit tests!
 */
@RunWith(AndroidJUnit4.class)
public final class EditTaskTest {
    private String mEditedTitle;
    private String mEditedDescription;
    private String mOldTitle;
    private String mOldDescription;

    @Rule
    public final ExpectedException thrownException = ExpectedException.none();

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(
            MainActivity.class);

    @Before
    public void init() {
        mEditedTitle = "Edited Title";
        mEditedDescription = "Edited description";
        mOldTitle = "title number ";
        mOldDescription = "description number ";

        //Make sur the database is empty before starting the tests
        emptyDatabase();
    }

    @After
    public void tearDown() {
        //Empty the database once the tests are finished.
        emptyDatabase();
    }

    /**
     * Empty the local database of the app.
     */
    private void emptyDatabase() {
        SQLiteDatabase myDb = getTargetContext()
                .openOrCreateDatabase(DatabaseContract.DATABASE_NAME, Context.MODE_PRIVATE, null);
        myDb.delete(DatabaseContract.TaskEntry.TABLE_NAME, null, null);
    }

    /**
     * Test that we can't put an existing title when editing a task.
     */
    @Test
    public void testCannotEditTaskWithAlreadyExistingTitle() {

        //Create two tasks
        for (int i = 0; i < 2; i++) {
            onView(withId(R.id.add_task_button)).perform(click());
            onView(withId(R.id.input_title)).perform(typeText(mOldTitle + i));
            onView(withId(R.id.input_description)).perform(typeText(mOldDescription + i));
            onView(withId(R.id.button_submit_task)).perform(click());
        }

        //Try to edit the first task to put the same title as the first task
        onData(anything())
                .inAdapterView(withId(R.id.list_view_tasks))
                .atPosition(0).perform(longClick());
        onView(withText(R.string.flt_ctx_menu_edit)).perform(click());

        //Update the title with an existing one
        onView(withId(R.id.title_existing_task)).perform(clearText());
        onView(withId(R.id.title_existing_task)).perform(typeText(mOldTitle + 1));

        //Check that the done editing button is not displayed
        onView(withId(R.id.edit_done_button_toolbar)).check(matches(not(isDisplayed())));

        //Go back to the main activity for the next test
        pressBack();

        //empty the database for the next test
        emptyDatabase();
    }

    /**
     * Test that we can't edit a task and put an empty title.
     */
    @Test
    public void testCannotAddTaskWithEmptyTitle() {
        //Create a task
        onView(withId(R.id.add_task_button)).perform(click());
        onView(withId(R.id.input_title)).perform(typeText(mOldTitle));
        onView(withId(R.id.input_description)).perform(typeText(mOldDescription));
        onView(withId(R.id.button_submit_task)).perform(click());


        //Try to edit the first task to put the same title as the first task
        onData(anything())
                .inAdapterView(withId(R.id.list_view_tasks))
                .atPosition(0).perform(longClick());
        onView(withText(R.string.flt_ctx_menu_edit)).perform(click());
        //Update the title with empty string
        onView(withId(R.id.title_existing_task)).perform(clearText());

        //Check that the done editing button is not displayed
        onView(withId(R.id.edit_done_button_toolbar)).check(matches(not(isDisplayed())));

        //Go back to the main activity for the next test
        pressBack();

        //empty the database for the next test
        emptyDatabase();
    }

    /**
     *
     */
    @Test
    public void testCanEditTaskTitleAndDescription() {
        //Create a task
        onView(withId(R.id.add_task_button)).perform(click());
        onView(withId(R.id.input_title)).perform(typeText(mOldTitle));
        onView(withId(R.id.input_description)).perform(typeText(mOldDescription));
        onView(withId(R.id.button_submit_task)).perform(click());


        //Try to edit the first task to put the same title as the first task
        onData(anything())
                .inAdapterView(withId(R.id.list_view_tasks))
                .atPosition(0).perform(longClick());
        onView(withText(R.string.flt_ctx_menu_edit)).perform(click());
        //Update the title and the description
        onView(withId(R.id.title_existing_task)).perform(clearText());
        onView(withId(R.id.description_existing_task)).perform(clearText());
        onView(withId(R.id.title_existing_task)).perform(typeText(mEditedTitle));
        onView(withId(R.id.description_existing_task)).perform(typeText(mEditedDescription));

        onView(withId(R.id.edit_done_button_toolbar)).perform(click());

        //Check that the title has been updated
        onData(anything())
                .inAdapterView(withId(R.id.list_view_tasks))
                .atPosition(0)
                .check(matches(hasDescendant(withText(mEditedTitle))));

        //Check that the description has been updated.
        onData(anything())
                .inAdapterView(withId(R.id.list_view_tasks))
                .atPosition(0)
                .check(matches(hasDescendant(withText(mEditedDescription))));

        //empty the database for the next test
        emptyDatabase();
    }


}