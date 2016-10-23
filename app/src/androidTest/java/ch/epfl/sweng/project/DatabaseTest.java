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
    //It is private to the database helper unfortunately
    private static final String DATABASE_NAME = "testTask.db";
    private String name;
    private String description;
    private Task task;
    private DatabaseHelper testDbHelper;

    @Before
    public void initTask() {
        name = "task";
        description = "This is a task";
        task = new Task(name);
        task.setDescription(description);
    }

    @Before
    public void dbSetUp() {
        testDbHelper = new DatabaseHelper(getTargetContext(), DATABASE_NAME);
    }

    //Close and delete the test database once test finish.
    @After
    public void tearDown() {
        testDbHelper.close();
        getTargetContext().deleteDatabase(DATABASE_NAME);
    }


    /**
     * Test if after adding a task, the database contains such a task.
     */
    @Test
    public void TestDatabaseAddCorrectly() {
        Cursor content = testDbHelper.getAllContents();

        //Ensuring the good numbers of Columns is in the Db
        assertEquals(3, content.getColumnCount());
        //Ensuring the dataBase upon first use has no data.
        assertEquals(0, content.getCount());

        testDbHelper.addData(task);

        content = testDbHelper.getAllContents();
        if (content.moveToFirst()) {
            //Ensuring the db does have an entry after calling addData
            assertEquals(1, content.getCount());

            //checking the name and the description of the task as saved by 
            String nameInDb = content.getString(content.getColumnIndex(DatabaseContract.TaskEntry.COLUMN_TASK_TITLE));
            String descriptionInDb = content.getString(content.getColumnIndex(DatabaseContract.TaskEntry.COLUMN_TASK_DESCRIPTION));
            assertTrue(nameInDb.equals(name));
            assertTrue(descriptionInDb.equals(description));
        } else {
            //the cursor should be able to move to the first element.
            fail();
        }
    }

    /** Dummy test to check that the variable are not removed **/
    @Test
    public void TestDatabaseContract() {
        String DatabaseName = DatabaseContract.DATABASE_NAME;
        String TableName = DatabaseContract.TaskEntry.TABLE_NAME;
        String ColumnDescription = DatabaseContract.TaskEntry.COLUMN_TASK_DESCRIPTION;
        String ColumnTitle = DatabaseContract.TaskEntry.COLUMN_TASK_TITLE;
        assertEquals(DatabaseContract.DATABASE_NAME, DatabaseName);
        assertEquals(DatabaseContract.TaskEntry.TABLE_NAME, TableName);
        assertEquals(DatabaseContract.TaskEntry.COLUMN_TASK_DESCRIPTION, ColumnDescription);
        assertEquals(DatabaseContract.TaskEntry.COLUMN_TASK_TITLE, ColumnTitle);
    }
}

