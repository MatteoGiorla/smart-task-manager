package ch.epfl.sweng.project;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.longClick;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.Matchers.anything;

/**
 * Unit tests!
 */
@RunWith(AndroidJUnit4.class)
public final class EditTaskTest extends SuperTest {
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
        onData(anything())
                .inAdapterView(withId(R.id.list_view_tasks))
                .atPosition(0).perform(longClick());
        onView(withText(R.string.flt_ctx_menu_edit)).perform(click());

        //Update the title with an existing one
        onView(withId(R.id.title_task)).perform(clearText());
        onView(withId(R.id.title_task)).perform(typeText(mOldTitle + 1));

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
        onData(anything())
                .inAdapterView(withId(R.id.list_view_tasks))
                .atPosition(0).perform(longClick());
        onView(withText(R.string.flt_ctx_menu_edit)).perform(click());
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
        onData(anything())
                .inAdapterView(withId(R.id.list_view_tasks))
                .atPosition(0).perform(longClick());
        onView(withText(R.string.flt_ctx_menu_edit)).perform(click());
        //Update the title and the description
        onView(withId(R.id.title_task)).perform(clearText());
        onView(withId(R.id.description_task)).perform(clearText());
        onView(withId(R.id.title_task)).perform(typeText(mEditedTitle));
        pressBack();
        onView(withId(R.id.description_task)).perform(typeText(mEditedDescription));
        pressBack();

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
        emptyDatabase(1);
    }
}