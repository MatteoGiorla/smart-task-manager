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
import static junit.framework.Assert.fail;

@RunWith(AndroidJUnit4.class)
public class AuthenticationTest {
    private String mGoogEmail;
    private String mGoogPassword;
    private UiDevice mUiDevice;
    private final static String NEXT_BUTTON_ID = "identifierNext";
    private final static String PASSWORD_NEXT_ID = "passwordNext";
    private final static String ACCEPT_ID = "next";
    private final static String GOOGSERV_NEXT = "com.google.android.gms:id/suw_navbar_next";
    private long untilTimeout;

    @Before
    public void setup(){
        mUiDevice = getInstance(getInstrumentation());
        mGoogEmail = "trixyfinger@gmail.com";
        mGoogPassword = "sweng1234TaskIt";
        untilTimeout = 3000; //an estimation, but the connection procedure should not last more
    }

    @After
    public void tearDown(){
        removeAccount();
    }

    private void removeAccount() {
        mUiDevice.pressHome();

    }

    @Rule
    public ActivityTestRule<LoginActivity> mActivityRule =
            new ActivityTestRule<LoginActivity>(LoginActivity.class);

    /**
     * perform user like actions on the phone to authenticate
     * oneself.
     */
    @Test
    public void GoogleLoginWorks() {
            onView(withId(R.id.google_sign_in_button)).perform(click());
            //first check if the user is already registered, if so just proceed to login.
            UiObject mEmailText = mUiDevice.findObject(new UiSelector().text(mGoogEmail));
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
                    associateNewGoogleAccount();
                }
            }
    }

    /**
     * method that proceeds to go through all the authentification process
     * when it is the first time we try to connect a google account with the app.
     */
    private void associateNewGoogleAccount() {
        try{
            UiObject emailHint = mUiDevice.findObject(new UiSelector().text("Enter your email"));
            emailHint.setText(mGoogEmail);

            UiObject nextAction = mUiDevice.findObject(new UiSelector().resourceId(NEXT_BUTTON_ID));
            nextAction.clickAndWaitForNewWindow(untilTimeout);

            UiObject passwordHint = mUiDevice.findObject(new UiSelector().text("Password"));
            passwordHint.setText(mGoogPassword);

            nextAction = mUiDevice.findObject(new UiSelector().resourceId(PASSWORD_NEXT_ID));
            nextAction.clickAndWaitForNewWindow(untilTimeout);

            UiObject acceptAction = mUiDevice.findObject(new UiSelector().resourceId(ACCEPT_ID));
            acceptAction.clickAndWaitForNewWindow();

            UiScrollable googServices = new UiScrollable(new UiSelector().scrollable(true));
            googServices.scrollForward();

            UiObject nextGoogleServ = mUiDevice.findObject(new UiSelector().text("Next"));
            nextGoogleServ.clickAndWaitForNewWindow();

        }catch (UiObjectNotFoundException u ){
            fail("There was an error while trying to associate new Google account with the app.");
        }
        checkIfMainActivity();
    }

    /**
     * serves as only checking if the add button is displayed,
     * which is equivalent as checking if we reached mainActivity
     */
    private void checkIfMainActivity(){
        onView(withId(R.id.add_task_button)).check(matches(isDisplayed()));
    }
 }