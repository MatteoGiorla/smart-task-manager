package ch.epfl.sweng.project;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

public class ChatTest extends SuperTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(
            MainActivity.class);

   public void openChat() {
       final String name = "Piano";
       final String description = "Clavecin";
       createATask(name, description);
       //Open EditTaskActivity for the newly created task
       onView(withId(R.id.list_view_tasks))
               .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
       onView(withId(R.id.chat_menu)).perform(click());
   }

    @Test
    public void messageSendIsDisplayed() {
        openChat();
    }
}
