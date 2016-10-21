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

import ch.epfl.sweng.project.authentication.LoginActivity;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.uiautomator.UiDevice.getInstance;
import static junit.framework.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class AuthenticationTest {
    private UiDevice mUiDevice;

    @Before
    public void setup(){
        mUiDevice = getInstance(getInstrumentation());
    }

    @After
    public void tearDown(){
        goBack();
    }

    private void goBack() {
        mUiDevice.pressBack();
        mUiDevice.pressBack();
    }

    @Rule
    public ActivityTestRule<LoginActivity> mActivityRule =
            new ActivityTestRule<LoginActivity>(LoginActivity.class);

    /**
     * Click on the facebook sign in button,
     * check if the facebook login is launched upon it,
     * or if it grants immediately access to the mainActivity.
     */
    @Test
    public void facebookSignInGetLaunch(){
        onView(withId(R.id.facebook_sign_in_button)).perform(click());
        //the android.webkit.WebView launched by facebook should be the only to have those properties
        UiObject facebookWebLaunched = mUiDevice.findObject(new UiSelector().className("android.webkit.WebView"));
        try{
            facebookWebLaunched.click();
            assertTrue("Facebook login web window correctly launched", true);
        }catch(UiObjectNotFoundException u){
            onView(withId(R.id.add_task_button)).check(matches(isDisplayed()));
        }
    }

 }