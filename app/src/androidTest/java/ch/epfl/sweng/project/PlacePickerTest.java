package ch.epfl.sweng.project;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.project.location_setting.LocationFragment;
import ch.epfl.sweng.project.location_setting.LocationSettingActivity;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.uiautomator.UiDevice.getInstance;
import static junit.framework.Assert.fail;

@RunWith(AndroidJUnit4.class)
public class PlacePickerTest {

    private static UiDevice mUiDevice;
    private static final String SEARCH_BAR = "com.google.android.gms:id/search_bar";
    private static final String SEL_LOCATION_TXT = "Select this location";
    private static final String EPFL = "EPFL";
    @Rule
    public ActivityTestRule<LocationSettingActivity> mActivityRule =
            new ActivityTestRule<>(LocationSettingActivity.class);

    @Before
    public void setup() {
        mUiDevice = getInstance(getInstrumentation());
    }

    @After
    public void tearDown(){
        mUiDevice.pressBack();
        mUiDevice.pressBack();
    }

    @Test
    public void canAddCustomLocationWithSearchBar(){
        onView(withId(R.id.add_location_button)).perform(click());
        onView(withId(R.id.choose_location)).perform(click());
        UiObject searchBar = mUiDevice.findObject(new UiSelector().resourceId(SEARCH_BAR));
        try{
            searchBar.clickAndWaitForNewWindow();
            UiObject searchField = mUiDevice.findObject(new UiSelector().text("Search"));
            searchField.setText("EPFL");
            //remove keyboard menu
            mUiDevice.pressBack();
            synchronized (mUiDevice){
                mUiDevice.wait(5000);
            }
            UiObject firstProposition = mUiDevice.findObject(new UiSelector().className("android.widget.RelativeLayout").index(0));
            firstProposition.clickAndWaitForNewWindow();
            UiObject selectLocation = mUiDevice.findObject(new UiSelector().text("SELECT"));
            selectLocation.clickAndWaitForNewWindow();
            Thread.sleep(5000);

            onView(withId(R.id.locationName)).perform(typeText(EPFL));
            pressBack();
            onView(withId(R.id.location_done_button_toolbar)).perform(click());

            //Check that the title has been updated
            SuperTest.checkALocation(EPFL, 5);

        }catch(UiObjectNotFoundException u){
            fail("Something went wrong with UiAutomator actions : " + u.getMessage());
        }catch(java.lang.InterruptedException i){
            fail(i.getMessage());
        }
    }


    //@Test
    public void canAddCustomLocationWithPlacePicker(){
        onView(withId(R.id.add_location_button)).perform(click());
        onView(withId(R.id.choose_location)).perform(click());
        UiObject selectLocation = mUiDevice.findObject(new UiSelector().text(SEL_LOCATION_TXT));
        try{
            selectLocation.click();
            //TODO: when this buton works (cf bastian nanchen from Granges) test it.

        }catch(UiObjectNotFoundException u){
            fail("Something went wrong with UiAutomator actions.");
        }
    }
}