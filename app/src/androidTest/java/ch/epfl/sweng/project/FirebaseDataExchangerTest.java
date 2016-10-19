package ch.epfl.sweng.project;


import android.content.Context;
import android.media.UnsupportedSchemeException;
import android.net.wifi.WifiManager;
import android.support.test.runner.AndroidJUnit4;

/**
 *  Unit Test of the communication layer, since there is no firebase database for now, only
 *  checks the exceptions are thrown
 */
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import ch.epfl.sweng.project.authentication.User;

import static android.support.test.InstrumentationRegistry.getContext;
import static junit.framework.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public final class FirebaseDataExchangerTest {

    private FirebaseDataExchanger fDE;
    private Task dummyTask1;
    private Task dummyTask2;
    private User dummyUser;
    private Context context;
    final private boolean ON = true;
    final private boolean OFF = false;

    @Rule
    public ExpectedException thrownException = ExpectedException.none();

    @Before
    public void setup(){
        wifiSwitch(ON);
    }

    public void initPrivateField(){
        context = getContext();
        fDE = new FirebaseDataExchanger(context);
        dummyTask1 = new Task("Task1", "dummy task1");
        dummyTask2 = new Task("Task2", "dummy task2");
        dummyUser = new User("250396", "viaccoz", "cedric.viaccoz@epfl.ch", "Cédric", "Viaccoz");
    }

    @Test
    public void hasAccessTest(){
        wifiSwitch(OFF);
        assertEquals(false, fDE.hasAccess());
        wifiSwitch(ON);
        //without access to internet, the test should fail.
        thrownException.expect(UnsupportedSchemeException.class);
        fDE.hasAccess();
    }

    @Test
    public void retrieveAllDataTest(){
        thrownException.expect(UnsupportedSchemeException.class);
        fDE.retrieveAllData(dummyUser);
    }

    @Test
    public void addNewTaskTest(){
        thrownException.expect(UnsupportedSchemeException.class);
        fDE.addNewTask(dummyTask1);
    }

    @Test
    public void updateTaskTest(){
        thrownException.expect(UnsupportedSchemeException.class);
        fDE.updateTask(dummyTask1, dummyTask2);
    }

    @Test
    public void deleteTaskTest(){
        thrownException.expect(UnsupportedSchemeException.class);
        fDE.deleteTask(dummyTask1);
    }

    /**
     * Utilitary method to switch on or off the wifi connections of the phone,
     * to
     *
     * @param On sense of the switch (true = on, false = off)
     */
    private void wifiSwitch(Boolean On){
        WifiManager wifi=(WifiManager) context.getSystemService(context.WIFI_SERVICE);
        wifi.setWifiEnabled(On);
    }


}
