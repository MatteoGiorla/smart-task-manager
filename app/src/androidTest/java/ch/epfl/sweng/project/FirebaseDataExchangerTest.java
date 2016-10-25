package ch.epfl.sweng.project;


import android.content.Context;
import android.net.wifi.WifiManager;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.project.data.FirebaseDataExchanger;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;

/**
 * Unit Test of the communication layer, since there is no firebase database for now, only
 * checks the exceptions are thrown
 */

@RunWith(AndroidJUnit4.class)
public final class FirebaseDataExchangerTest {

    private FirebaseDataExchanger fDE;
    private Task dummyTask1;
    private Task dummyTask2;
    private Context context;
    final private boolean ON = true;
    final private boolean OFF = false;

    @Before
    public void setup(){
        initPrivateField();
    }

    @After
    public void tearDown(){
        NetworkSwitch(ON);
    }

    private void initPrivateField(){
        context = getInstrumentation().getContext();
        fDE = new FirebaseDataExchanger(context);
        dummyTask1 = new Task("Task1", "dummy task1");
        dummyTask2 = new Task("Task2", "dummy task2");

    }

    @Test
    public void retrieveAllDataTest(){
        try{
            fDE.retrieveAllData();
            fail("Should have thrown an UnsupportedOperation Exception");
        }catch(UnsupportedOperationException uns){
            assertTrue("Need implementing Firebase connection", true);
        }

    }

    @Test
    public void addNewTaskTest(){
        try{
            fDE.addNewTask(dummyTask1);
            fail("Should have thrown an UnsupportedOperation Exception");
        }catch(UnsupportedOperationException uns){
            assertTrue("Need implementing Firebase connection", true);
        }

    }

    @Test
    public void updateTaskTest(){
        try{
            fDE.updateTask(dummyTask1, dummyTask2);
            fail("Should have thrown an UnsupportedOperation Exception");
        }catch(UnsupportedOperationException uns){
            assertTrue("Need implementing Firebase connection", true);
        }

    }

    @Test
    public void deleteTaskTest(){
        try{
            fDE.deleteTask(dummyTask1);
            fail("Should have thrown an UnsupportedOperation Exception");
        }catch(UnsupportedOperationException uns){
            assertTrue("Need implementing Firebase connection", true);
        }
    }

    /**
     * Utilitary method to switch on or off the network connections of the phone
     *
     * @param enable sense of the switch (true = on, false = off)
     */
    private void NetworkSwitch(Boolean enable){
        WifiManager wifiManager = (WifiManager)this.context.getSystemService(Context.WIFI_SERVICE);
        wifiManager.setWifiEnabled(enable);
    }

}
