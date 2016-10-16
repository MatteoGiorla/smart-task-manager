package ch.epfl.sweng.project.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import ch.epfl.sweng.project.Task;

/**
 * Manages a local database for task data.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    // If the database schema change, you MUST increment the database version.
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context, String DATABASE_NAME) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Override the method onCreate to generate the
     * SQL table that will contain the tasks.
     * @param db an SQLiteDatabase
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        //Create a table to hold the tasks.
        final String SQL_CREATE_TASK_TABLE = "CREATE TABLE " + DatabaseContract.TaskEntry.TABLE_NAME + " (" +
                DatabaseContract.TaskEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                DatabaseContract.TaskEntry.COLUMN_TASK_TITLE + " TEXT NOT NULL, " +
                DatabaseContract.TaskEntry.COLUMN_TASK_DESCRIPTION + " TEXT NOT NULL " +
                " )";

        db.execSQL(SQL_CREATE_TASK_TABLE);
    }


    /**
     * This method will be called when the database version is changed.
     * It delete and create the database from scratch with the new
     * schema.
     *
     * @param db an SQLiteDatabase
     * @param oldVersion old version of the database
     * @param newVersion new version of the database
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Note that this only fires if you change the version number for your database.
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.TaskEntry.TABLE_NAME);
        onCreate(db);
    }

    /**
     * Add the given task in the database.
     *
     * @param task Task that need to be added in the database
     * @return true if the task was added correctly otherwise false
     */
    public boolean addData(Task task) {
        SQLiteDatabase mDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseContract.TaskEntry.COLUMN_TASK_TITLE, task.getName());
        contentValues.put(DatabaseContract.TaskEntry.COLUMN_TASK_DESCRIPTION, task.getDescription());

        long resultQuery = mDatabase.insert(DatabaseContract.TaskEntry.TABLE_NAME, null, contentValues);

        return resultQuery != -1;
    }

    /**
     * Provide all the content in the database.
     *
     * @return Cursor Content of the database
     */
    public Cursor getAllContents() {
        SQLiteDatabase mDatabase = this.getWritableDatabase();
        return mDatabase.rawQuery("SELECT * FROM " + DatabaseContract.TaskEntry.TABLE_NAME, null);
    }

    /**
     * Delete the given task from the database.
     *
     * @param task Task that need to be deleted from the databases
     * @return true if the task was deleted correctly otherwise false
     */
    public boolean removeData(Task task) {
        SQLiteDatabase mDatabase = this.getWritableDatabase();

        String table = DatabaseContract.TaskEntry.TABLE_NAME;
        String whereClause = DatabaseContract.TaskEntry.COLUMN_TASK_TITLE + " = ? and " +
                DatabaseContract.TaskEntry.COLUMN_TASK_DESCRIPTION + " = ?";
        String whereArgs[] = {task.getName(), task.getDescription()};

        int nbRowAffected = mDatabase.delete(table, whereClause, whereArgs);

<<<<<<< HEAD
=======
        return nbRowAffected == 1;
    }

    /**
     * Update a task from the database with a new one.
     *
     * @param oldTask The task to be edited
     * @param newTask THe new task
     * @return true if the task was updated correctly otherwise false
     */
    public boolean editTask(Task oldTask, Task newTask) {
        SQLiteDatabase mDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseContract.TaskEntry.COLUMN_TASK_TITLE, newTask.getName());
        contentValues.put(DatabaseContract.TaskEntry.COLUMN_TASK_DESCRIPTION, newTask.getDescription());

        String table = DatabaseContract.TaskEntry.TABLE_NAME;
        String whereClause = DatabaseContract.TaskEntry.COLUMN_TASK_TITLE + " = ?";
        String whereArgs[] = {oldTask.getName()};
        int nbRowAffected = mDatabase.update(table, contentValues, whereClause, whereArgs);

>>>>>>> edit_task
        return nbRowAffected == 1;
    }
}
