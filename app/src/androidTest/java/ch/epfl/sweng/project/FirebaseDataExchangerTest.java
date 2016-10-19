package ch.epfl.sweng.project;


import android.media.UnsupportedSchemeException;
import android.support.test.runner.AndroidJUnit4;

/**
 *  Unit Test of the communication layer, since there is no firebase database for now, only
 *  checks the exceptions are thrown
 */
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import ch.epfl.sweng.project.authentication.User;

import static android.support.test.InstrumentationRegistry.getContext;

@RunWith(AndroidJUnit4.class)
public final class FirebaseDataExchangerTest {

    private FirebaseDataExchanger fDE;
    private Task dummyTask1;
    private Task dummyTask2;
    private User dummyUser;

    @Rule
    public ExpectedException thrownException = ExpectedException.none();

    @Before
    public void initPrivateField(){
        fDE = new FirebaseDataExchanger(getContext());
        dummyTask1 = new Task("Task1", "dummy task1");
        dummyTask2 = new Task("Task2", "dummy task2");
        dummyUser = new User("250396", "viaccoz", "cedric.viaccoz@epfl.ch", "Cédric", "Viaccoz");
    }

    public void hasAccessTest(){
        thrownException.expect(UnsupportedSchemeException.class);
    }

    public void retrieveAllDataTest(){
        thrownException.expect(UnsupportedSchemeException.class);
        fDE.retrieveAllData(dummyUser);
    }

    public void addNewTaskTest(){
        thrownException.expect(UnsupportedSchemeException.class);
        fDE.addNewTask(dummyTask1);
    }

    public void updateTaskTest(){
        thrownException.expect(UnsupportedSchemeException.class);
        fDE.updateTask(dummyTask1, dummyTask2);
    }

    public void deleteTaskTest(){
        thrownException.expect(UnsupportedSchemeException.class);
        fDE.deleteTask(dummyTask1);
    }


}
