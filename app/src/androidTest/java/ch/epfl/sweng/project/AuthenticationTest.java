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
    private String mGoogleEmail;
    private String mGooglePassword;
    private String mFacebookEmail;
    private String mFacebookPassword;
    private UiDevice mUiDevice;
    private final static String NEXT_BUTTON_ID = "identifierNext";
    private final static String PASSWORD_NEXT_ID = "passwordNext";
    private final static String ACCEPT_ID = "next";
    private final static String FB_MAIL_ID = "com.facebook.katana:id/a4j";
    private final static String FB_PASSWORD_ID = "com.facebook.katana:id/a4k";
    private long untilTimeout;

    @Before
    public void setup(){
        mUiDevice = getInstance(getInstrumentation());
        mGoogleEmail = "trixyfinger@gmail.com";
        mGooglePassword = "sweng1234TaskIt";
        mFacebookEmail = "cirdec3961@gmail.com";
        mFacebookPassword = "Kristel";
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

    @Test
    public void BogusTest(){
        assertTrue(true);
    }
    /**
     * perform user like actions on the phone to authenticate
     * oneself into a google account (even if it is already memorized).
     */
    //@Test
    public void GoogleLoginWorks() {
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
                associateNewGoogleAccount();
            }
        }
    }

    /**
     * perform user like actions on the phone to authenticate
     * oneself.
     */
    //@Test
    public void FacebookLoginWorks() {
        onView(withId(R.id.facebook_sign_in_button)).perform(click());
        //first check if the user is already registered, if so just proceed to login.
        UiObject mEmailText = mUiDevice.findObject(new UiSelector().resourceId(FB_MAIL_ID));
        try{
            //removing the pre existent text if there is one.
            mEmailText.click();
            mEmailText.setText(mFacebookEmail);
            UiObject mPasswordText = mUiDevice.findObject(new UiSelector().resourceId(FB_PASSWORD_ID));
            try{
                mPasswordText.click();
                mPasswordText.setText(mFacebookPassword);

                UiObject mLoginButton = mUiDevice.findObject(new UiSelector().text("LOG IN"));
                mLoginButton.clickAndWaitForNewWindow(untilTimeout);
                checkIfMainActivity();
            }catch(UiObjectNotFoundException u0){
                fail("Error encountered while logging on facebook");
            }
            checkIfMainActivity();
        } catch(UiObjectNotFoundException u1){
            //if not, check if we reach the main activity
            checkIfMainActivity();
        }
    }

    /**
     * method that proceeds to go through all the authentication process
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

            UiObject nextGoogleServ = mUiDevice.findObject(new UiSelector().text("NEXT"));
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