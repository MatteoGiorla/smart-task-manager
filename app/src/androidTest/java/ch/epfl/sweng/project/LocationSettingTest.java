package ch.epfl.sweng.project;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import ch.epfl.sweng.project.location_setting.LocationFragment;
import ch.epfl.sweng.project.location_setting.LocationSettingActivity;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.core.IsNot.not;


/**
 * Unit tests!
 */
@RunWith(AndroidJUnit4.class)
public final class LocationSettingTest extends SuperTest {


    @Rule
    public final ExpectedException thrownException = ExpectedException.none();
    @Rule
    public ActivityTestRule<LocationSettingActivity> mActivityRule = new ActivityTestRule<>(
            LocationSettingActivity.class);
    private String mTitleToBeTyped;

    @Before
    public void initValidString() {
        mTitleToBeTyped = "test title number ";
    }

    /**
     * Test that those locations are by default present
     * at the top of the location setting when first opening.
     */
    @Test
    public void defaultLocationsArePresent(){
        for(int i = 0; i < LocationFragment.defaultLocationsSize; ++i){
            checkALocation(LocationFragment.defaultLocations[i].getName(), i);
        }
    }

    @Test
    public void someDefaultLocationsCanBeDeletedOtherNot(){
        //deleting the "optional" default locations and testing they are not there.
        for(int i = 2; i < LocationFragment.defaultLocationsSize; ++i){
            deleteALocation(2);
            onData(anything())
                    .inAdapterView(withId(R.id.list_view_locations))
                    .atPosition(0)
                    .check(matches(hasDescendant(withText(LocationFragment.defaultLocations[i].getName()))));
        }
        //TODO: check "Everywhere" and "Downtown" can't be deleted

    }

    /**
     * Test that a Location has been correctly created and added
     * in the ListView.
     */
    @Test
    public void testCanAddLocation() {
        for (int i = 0; i < createdLocations; i++) {
            createALocation(mTitleToBeTyped + i);
            //Check title name inside listView
            checkALocation(mTitleToBeTyped, i + LocationFragment.defaultLocationsSize);
        }
    }


    /**
     * Test that we can't add a location with an empty title.
     */
    @Test
    public void testCannotAddLocationWithEmptyTitle() {
        //Create a task with empty titles
        onView(withId(R.id.add_location_button)).perform(click());
        onView(withId(R.id.locationName)).perform(typeText(""));
        pressBack();
        onView(withId(R.id.edit_done_button_toolbar)).perform(click());

        //Get the error message
        String errorMessage = getInstrumentation()
                .getTargetContext()
                .getResources()
                .getText(R.string.error_location_name_empty)
                .toString();

        //Check that the error message is displayed
        onView(withId(R.id.location_name_layout))
                .check(matches(ErrorTextInputLayoutMatcher
                        .withErrorText(containsString(errorMessage))));
        pressBack();
    }

    /**
     * Test that we can't add a location with an already used title.
     */
    @Test
    public void testCannotAddLocationWithExistingTitle() {

        //Try to create a second class with the same title as the first one
        onView(withId(R.id.add_location_button)).perform(click());
        onView(withId(R.id.locationName)).perform(typeText(LocationFragment.defaultLocations[0].getName()));
        pressBack();
        onView(withId(R.id.edit_done_button_toolbar)).check(matches(not(isDisplayed())));

        //Get the error message
        String errorMessage = getInstrumentation()
                .getTargetContext()
                .getResources()
                .getText(R.string.error_location_name_duplicated)
                .toString();

        //Check that the error message is displayed
        onView(withId(R.id.location_name_layout))
                .check(matches(ErrorTextInputLayoutMatcher
                        .withErrorText(containsString(errorMessage))));
        pressBack();
        pressBack();
    }
}
