package ch.epfl.sweng.project;


import android.support.test.rule.ActivityTestRule;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;
import android.support.test.uiautomator.Until;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import ch.epfl.sweng.project.notification.TaskNotification;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.uiautomator.UiDevice.getInstance;
import static com.facebook.FacebookSdk.getApplicationContext;
import static org.junit.Assert.assertTrue;

public class NotificationTest extends SuperTest {
    private static long untilTimeout;
    private UiDevice mDevice;
    private Task task;

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(
            MainActivity.class);

    @Before
    public void setUp() throws Exception {
        mDevice = getInstance(getInstrumentation());
        untilTimeout = 10000;
    }

    @Before
    public void addTheTask() {
        String taskName = "Test notification";
        String taskDescription = "Description for the notification";

        createATask(taskName, taskDescription);
        String defaultLocation = "Everywhere";
        Date defaultDueDate = new Date();
        long defaultDuration = 5;
        String defaultEnergy = Task.Energy.NORMAL.toString();
        String contributor = User.DEFAULT_EMAIL;
        long startDuration = 30;
        task = new Task(taskName, taskDescription, defaultLocation, defaultDueDate, defaultDuration, defaultEnergy, Collections.singletonList(contributor), startDuration);
    }


    @Test
    public void testNotification() throws UiObjectNotFoundException {
        ArrayList<Task> taskList = new ArrayList<>();
        taskList.add(task);

        TaskNotification notificationBuilder = new TaskNotification(taskList, getApplicationContext());
        notificationBuilder.scheduleNotification(notificationBuilder.buildNotification(task.getName()), 1, 0);

        mDevice.waitForWindowUpdate(null, 13000);
        mDevice.openNotification();
        mDevice.wait(Until.hasObject(By.pkg("com.android.systemui")), untilTimeout);

        UiSelector notificationStackScroller = new UiSelector().packageName("com.android.systemui")
                .className("android.widget.ScrollView")
                .resourceId("com.android.systemui:id/notification_stack_scroller");
        UiObject notificationStackScrollerUiObject = mDevice.findObject(notificationStackScroller);

        //access top notification in the center through parent
        UiObject notificationSelectorUiObject = notificationStackScrollerUiObject.getChild(new UiSelector().textContains("Test notification"));
        assertTrue(notificationSelectorUiObject.exists());

        //Perform click on notification
        pressBack();
    }
}
