package ch.epfl.sweng.project;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

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
        onView(withId(R.id.input)).perform(typeText("Hello"));
        onView(withId(R.id.send_message_button)).perform(click());
        onView(withId(R.id.list_of_messages)).check(matches(hasDescendant(withText("Hello"))));
    }
}
