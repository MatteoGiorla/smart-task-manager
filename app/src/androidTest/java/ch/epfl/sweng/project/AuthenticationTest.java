package ch.epfl.sweng.project;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiScrollable;
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
import static junit.framework.Assert.fail;

@RunWith(AndroidJUnit4.class)
public class AuthenticationTest {

    private final static String NEXT_BUTTON_ID = "identifierNext";
    private final static String PASSWORD_NEXT_ID = "passwordNext";
    private final static String ACCEPT_ID = "next";
    private static String mGoogleEmail;
    private static String mGooglePassword;
    private static long untilTimeout;

    @Rule
    public ActivityTestRule<LoginActivity> mActivityRule =
            new ActivityTestRule<>(LoginActivity.class);
    private UiDevice mUiDevice;

    @Before
    public void setup() {
        mUiDevice = getInstance(getInstrumentation());
        mGoogleEmail = "trixyfinger@gmail.com";
        mGooglePassword = "sweng1234TaskIt";
        untilTimeout = 5000;
    }

    @After
    public void tearDown() {
        goBack();
        /*try{
            removeAccount();
        }catch (UiObjectNotFoundException e){
            Log.d("Authentication test", "Error something not found.");
        }*/
    }

    private void goBack() {
        mUiDevice.pressBack();
        mUiDevice.pressBack();
    }

    /**
     * Click on the facebook sign in button,
     * check if the facebook login is launched upon it,
     * or if it grants immediately access to the mainActivity.
     */
    //@Test
    public void facebookSignInGetLaunch() {
        onView(withId(R.id.facebook_sign_in_button)).perform(click());
        //the android.webkit.WebView launched by facebook should be the only to have those properties
        UiObject facebookWebLaunched = mUiDevice.findObject(new UiSelector().className("android.webkit.WebView"));
        try {
            facebookWebLaunched.click();
            assertTrue("Facebook login web window correctly launched", true);
        } catch (UiObjectNotFoundException u) {
//            onView(withId(R.id.add_task_button)).check(matches(isDisplayed()));
        }
    }



    /**
     * perform user like actions on the phone to authenticate
     * oneself into a google account (even if it is already memorized).
     */
    //Test
    public void googleLoginWorks() {
        onView(withId(R.id.google_sign_in_button)).perform(click());
        //first check if the user is already registered, if so just proceed to login.
        UiObject mEmailText = mUiDevice.findObject(new UiSelector().text(mGoogleEmail));
        try{
            mEmailText.click();
            checkIfMainActivity();
        } catch(UiObjectNotFoundException u1){
            //if not, check if we are on the popup with
            UiObject addAcount = mUiDevice.findObject(new UiSelector().text("Add account"));
            try{
                addAcount.click();
                associateNewGoogleAccount();
            }catch(UiObjectNotFoundException u2){
                checkIfMainActivity();
            }
        }
    }



    /**
     * method that proceeds to go through all the authentification process
     * when it is the first time we try to connect a google account with the app.
     *
     * The test might takes some time after clicking on the sign in button,
     * the transition between espresso and uiautomator might be responsible
     * for that.
     */
    private void associateNewGoogleAccount() {
        try{
            UiObject emailHint = mUiDevice.findObject(new UiSelector().text("Enter your email"));
            emailHint.setText(mGoogleEmail);

            UiObject nextAction = mUiDevice.findObject(new UiSelector().resourceId(NEXT_BUTTON_ID));
            nextAction.clickAndWaitForNewWindow(untilTimeout);

            UiObject passwordHint = mUiDevice.findObject(new UiSelector().resourceId("password"));
            passwordHint.setText(mGooglePassword);

            nextAction = mUiDevice.findObject(new UiSelector().resourceId(PASSWORD_NEXT_ID));
            nextAction.clickAndWaitForNewWindow(untilTimeout);

            UiObject acceptAction = mUiDevice.findObject(new UiSelector().resourceId(ACCEPT_ID));
            acceptAction.clickAndWaitForNewWindow();

            UiScrollable googServices = new UiScrollable(new UiSelector().scrollable(true));
            googServices.scrollForward();

            UiObject nextGoogleServer = mUiDevice.findObject(new UiSelector().text("Next"));
            nextGoogleServer.clickAndWaitForNewWindow();

        }catch (UiObjectNotFoundException u ){
            fail("There was an error while trying to associate new Google account with the app.");
        }
        checkIfMainActivity();
    }

    @Test
    public void removeAccount() throws UiObjectNotFoundException {
        mUiDevice.pressHome();
        //click on main button
        //index : 2, package : com.android.launcher3, content_description: Apps
        UiObject UiAppTray = mUiDevice.findObject(new UiSelector().descriptionContains("Apps"));
        UiAppTray.click();
        UiObject UiSettingsIcon = mUiDevice.findObject(new UiSelector().text("Settings"));
        UiSettingsIcon.clickAndWaitForNewWindow();

        //scroll the settings
        UiScrollable settingsView = new UiScrollable(new UiSelector().scrollable(true));
        //put the scroller on the upmost position to scrollForward correctly after.
        settingsView.flingBackward();
        settingsView.scrollForward();
        settingsView.scrollForward();

        UiObject UiAccount = mUiDevice.findObject(new UiSelector().text("Accounts"));
        UiAccount.click();
        UiObject GoogleAccount = mUiDevice.findObject(new UiSelector().text("Google"));
        GoogleAccount.click();
        UiObject threeDots = mUiDevice.findObject(new UiSelector().descriptionContains("More options"));
        threeDots.click();

        UiObject deleteAccountOption = mUiDevice.findObject(new UiSelector().text("Remove account"));
        deleteAccountOption.click();

        UiObject removeCurrAccount = mUiDevice.findObject(new UiSelector().text("REMOVE ACCOUNT"));
        removeCurrAccount.click();

    }

    /**
     * serves as only checking if the add button is displayed,
     * which is equivalent as checking if we reached mainActivity
     */
    private void checkIfMainActivity(){
        onView(withId(R.id.add_task_button)).check(matches(isDisplayed()));
    }


    /**
     * check if we reached location_settings activity upon first launch
     */
    private void checkIfLocationSettingsActivity(){
        onView(withId(R.id.add_location_button)).check(matches(isDisplayed()));
    }
}