package ch.epfl.sweng.project;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.camera2.CameraConstrainedHighSpeedCaptureSession;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import java.util.Calendar;
import java.util.GregorianCalendar;

import ch.epfl.sweng.project.data.DatabaseContract;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.InstrumentationRegistry.getTargetContext;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.longClick;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.Assert.assertEquals;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.anything;
import static org.junit.Assert.assertThat;



/**
 * Unit tests!
 */
@RunWith(AndroidJUnit4.class)
public final class NewTaskTest {
    private String mTitleToBeTyped;
    private String mDescriptionToBeTyped;
    private String name;
    private String description;
    private Task task;

    @Rule
    public final ExpectedException thrownException = ExpectedException.none();

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(
            MainActivity.class);

    @Before
    public void initValidString() {
        mTitleToBeTyped = "test title number ";
        mDescriptionToBeTyped = "test description number ";
        name = "task";
        description = "The first task";
        task = new Task(name);
        task.setDescription(description);

        //Empty the database
        emptyDatabase();
    }

    //Empty the database once the tests are finished.
    @After
    public void tearDown() {
        emptyDatabase();
    }

    private void emptyDatabase() {
        SQLiteDatabase myDb = getTargetContext()
                .openOrCreateDatabase(DatabaseContract.DATABASE_NAME, Context.MODE_PRIVATE, null);
        myDb.delete(DatabaseContract.TaskEntry.TABLE_NAME, null, null);
    }

    @Test
    public void packageNameIsCorrect() {
        final Context context = getTargetContext();
        assertThat(context.getPackageName(), is("ch.epfl.sweng.project"));
    }

    /**
     * Test that a Task has been correctly created and added
     * in the ListView.
     */
    @Test
    public void testCanAddTask() {
        for (int i = 0; i < 10; i++) {
            onView(withId(R.id.add_task_button)).perform(click());
            onView(withId(R.id.input_title)).perform(typeText(mTitleToBeTyped + i));
            onView(withId(R.id.input_description)).perform(typeText(mDescriptionToBeTyped + i));
            onView(withId(R.id.button_submit_task)).perform(click());
            //Check title name inside listView
            onData(anything())
                    .inAdapterView(withId(R.id.list_view_tasks))
                    .atPosition(i)
                    .check(matches(hasDescendant(withText(mTitleToBeTyped + i))));
            //Check description inside listView
            onData(anything())
                    .inAdapterView(withId(R.id.list_view_tasks))
                    .atPosition(i)
                    .check(matches(hasDescendant(withText(mDescriptionToBeTyped + i))));
        }

        emptyDatabase();
    }

    /**
     * Test that the getters return the good value
     */
    @Test
    public void testTaskGetters() {
        assertEquals(name, task.getName());
        assertEquals(description, task.getDescription());


    }

    /**
     * Test that the getters and setters work correctly with parameters
     */
    @Test
    public void testParameters() {
        Task testTask = new Task("Test with parameters");
        assertEquals(null, testTask.getLocation());
        assertEquals(null, testTask.getDueDate());
        assertEquals(0, testTask.getDuration());
        assertEquals(null, testTask.getEnergy());

        Location newLocationTest = new Location("Office", Location.LocationType.WORKPLACE, null);
        testTask.setLocation(newLocationTest);
        testTask.setDueDate(new GregorianCalendar(2017, 10, 23));
        testTask.setDurationInMinutes(60);
        testTask.setEnergyNeeded(Task.Energy.HIGH);

        assertEquals(newLocationTest, testTask.getLocation());
        assertEquals(2017, testTask.getDueDate().get(Calendar.YEAR));
        assertEquals(60, testTask.getDuration());
        assertEquals(Task.Energy.HIGH, testTask.getEnergy());
    }

    /**
     * Test that the setters modify correctly the Task
     */
    @Test
    public void testTaskSetters() {
        String newName = "another name";
        String newDescription = "This is a new description";

        task.setName(newName);
        task.setDescription(newDescription);

        assertEquals(newName, task.getName());
        assertEquals(newDescription, task.getDescription());
    }

    /**
     * Test that the setName setter throws an IllegalArgumentException
     * when its argument is null
     */
    @Test
    public void testTaskSetNameException() {
        thrownException.expect(IllegalArgumentException.class);
        task.setName(null);
    }

    /**
     * Test that the setDescription setter throws an IllegalArgumentException
     * when its argument is null
     */
    @Test
    public void testTaskSetDescriptionException() {
        thrownException.expect(IllegalArgumentException.class);
        task.setDescription(null);
    }

    /**
     * Test that the public constructor throws an IllegalArgumentException
     * when its arguments are null
     */
    @Test
    public void testConstructorException() {
        thrownException.expect(IllegalArgumentException.class);
        new Task(null);
    }

    /**
     * Test the describeContents method
     */
    @Test
    public void testDescribeContents() {
        assertEquals(0, task.describeContents());
    }
    /**
     * Test that an added Task has been correctly deleted when clicking on Delete.
     */
    @Test
    public void testCanDeleteTasks() {
        //We create and add tasks
        for (int i = 0; i < 10; i++) {
            onView(withId(R.id.add_task_button)).perform(click());
            onView(withId(R.id.input_title)).perform(typeText(mTitleToBeTyped + i));
            onView(withId(R.id.input_description)).perform(typeText(mDescriptionToBeTyped + i));
            onView(withId(R.id.button_submit_task)).perform(click());
        }

        //We delete the tasks
        for (int i = 0; i < 10; i++) {
            onData(anything())
                    .inAdapterView(withId(R.id.list_view_tasks))
                    .atPosition(0).perform(longClick());
            onView(withText(R.string.flt_ctx_menu_delete)).perform(click());

            //Test if the tasks are correctly deleted
            if (i != 9) {
                onData(anything())
                        .inAdapterView(withId(R.id.list_view_tasks))
                        .atPosition(0).check(matches(hasDescendant(withText(mTitleToBeTyped + (i + 1)))));
                onData(anything())
                        .inAdapterView(withId(R.id.list_view_tasks))
                        .atPosition(0).check(matches(hasDescendant(withText(mDescriptionToBeTyped + (i + 1)))));
            }
        }

        emptyDatabase();
    }

    /**
     * Test that we can't add a task with an already used title.
     */
    @Test
    public void testCannotAddTaskWithExistingTitle() {
        //Create a first task
        onView(withId(R.id.add_task_button)).perform(click());
        onView(withId(R.id.input_title)).perform(typeText(mTitleToBeTyped));
        onView(withId(R.id.input_description)).perform(typeText(mDescriptionToBeTyped));
        onView(withId(R.id.button_submit_task)).perform(click());

        //Try to create a second class with the same title as the first one
        onView(withId(R.id.add_task_button)).perform(click());
        onView(withId(R.id.input_title)).perform(typeText(mTitleToBeTyped));
        onView(withId(R.id.input_description)).perform(typeText(mDescriptionToBeTyped));
        onView(withId(R.id.button_submit_task)).perform(click());

        //Get the error message
        String errorMessage = getInstrumentation()
                .getTargetContext()
                .getResources()
                .getText(R.string.error_title_duplicated)
                .toString();

        //Check that the error message is displayed
        onView(withId(R.id.input_layout_title))
                .check(matches(ErrorTextInputLayoutMatcher
                .withErrorText(containsString(errorMessage))));
    }

    /**
     * Test that we can't add a task with an empty title.
     */
    @Test
    public void testCannotAddTaskWithEmptyTitle() {
        //Create a task with empty titles
        onView(withId(R.id.add_task_button)).perform(click());
        onView(withId(R.id.input_title)).perform(typeText(""));
        onView(withId(R.id.input_description)).perform(typeText(mDescriptionToBeTyped));
        onView(withId(R.id.button_submit_task)).perform(click());

        //Get the error message
        String errorMessage = getInstrumentation()
                .getTargetContext()
                .getResources()
                .getText(R.string.error_title_empty)
                .toString();

        //Check that the error message is displayed
        onView(withId(R.id.input_layout_title))
                .check(matches(ErrorTextInputLayoutMatcher
                .withErrorText(containsString(errorMessage))));
    }
}
