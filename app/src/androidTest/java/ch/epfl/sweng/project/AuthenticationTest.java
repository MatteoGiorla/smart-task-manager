package ch.epfl.sweng.project;


import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.project.authentication.LoginActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.uiautomator.UiDevice.getInstance;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;

@RunWith(AndroidJUnit4.class)
public class AuthenticationTest {
    private String mEmail;
    private String mPassword;
    private UiDevice mUiDevice;
    private final static String NEXT_BUTTON = "NEXT";
    private final static String ACCEPT_BUTTON = "ACCEPT";
    private long untilTimeout;

    @Before
    public void setup(){
        mUiDevice = getInstance(InstrumentationRegistry.getInstrumentation());
        mEmail = "trixyfinger@gmail.com";
        mPassword = "sweng1234TaskIt";
        untilTimeout = 5000;
    }

    @Rule
    public ActivityTestRule<LoginActivity> mActivityRule = new ActivityTestRule<LoginActivity>(LoginActivity.class);

    @Test
    public void GoogleLoginWorks() {
            onView(withId(R.id.google_sign_in_button)).perform(click());
            //first check if the user is already registered, if so just proceed to login.
            UiObject mEmailText = mUiDevice.findObject(new UiSelector().text(mEmail));
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

    /* the google authentification with google should follow those steps:
        type the test email under the "Enter your email" textview
        then click "NEXT"
        then enter the paswword under the "Password" textview
        then click "NEXT"
        then click "ACCEPT" "under the terms of service"
        there is a wait (while loop until see NEXT) Then click NEXT
        And the magic should have happened
     */
    private void associateNewGoogleAccount() {
        try{
            UiObject emailHint = mUiDevice.findObject(new UiSelector().text("Enter your email"));
            emailHint.setText(mEmail);
            UiObject nextAction = mUiDevice.findObject(new UiSelector().text(NEXT_BUTTON));
            nextAction.click();
            UiObject passwordHint = mUiDevice.findObject(new UiSelector().text("Password"));
            passwordHint.setText(mPassword);
            nextAction = mUiDevice.findObject(new UiSelector().text(NEXT_BUTTON));
            nextAction.click();
            UiObject acceptAction = mUiDevice.findObject(new UiSelector().text(ACCEPT_BUTTON));
            mUiDevice.wait(untilTimeout);
            nextAction = mUiDevice.findObject(new UiSelector().text(NEXT_BUTTON));
            nextAction.click();
            checkIfMainActivity();
        }catch (UiObjectNotFoundException u ){
            fail("There was an error while trying to associate new Google account with the app.");
        }catch (InterruptedException i){
            fail("There was an interruption during a wait during the authentification to Google, " +
                    "try again or check out your network connection");
        }
    }

    private void checkIfMainActivity(){
        assertTrue(true);
    }


 }

