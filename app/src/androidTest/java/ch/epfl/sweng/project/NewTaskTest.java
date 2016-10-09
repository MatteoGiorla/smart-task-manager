package ch.epfl.sweng.project;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.Assert.assertEquals;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;


/**
 * Unit tests!
 */
@RunWith(AndroidJUnit4.class)
public final class NewTaskTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(
            MainActivity.class);
    @Rule
    private final ExpectedException thrownException = ExpectedException.none();
    private String mTitleToBeTyped;
    private String mDescriptionToBeTyped;
    private String name;
    private String description;
    private Task task;

    @Before
    public void initValidString() {
        mTitleToBeTyped = "test title name number ";
        mDescriptionToBeTyped = "test description input number ";
        name = "task";
        description = "This is the first task";
        task = new Task(name, description);
    }

    @Test
    public void packageNameIsCorrect() {
        final Context context = InstrumentationRegistry.getTargetContext();
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
        new Task(null, null);
    }
}

