package ch.epfl.sweng.project;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import ch.epfl.sweng.project.location_setting.LocationSettingActivity;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.core.IsNot.not;


/**
 * Unit tests!
 */
@RunWith(AndroidJUnit4.class)
public final class NewLocationTest extends SuperTest {

    private final String EVERYWHERE_STR = "Everywhere";
    private final String DOWNTOWN_STR = "Downtown";
    private final String ATWORK_STR = "At work";
    private final String ATHOME_STR = "At home";
    private final String ATSCHOOL_STR = "At school";

    private final int defaultLocationsOffset = 5;

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
        checkALocation(EVERYWHERE_STR, 0);
        checkALocation(DOWNTOWN_STR, 1);
        checkALocation(ATWORK_STR, 2);
        checkALocation(ATHOME_STR, 3);
        checkALocation(ATSCHOOL_STR, 4);
    }

    @Test
    public void someDefaultLocationsCanBeDeletedOtherNot(){
        //TODO: write the test suite.
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
            checkALocation(mTitleToBeTyped, i+defaultLocationsOffset);
        }
    }


    /**
     * Test that we can't add a location with an empty title.
     */
    @Test
    public void testCannotAddLocationWithEmptyTitle() {
        //Create a task with empty titles
        onView(withId(R.id.add_task_button)).perform(click());
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
        //Create a first location
        createALocation(mTitleToBeTyped);

        //Try to create a second class with the same title as the first one
        onView(withId(R.id.add_location_button)).perform(click());
        onView(withId(R.id.locationName)).perform(typeText(mTitleToBeTyped));
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
        emptyDatabase(1);
    }
}
