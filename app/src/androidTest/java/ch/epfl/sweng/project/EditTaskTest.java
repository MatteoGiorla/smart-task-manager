package ch.epfl.sweng.project;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.containsString;

/**
 * Unit tests!
 */
@RunWith(AndroidJUnit4.class)
public final class EditTaskTest extends SuperTest {
    @Rule
    public final ExpectedException thrownException = ExpectedException.none();
    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(
            MainActivity.class);
    private String mEditedTitle;
    private String mEditedDescription;
    private String mOldTitle;
    private String mOldDescription;

    @Before
    public void init() {
        mEditedTitle = "Edited Title";
        mEditedDescription = "Edited description";
        mOldTitle = "title number ";
        mOldDescription = "description number ";
    }

    /**
     * Test that we can't put an existing title when editing a task.
     */
    @Test
    public void testCannotEditTaskWithAlreadyExistingTitle() {

        //Create two tasks
        for (int i = 0; i < createdTask; i++) {
            createATask(mOldTitle + i, mOldDescription + i);
        }

        //Try to edit the first task to put the same title as the first task
        onView(withId(R.id.list_view_tasks))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        onView(withId(R.id.edit_task_button)).perform(click());

        //Update the title with an existing one
        onView(withId(R.id.title_task)).perform(clearText());
        onView(withId(R.id.title_task)).perform(typeText(mOldTitle + 1));
        pressBack();

        //Get the error message
        String errorMessage = getInstrumentation()
                .getTargetContext()
                .getResources()
                .getText(R.string.error_title_duplicated)
                .toString();

        //Check that the error message is displayed
        onView(withId(R.id.title_task_layout))
                .check(matches(ErrorTextInputLayoutMatcher
                        .withErrorText(containsString(errorMessage))));

        //Go back to the main activity for the next test
        pressBack();
        pressBack();

        emptyDatabase(createdTask);
    }

    /**
     * Test that we can't edit a task and put an empty title.
     */
    @Test
    public void testCannotAddTaskWithEmptyTitle() {
        //Create a task
        createATask(mOldTitle, mOldDescription);

        //Try to edit the first task to put the same title as the first task
        onView(withId(R.id.list_view_tasks))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        onView(withId(R.id.edit_task_button)).perform(click());


        //Update the title with empty string
        onView(withId(R.id.title_task)).perform(clearText());

        //Get the error message
        String errorMessage = getInstrumentation()
                .getTargetContext()
                .getResources()
                .getText(R.string.error_title_empty)
                .toString();

        //Check that the error message is displayed
        onView(withId(R.id.title_task_layout))
                .check(matches(ErrorTextInputLayoutMatcher
                        .withErrorText(containsString(errorMessage))));
        pressBack();
        pressBack();
        emptyDatabase(1);
    }

    /**
     *
     */
    @Test
    public void testCanEditTaskTitleAndDescription() {
        //Create a task
        createATask(mOldTitle, mOldDescription);

        //Try to edit the first task to put the same title as the first task
        onView(withId(R.id.list_view_tasks))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        onView(withId(R.id.edit_task_button)).perform(click());

        //Update the title and the description
        onView(withId(R.id.title_task)).perform(clearText());
        onView(withId(R.id.description_task)).perform(clearText());
        onView(withId(R.id.title_task)).perform(typeText(mEditedTitle));
        pressBack();
        onView(withId(R.id.description_task)).perform(typeText(mEditedDescription));
        pressBack();

        onView(withId(R.id.edit_done_button_toolbar)).perform(click());

        //Check that the title has been updated
        onView(withId(R.id.title_task_information_activity))
                .check(matches(withText(mEditedTitle)));

        pressBack();
        //empty the database for the next test
        emptyDatabase(1);
    }
}