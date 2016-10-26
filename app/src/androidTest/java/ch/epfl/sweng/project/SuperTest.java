package ch.epfl.sweng.project;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.longClick;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anything;

class SuperTest {

    void createATask(String taskTitle, String taskDescription){
        onView(withId(R.id.add_task_button)).perform(click());
        onView(withId(R.id.title_task)).perform(typeText(taskTitle));
        onView(withId(R.id.description_task)).perform(typeText(taskDescription));
        onView(withId(R.id.edit_done_button_toolbar)).perform(click());
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
