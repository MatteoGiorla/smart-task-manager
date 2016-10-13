package ch.epfl.sweng.project;

import android.database.Cursor;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.project.data.DatabaseContract;
import ch.epfl.sweng.project.data.DatabaseHelper;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;

/**
 * Unit Tests
 * Testing the database with its only features.
 */
@RunWith(AndroidJUnit4.class)
public class DatabaseTest {
    private String name;
    private String description;
    private Task task;
    private final String TASK_NAME = "task";
    private final String TASK_DESCR = "This is a task";

    //It is private to the database helper unfortunately
    private static final String DATABASE_NAME = "task.db";
    private DatabaseHelper testDbHelper;

    @Before
    public void initTask() {
        name = TASK_NAME;
        description = TASK_DESCR;
        task = new Task(name, description);
    }

    @Before
    public void dbSetUp() {
        getTargetContext().deleteDatabase(DATABASE_NAME);
        testDbHelper = new DatabaseHelper(getTargetContext());
    }

    //to avoid having a stale database in the system.
    @After
    public void tearDown(){
        testDbHelper.close();
    }


    /**
     * Test if after adding a task, the database contains such a task.
     */
    @Test
    public void databaseTest(){
        Cursor content = testDbHelper.getAllContents();

        //Ensuring the good numbers of Columns is in the Db
        assertEquals(3, content.getColumnCount());
        //Ensuring the dataBase upon first use has no data.
        assertEquals(0, content.getCount() );

        testDbHelper.addData(task);

        content = testDbHelper.getAllContents();
        if(content.moveToFirst()){
            //Ensuring the db does have an entry after calling addData
            assertEquals(1, content.getCount());

            //checking the name and the description of the task as saved by 
            String nameInDb = content.getString(content.getColumnIndex(DatabaseContract.TaskEntry.COLUMN_TASK_TITLE));
            String descriptionInDb = content.getString(content.getColumnIndex(DatabaseContract.TaskEntry.COLUMN_TASK_DESCRIPTION));
            assertTrue(nameInDb.equals(name));
            assertTrue(descriptionInDb.equals(description));
        }else{
            //the cursor should be able to move to the first element.
            fail();
        }
    }
}

