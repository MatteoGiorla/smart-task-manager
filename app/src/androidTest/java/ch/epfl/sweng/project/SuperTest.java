package ch.epfl.sweng.project;

import org.junit.BeforeClass;

import ch.epfl.sweng.project.data.TaskProvider;
import ch.epfl.sweng.project.data.UserProvider;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.longClick;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.Assert.fail;
import static org.hamcrest.Matchers.anything;

class SuperTest {
    final int createdTask = 2;
    final int createdLocations = 2;

    @BeforeClass
    public static void setTaskProvider() {
        TaskProvider.setProvider(TaskProvider.TEST_PROVIDER);
    }

    @BeforeClass
    public static void setUserProvider() {
        UserProvider.setProvider(UserProvider.TEST_PROVIDER);
    }

    static void checkALocation(String locationTitle, int locationPos){
        onData(anything())
                .inAdapterView(withId(R.id.list_view_locations))
                .atPosition(locationPos)
                .check(matches(hasDescendant(withText(locationTitle))));
    }

    void createALocation(String locationTitle){
        onView(withId(R.id.add_location_button)).perform(click());
        onView(withId(R.id.locationName)).perform(typeText(locationTitle));
        pressBack();
        onView(withId(R.id.location_done_button_toolbar)).perform(click());
    }

    void createATask(String taskTitle, String taskDescription){
        onView(withId(R.id.add_task_button)).perform(click());
        onView(withId(R.id.title_task)).perform(typeText(taskTitle));
        pressBack();
        onView(withId(R.id.description_task)).perform(typeText(taskDescription));
        pressBack();
        onView(withId(R.id.edit_done_button_toolbar)).perform(click());
    }

    void deleteALocation(int position){
        onData(anything())
                .inAdapterView(withId(R.id.list_view_locations))
                .atPosition(position).perform(longClick());
        onView(withText(R.string.flt_ctx_menu_delete)).perform(click());
    }


    /**
     * Utilitary method to wait until we can check which activity was launched.
     */
    static void waitForrActivity(){
        try{
            Thread.sleep(3000);
        }catch(java.lang.InterruptedException i){
            fail(i.getMessage());
        }
    }

    /**
     *  Delete the numbers of tasks given
     */
    void emptyDatabase(int size) {
        for (int i = 0; i < size; i++) {
            onData(anything())
                    .inAdapterView(withId(R.id.list_view_tasks))
                    .atPosition(0).perform(longClick());
            onView(withText(R.string.flt_ctx_menu_delete)).perform(click());
        }
    }
}
